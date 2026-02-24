package it.unict.gallosiciliani.gs.derivationsextractor.custommissed;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.List;
import java.util.SortedSet;

/**
 * Raw data concerning a derivation, extracted from the knowledge base.
 * The lexical entry MUST have an etymology. It may have an empty derivation chain.
 */
public interface DerivationRawDataCM {
    /**
     * get the entry this derivation refers to.
     * @return the entry this derivation refers to
     */
    LexicalEntry getEntry();

    /**
     * The derivation from the etymon
     *
     * @return a list of derivations steps
     */
    DerivationPathNode getDerivationChain();

    /**
     *
     * @return missed phenomena
     */
    SortedSet<LinguisticPhenomenon> getMissed();
}
