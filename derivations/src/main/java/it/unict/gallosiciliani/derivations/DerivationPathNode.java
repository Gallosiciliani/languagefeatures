package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

/**
 * Node of a derivation path from a candidate etymon to strings obtained applying language features
 * @author Cristiano Longo
 */
public interface DerivationPathNode {
    /**
     * String obtained by applying the transformation to the
     * lexeme in the previous derivation path.
     */
    String get();

    /**
     * Previous node in the derivation path
     * @return node or null if it is the path root
     */
    DerivationPathNode prev();

    /**
     * The language feature transformation which produced this
     * node, if not the root node. Null otherwise.
     *
     * @return LanguageFeatureTransformation or null
     */
    LinguisticPhenomenon getLinguisticPhenomenon();

    /**
     * Number of nodes in this derivation
     * @return derivation length
     */
    int length();
}
