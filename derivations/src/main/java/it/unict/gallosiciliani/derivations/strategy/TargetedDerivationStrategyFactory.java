package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationsToTargetContainer;

/**
 * Factory for {@link TargetedDerivationStrategy}
 * @author Cristiano Longo
 */
public class TargetedDerivationStrategyFactory implements DerivationStrategyFactory{
    private final DerivationsToTargetContainer consumer;
    private final TargetedDerivationStrategySelectorFactory selectorFactory;

    public TargetedDerivationStrategyFactory(final DerivationsToTargetContainer consumer,
                                             final TargetedDerivationStrategySelectorFactory selectorFactory){
        this.consumer=consumer;
        this.selectorFactory=selectorFactory;
    }

    @Override
    public DerivationStrategy build(DerivationPathNode initialDerivation) {
        return new TargetedDerivationStrategy(initialDerivation, consumer, selectorFactory);
    }
}
