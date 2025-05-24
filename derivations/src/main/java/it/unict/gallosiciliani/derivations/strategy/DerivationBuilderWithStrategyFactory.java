package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.DerivationBuilderFactory;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.List;

public class DerivationBuilderWithStrategyFactory implements DerivationBuilderFactory {
    private final TargetedDerivationStrategySelectorFactory selectorFactory;

    public DerivationBuilderWithStrategyFactory(final TargetedDerivationStrategySelectorFactory selectorFactory){
        this.selectorFactory=selectorFactory;
    }

    @Override
    public DerivationBuilder build(List<? extends LinguisticPhenomenon> phenomena, List<NearestShortestDerivation> targets) {
        return new DerivationBuilderWithStrategy(phenomena, new CompoundDerivationStrategyFactory(targets, selectorFactory));
    }
}
