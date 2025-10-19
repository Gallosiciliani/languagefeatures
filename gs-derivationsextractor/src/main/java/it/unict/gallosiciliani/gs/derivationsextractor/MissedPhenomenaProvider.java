package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.SortedSet;

/**
 * @author Cristiano Longo
 */
public interface MissedPhenomenaProvider {

    SortedSet<LinguisticPhenomenon> getMissedPhenomena(final String lemma);
}
