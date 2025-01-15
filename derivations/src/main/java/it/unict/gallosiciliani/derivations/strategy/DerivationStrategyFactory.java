package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationPathNode;

/**
 * Factory for {@link DerivationStrategy} objects.
 * @author Cristiano Longo
 */
public interface DerivationStrategyFactory {

    /**
     * Create a strategy to handle the given derivation
     * @param initialDerivation a derivation, usually with just the root node
     * @return strategy for the initial derivation
     */
    DerivationStrategy build(DerivationPathNode initialDerivation);
}
