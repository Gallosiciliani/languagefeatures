package it.unict.gallosiciliani.derivations;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Container for {@link TargetedDerivation}
 * @author Cristiano Longo
 */
public interface DerivationsToTargetContainer extends Consumer<TargetedDerivation> {
    String getTarget();
    Collection<DerivationPathNode> getDerivation();
}
