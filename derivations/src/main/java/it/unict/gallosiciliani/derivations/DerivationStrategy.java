package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

/**
 * Strategy to decide if a derivation should proceed or must stop. Also, handle death derivation branches.
 * @author Cristiano Longo
 */
public interface DerivationStrategy {

    /**
     *
     * @return the derivation this strategy refers to
     */
    DerivationPathNode getDerivation();

    /**
     *
     * @return true if this derivation should proceed, false it is worst than its predecessor
     */
    boolean goOn();

    /**
     * Create a strategy for a derivation extending the one corresponding to this strategy, but extended with just one node
     *
     * @param extendedPath the derivation corresponding to this strategy, but extended by one node
     * @return a derivation strategy corresponding to the extended derivation
     */
    DerivationStrategy extend(final DerivationPathNode extendedPath);

    /**
     * Consume this derivation (or one of its ancestors) when the derivation stops
     */
    void end();
}
