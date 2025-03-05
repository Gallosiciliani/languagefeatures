package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.DerivationBuilderFactory;
import it.unict.gallosiciliani.derivations.DerivationBuilderWithStrategyFactory;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Select strategies the nearest to the target strategies
 */
public class NearestStrategySelector implements TargetedDerivationStrategySelector {

    public static TargetedDerivationStrategySelectorFactory FACTORY=NearestStrategySelector::new;
    public static DerivationBuilderFactory DERIVATION_BUILDER_FACTORY=new DerivationBuilderWithStrategyFactory(FACTORY);

    @Getter
    private final Collection<DerivationStrategy> selectedStrategies;
    private int distance;

    public NearestStrategySelector(final TargetedDerivationStrategy initialContent){
        selectedStrategies=new LinkedList<>();
        selectedStrategies.add(initialContent);
        distance=initialContent.getDistance();
    }

    @Override
    public void accept(final TargetedDerivationStrategy s){
        if (s.getDistance()>distance)
            return;
        if (s.getDistance()<distance) {
            distance=s.getDistance();
            selectedStrategies.clear();
        }
        selectedStrategies.add(s);
    }
}
