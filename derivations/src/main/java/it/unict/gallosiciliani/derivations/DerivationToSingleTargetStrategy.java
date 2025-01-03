package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

class DerivationToSingleTargetStrategy implements DerivationStrategy {
    @Getter
    private final DerivationPathNode derivation;
    private final DerivationToSingleTargetStrategy prev;
    private final int distance;
    private final DerivationsToTargetContainer consumer;

    // base case
    DerivationToSingleTargetStrategy(final DerivationPathNode initialDetivation, final DerivationsToTargetContainer consumer){
        derivation=initialDetivation;
        distance=LevenshteinDistance.getDefaultInstance().apply(derivation.get(), consumer.getTarget());
        this.consumer=consumer;
        prev=null;
    }

    private DerivationToSingleTargetStrategy(final DerivationPathNode extendedDerivation, final DerivationToSingleTargetStrategy prev){
        derivation=extendedDerivation;
        consumer=prev.consumer;
        distance = LevenshteinDistance.getDefaultInstance().apply(extendedDerivation.get(), consumer.getTarget());
        this.prev=prev;
    }

    @Override
    public boolean goOn() {
        return distance<=prev.distance;
    }

    @Override
    public DerivationStrategy extend(final DerivationPathNode extendedPath) {
        return new DerivationToSingleTargetStrategy(extendedPath, this);
    }

    @Override
    public void end() {
        if (prev==null || distance<prev.distance)
            consumer.test(derivation);
        else prev.end();
    }
}
