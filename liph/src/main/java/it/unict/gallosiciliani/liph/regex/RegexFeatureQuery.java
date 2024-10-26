package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import java.util.LinkedList;
import java.util.List;

/**
 * SPARQL query for Regex features
 * @author Cristiano Longo
 */
public class RegexFeatureQuery {
    private static final String FEATURE_IRI_VAR = "feature";
    private static final String REGEX_VAR = "regex";
    private static final String REPLACEMENT_VAR = "replacement";

    private String parentPropertyIRI=null;

    /**
     * Execute the query against the specified model
     * @param model the model
     * @return query results
     */
    public List<RegexFeatureQuerySolution> exec(final Model model){
        try(final QueryExecution e = QueryExecutionFactory.create(buildQueryString(), model)){
            final List<RegexFeatureQuerySolution> result=new LinkedList<>();
            e.execSelect().forEachRemaining(querySolution -> result.add(new RegexFeatureQuerySolution(){
                @Override
                public String getFeatureIRI() {
                    return querySolution.getResource(FEATURE_IRI_VAR).getURI();
                }

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
     * Restrict selection to subproperties of the specified one
     * @param parentPropertyIRI IRI of the superproperty
     * @return this query
     */
    public RegexFeatureQuery setParentProperty(final String parentPropertyIRI){
        this.parentPropertyIRI=parentPropertyIRI;
        return this;
    }

    /**
     * Build the query string
     * @return the query string
     */
    private String buildQueryString(){
        return "SELECT ?"+ FEATURE_IRI_VAR+" ?"+ RegexFeatureQuery.REGEX_VAR+" ?"+ REPLACEMENT_VAR+" WHERE{"+
                "?"+ FEATURE_IRI_VAR+" <"+ LinguisticPhenomena.REGEX_ANN_PROPERTY+"> ?"+ RegexFeatureQuery.REGEX_VAR+" ."+
                "?"+ FEATURE_IRI_VAR+" <"+ LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY+"> ?"+ RegexFeatureQuery.REPLACEMENT_VAR+
                (parentPropertyIRI==null ? "" : " . ?"+FEATURE_IRI_VAR+" <"+ RDFS.subPropertyOf.getURI()+"> <"+parentPropertyIRI+">")+
                "} ORDER BY ?"+ FEATURE_IRI_VAR;
    }
}
