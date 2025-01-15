package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.*;

import java.util.Collection;

/**
 * Create a {@link CompoundDerivationStrategy} which will notify a predefined set of consumers
 * @author Cristiano Longo
 */
public class CompoundDerivationStrategyFactory implements DerivationStrategyFactory {
    private final Collection<? extends DerivationsToTargetContainer> consumers;
    private final TargetedDerivationStrategySelectorFactory selectorFactory;

    public CompoundDerivationStrategyFactory(final Collection<? extends DerivationsToTargetContainer> consumers,
                                             final TargetedDerivationStrategySelectorFactory selectorFactory){
        this.consumers=consumers;
        this.selectorFactory=selectorFactory;
    }

    @Override
    public DerivationStrategy build(DerivationPathNode initialDerivation) {
        final Collection<DerivationStrategy> children=consumers.stream()
                .<DerivationStrategy>map((c)->new TargetedDerivationStrategy(initialDerivation, c, selectorFactory))
                .toList();
        return new CompoundDerivationStrategy(children);
    }
}
