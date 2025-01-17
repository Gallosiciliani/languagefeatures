package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationsToTargetContainer;
import it.unict.gallosiciliani.derivations.TargetedDerivation;
import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;

/**
 * A derivation strategy guided by the distance to a fixed target
 *
 * @author Cristiano Longo
 */
public class TargetedDerivationStrategy implements DerivationStrategy, TargetedDerivation {

    @Getter
    private final DerivationPathNode derivation;
    private final TargetedDerivationStrategy prev;
    @Getter
    private final int distance;
    private final DerivationsToTargetContainer consumer;
    private final TargetedDerivationStrategySelectorFactory selectorFactory;
    private boolean consumed;

    // base case
    public TargetedDerivationStrategy(final DerivationPathNode initialDerivation,
                               final DerivationsToTargetContainer consumer,
                               final TargetedDerivationStrategySelectorFactory selectorFactory){
        derivation=initialDerivation;
        this.consumer=consumer;
        this.selectorFactory=selectorFactory;
        distance=LevenshteinDistance.getDefaultInstance().apply(derivation.get(), consumer.getTarget());
        if (distance==0) consumer.accept(this);
        prev=null;
    }

    private TargetedDerivationStrategy(final DerivationPathNode extendedDerivation, final TargetedDerivationStrategy prev){
        derivation=extendedDerivation;
        consumer=prev.consumer;
        selectorFactory=prev.selectorFactory;
        distance = LevenshteinDistance.getDefaultInstance().apply(extendedDerivation.get(), consumer.getTarget());
        if (distance==0) consumer.accept(this);
        this.prev=prev;
    }

    @Override
    public Collection<DerivationStrategy> branch(final Collection<DerivationPathNode> branches) {
        final TargetedDerivationStrategySelector c=selectorFactory.build(this);
        branches.stream()
                .map((b)-> new TargetedDerivationStrategy(b, this))
                .filter((s)->s.distance>0)
                .forEach(c);
        return c.getSelectedStrategies();
    }

    @Override
    public final void end() {
        if (consumed)
            return;
        consumed=true;
        if (prev==null || distance<prev.distance)
            consumer.accept(this);
        else prev.end();
    }
}
