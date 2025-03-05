package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.derivations.strategy.CompoundDerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.DerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategySelectorFactory;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.List;

public class DerivationBuilderWithStrategyFactory implements DerivationBuilderFactory{
    private final TargetedDerivationStrategySelectorFactory selectorFactory;

    public DerivationBuilderWithStrategyFactory(final TargetedDerivationStrategySelectorFactory selectorFactory){
        this.selectorFactory=selectorFactory;
    }

    @Override
    public DerivationBuilder build(List<? extends LinguisticPhenomenon> phenomena, List<NearestShortestDerivation> targets) {
        return new DerivationBuilderWithStrategy(phenomena, new CompoundDerivationStrategyFactory(targets, selectorFactory));
    }
}
