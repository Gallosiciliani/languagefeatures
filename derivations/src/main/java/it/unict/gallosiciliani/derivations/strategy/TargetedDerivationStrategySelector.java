package it.unict.gallosiciliani.derivations.strategy;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Selection criterion for branches of a {@link TargetedDerivationStrategy}
 * @author Cristiano Longo
 */
public interface TargetedDerivationStrategySelector extends Consumer<TargetedDerivationStrategy> {
    /**
     * Get the selected strategies among all the accepted ones.
     * @return selected strategies
     */
    Collection<DerivationStrategy> getSelectedStrategies();
}
