package it.unict.gallosiciliani.derivations.strategy;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Select strategies the nearest to the target strategies
 */
public class NearestStrategySelector implements TargetedDerivationStrategySelector {

    public static TargetedDerivationStrategySelectorFactory FACTORY=NearestStrategySelector::new;

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
