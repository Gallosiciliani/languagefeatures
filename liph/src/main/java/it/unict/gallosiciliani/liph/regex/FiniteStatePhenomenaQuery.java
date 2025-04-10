package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import java.util.LinkedList;
import java.util.List;

/**
 * SPARQL query for {@link it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon}
 * @author Cristiano Longo
 */
public class FiniteStatePhenomenaQuery implements RegexFeatureQuery{
    private static final String FEATURE_IRI_VAR = "feature";
    private static final String LABEL_VAR = "label";
    private static final String REGEX_VAR = "regex";
    private static final String REPLACEMENT_VAR = "replacement";


    @Override
    public List<RegexFeatureQuerySolution> exec(final Model model){
        final String query=buildQueryString();
        try(final QueryExecution e = QueryExecutionFactory.create(query, model)){
            final List<RegexFeatureQuerySolution> result=new LinkedList<>();
            e.execSelect().forEachRemaining(querySolution -> result.add(new RegexFeatureQuerySolution(){
                @Override
                public String getFeatureIRI() {
                    return querySolution.getResource(FEATURE_IRI_VAR).getURI();
                }

                @Override
                public String getFeatureLabel(){ return querySolution.getLiteral(LABEL_VAR).getString();}

                @Override
                public String getRegex() {
                    return querySolution.getLiteral(REGEX_VAR).getString();
                }

                @Override
                public String getReplacement() {
                    return querySolution.getLiteral(REPLACEMENT_VAR).getString();
                }
            }));
            return result;
        }
    }

    /**
     * Build the query string
     * @return the query string
     */
    private String buildQueryString(){
        return "SELECT ?"+ FEATURE_IRI_VAR+" ?"+ FiniteStatePhenomenaQuery.LABEL_VAR+" ?"+ FiniteStatePhenomenaQuery.REGEX_VAR+" ?"+ REPLACEMENT_VAR+" WHERE{"+
                "?"+ FEATURE_IRI_VAR+" <"+ RDFS.label.getURI()+"> ?"+ FiniteStatePhenomenaQuery.LABEL_VAR+" ;"+
                " <"+ LinguisticPhenomena.MATCHING_PATTERN_DATA_PROPERTY+"> ?"+ FiniteStatePhenomenaQuery.REGEX_VAR+" ;"+
                " <"+ LinguisticPhenomena.REPLACE_WITH_DATA_PROPERTY+"> ?"+ FiniteStatePhenomenaQuery.REPLACEMENT_VAR+
                "} ORDER BY ?"+ FEATURE_IRI_VAR;
    }
}
