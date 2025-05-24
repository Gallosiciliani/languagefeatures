package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationBuilderFactory;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Select strategies which are not farther than the current one.
 * Notice that here the current one is always returned by the branch method.
 */
public class NotFartherStrategySelector implements TargetedDerivationStrategySelector {

    public static TargetedDerivationStrategySelectorFactory FACTORY= NotFartherStrategySelector::new;
    public static DerivationBuilderFactory DERIVATION_BUILDER_FACTORY=new DerivationBuilderWithStrategyFactory(FACTORY);

    @Getter
    private final Collection<DerivationStrategy> selectedStrategies;
    private final int distance;

    public NotFartherStrategySelector(final TargetedDerivationStrategy initialContent){
        selectedStrategies=new LinkedList<>();
        selectedStrategies.add(initialContent);
        distance=initialContent.getDistance();
    }

    @Override
    public void accept(final TargetedDerivationStrategy s){
        if (s.getDistance()>distance)
            return;
        selectedStrategies.add(s);
    }
}
