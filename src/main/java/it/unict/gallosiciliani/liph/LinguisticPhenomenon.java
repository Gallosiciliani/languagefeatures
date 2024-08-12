package it.unict.gallosiciliani.liph;

import java.util.Set;

/**
 * Transform a source string into one or more derived ones.
 * This represents the operational description of a language feature.
 * @author Cristiano Longo
 */
public interface LinguisticPhenomenon {
    /**
     * The IRI which uniquely identifies the feature
     * @return feature IRI
     */
    String getIRI();
    /**
     *
     * @param src source string
     * @return all the strings which are obtained with applying the feature to the source string
     */
    Set<String> apply(String src);
}
