package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.Collection;

/**
 * Strategy to decide if a derivation should proceed or must stop.
 * @author Cristiano Longo
 */
public interface DerivationStrategy {

    /**
     *
     * @return the derivation this strategy refers to
     */
    DerivationPathNode getDerivation();

    /**
     * Create strategies to handle the branching of a derivation due to the application of a linguistic phenomenon.
     *
     * @param branches derivations obtained by applying a {@link LinguisticPhenomenon} to one
     *                 characterizing this strategy.
     * @return strategies corresponding to these branches, notice that it may contain a strategy corresponding to the current derivation
     */
    Collection<DerivationStrategy> branch(final Collection<DerivationPathNode> branches);

    /**
     * Consume this derivation (or one of its ancestors) when the derivation stops
     */
    void end();
}
