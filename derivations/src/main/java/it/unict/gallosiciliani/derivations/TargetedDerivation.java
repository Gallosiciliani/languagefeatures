package it.unict.gallosiciliani.derivations;

/**
 * A derivation associated to a target
 */
public interface TargetedDerivation {
    DerivationPathNode getDerivation();
    int getDistance();
}
