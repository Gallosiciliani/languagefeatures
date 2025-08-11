package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.Set;
import java.util.SortedSet;

/**
 * Information concerning the phenomena in a derivation
 *
 * @author Cristiano Longo
 */
public interface DerivationPhenomena {
    /**
     * all the phenomena which applies to the derivation etymon, considering a fixed finite set of
     * eligible phenomena.
     *
     * @return all the phenomena which applies to the derivation etymon
     */
    Set<LinguisticPhenomenon> getSuitablePhenomena();

    /**
     * All the suitable phenomenon which did not occur in the derivation.
     * @return suitable phenomena not occurring in the derivation
     */
    SortedSet<LinguisticPhenomenon> getMissedPhenomena();
}
