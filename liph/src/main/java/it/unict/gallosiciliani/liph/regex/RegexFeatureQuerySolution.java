package it.unict.gallosiciliani.liph.regex;

/**
 * Solution for a query to get regex features
 * @author Cristiano Longo
 */
public interface RegexFeatureQuerySolution {
    String getFeatureIRI();
    String getFeatureLabel();
    String getFeatureComment();
    String getRegex();
    String getReplacement();
}
