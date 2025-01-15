package it.unict.gallosiciliani.derivations.strategy;

/**
 * Factory for {@link TargetedDerivationStrategy}
 */
public interface TargetedDerivationStrategySelectorFactory {

    TargetedDerivationStrategySelector build(final TargetedDerivationStrategy parentStrategy);
}
