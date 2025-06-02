package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.exceptions.OWLPersistenceException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.*;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Cristiano Longo
 */
@Service
@Slf4j
public class SPARQLService {
    @Autowired
    EntityManager entityManager;

    /**
     * Perform a SPARQL query on the knowledge base. The resultset is returned in
     * the CSV format as described in <a href="https://www.w3.org/TR/2013/REC-sparql11-results-csv-tsv-20130321/">SPARQL 1.1 Query Results CSV and TSV Formats</a>
     *
     * @param query the SPARQL query
     * @return the query result in CSV format
     */
    @Deprecated
    public String performSelectQuery(final String query) throws IOException, SPARQLQueryException {
        final Query q = entityManager.createNativeQuery(query);
        try {
            return SPARQLResultToCSVConverter.getResultAsCSV(q);
        } catch(final OWLPersistenceException e){
            throw new SPARQLQueryException(query,e);
        }
    }

    public String performSelectQueryJena(final String query, final ResultsFormat format) throws SPARQLQueryException {
        final Dataset dataset=entityManager.unwrap(Dataset.class);
        try {
            final QueryExecutionDatasetBuilder builder = QueryExecutionDatasetBuilder.create().query(query).dataset(dataset);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (final QueryExecution e = builder.build()) {
                final ResultSet rs = e.execSelect();
                ResultSetFormatter.output(out, rs, format);
                return out.toString(StandardCharsets.UTF_8);
            }
        } catch (final QueryParseException e) {
            throw new SPARQLQueryException(query, e);
        }
    }

}
