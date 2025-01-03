package it.unict.gallosiciliani.derivations;

import lombok.Getter;

import java.util.Collection;

class DerivationsToMultipleTargetsStrategy implements DerivationStrategy{

    @Getter
    private final DerivationPathNode derivation;
    private final Collection<? extends DerivationStrategy> children;

    DerivationsToMultipleTargetsStrategy(final DerivationPathNode initialDetivation, final Collection<? extends DerivationsToTargetContainer> consumers) {
        derivation=initialDetivation;
        children = consumers.stream().map((x) -> new DerivationToSingleTargetStrategy(initialDetivation, x)).toList();
    }

    private DerivationsToMultipleTargetsStrategy(final DerivationsToMultipleTargetsStrategy prev, final Collection<? extends DerivationStrategy> children) {
        derivation=prev.derivation;
        this.children=children;
    }

    @Override
    public boolean goOn() {
        return !children.isEmpty();
    }

    @Override
    public DerivationStrategy extend(final DerivationPathNode extendedPath) {
        return new DerivationsToMultipleTargetsStrategy(this, children.stream().map((x)->x.extend(extendedPath)).filter(DerivationStrategy::goOn).toList());
    }

    @Override
    public void end() {
        children.forEach(DerivationStrategy::end);
    }
}