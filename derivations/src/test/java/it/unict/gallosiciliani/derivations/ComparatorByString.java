package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.derivations.strategy.DerivationStrategy;

import java.util.Comparator;

/**
 * Compare derivation strategies by the string representation of their derivations
 */
public class ComparatorByString implements Comparator<DerivationStrategy> {
    public static final Comparator<DerivationStrategy> IN=new ComparatorByString();
    public int compare(final DerivationStrategy u, final DerivationStrategy v) {
        return u.getDerivation().toString().compareTo(v.getDerivation().toString());
    }
}
