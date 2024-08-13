package it.unict.gallosiciliani.liphtools.liph.regex;

import it.unict.gallosiciliani.liphtools.liph.LinguisticPhenomena;
import org.apache.jena.query.QuerySolution;

/**
 * Solution for a query to get regex features
 * @author Cristiano Longo
 */
class RegexFeatureQuerySolution {
    static final String FEATURE_IRI_VAR = "feature";
    static final String REGEX_VAR = "regex";
    static final String REPLACEMENT_VAR = "replacement";

    static final String QUERY = "SELECT ?"+FEATURE_IRI_VAR+" ?"+REGEX_VAR+" ?"+REPLACEMENT_VAR+" WHERE{"+
            "?"+FEATURE_IRI_VAR+" <"+ LinguisticPhenomena.REGEX_ANN_PROPERTY+"> ?"+REGEX_VAR+" ."+
            "?"+FEATURE_IRI_VAR+" <"+ LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY+"> ?"+REPLACEMENT_VAR+"} ORDER BY ?"+FEATURE_IRI_VAR;

    final String featureIRI;
    final String regex;
    final String replacement;

    RegexFeatureQuerySolution(final QuerySolution row){
        featureIRI = row.getResource(FEATURE_IRI_VAR).getURI();
        regex = row.getLiteral(REGEX_VAR).getString();
        replacement = row.getLiteral(REPLACEMENT_VAR).toString();
    }

    /**
     * Create a novel feature with the specified regex and the only replacement in this solution
     * @return a novel feature based on this query solution
     */
    RegexLinguisticPhenomenon buildFeature(){
        return new RegexLinguisticPhenomenon(featureIRI, regex, replacement);
    }
}
