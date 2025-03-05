package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.List;

/**
 * Factory for {@link DerivationBuilder} objects
 * @author Cristiano Longo
 */
public interface DerivationBuilderFactory {
    DerivationBuilder build(final List<? extends LinguisticPhenomenon> phenomena, final List<NearestShortestDerivation> targets);
}
