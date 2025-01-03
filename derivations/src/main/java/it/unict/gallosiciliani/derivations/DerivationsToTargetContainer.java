package it.unict.gallosiciliani.derivations;

import java.util.Collection;
import java.util.function.Predicate;

public interface DerivationsToTargetContainer extends Predicate<DerivationPathNode> {
    String getTarget();
    Collection<DerivationPathNode> getDerivation();
}
