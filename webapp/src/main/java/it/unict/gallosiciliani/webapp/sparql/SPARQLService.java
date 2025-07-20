package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.model.EntityManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.ResultSetMgr;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Cristiano Longo
 */
@Service
@Slf4j
public class SPARQLService {
    @Autowired
    EntityManager entityManager;

    private final Map<ResultsFormat, Lang> extendedResultsFormatToLang=Map.of(
            ResultsFormat.FMT_RDF_TTL, Lang.TTL,
            ResultsFormat.FMT_RDF_TRIG, Lang.TRIG,
            ResultsFormat.FMT_RDF_N3, Lang.N3,
            ResultsFormat.FMT_RDF_NT, Lang.NTRIPLES,
            ResultsFormat.FMT_RDF_NQ, Lang.NQUADS,
            ResultsFormat.FMT_RDF_JSONLD, Lang.JSONLD,
            ResultsFormat.FMT_RDF_XML, Lang.RDFXML
    );

    private Dataset dataset;

    @PostConstruct
    void initJenaDataset(){
        dataset=entityManager.unwrap(Dataset.class);
        if (dataset==null) throw new IllegalStateException("Jena Dataset not available");
    }

    /**
     * Perform a SPARQL query on the knowledge base. The resultset is returned in
     * the CSV format as described in <a href="https://www.w3.org/TR/2013/REC-sparql11-results-csv-tsv-20130321/">SPARQL 1.1 Query Results CSV and TSV Formats</a>
     *
     * @param query the SPARQL query
     * @return the query result in CSV format
     */
    public String query(final String query, final ResultsFormat format) throws SPARQLQueryException {
        return performSelectQueryJena(query, format);
    }

    /**
     * Perform a SPARQL query on the knowledge base using Jena ARQ.
     *
     * @param query the SPARQL query
     * @param format requested results format
     * @return the query result in the requested format
     */
    private String performSelectQueryJena(final String query, final ResultsFormat format) throws SPARQLQueryException {
        try {
            final QueryExecutionDatasetBuilder builder = QueryExecutionDatasetBuilder.create().query(query).dataset(dataset);
            final Lang outLang=convert(format);
            try (final QueryExecution e = builder.build()) {
                if (e.getQuery().isAskType())
                    return ResultSetMgr.asString(e.execAsk(), outLang);
                if (e.getQuery().isSelectType())
                    return ResultSetMgr.asString(e.execSelect(), outLang);
                if (e.getQuery().isDescribeType())
                    return RDFWriter.source(e.execDescribe()).lang(outLang).asString();
                if (e.getQuery().isConstructType())
                    return RDFWriter.source(e.execConstruct()).lang(outLang).asString();
                throw new UnsupportedOperationException("Unsupported query type");
            }
        } catch (final QueryParseException e) {
            throw new SPARQLQueryException(query, e);
        }
    }


    private Lang convert(final ResultsFormat format){
        final Lang convertedByJena=ResultsFormat.convert(format);
        return convertedByJena!=null ?  convertedByJena : extendedResultsFormatToLang.get(format);
    }
}
