package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.List;

/**
 * Raw data concerning a derivation, extracted from the knowledge base.
 * The lexical entry MUST have an etymology. It may have an empty derivation chain.
 */
public interface DerivationRawData {

    /**
     * The complete set of available linguistic phenomena used while extracting this {@link DerivationRawData} instance
     * @return a provider of linguistic phenomena
     */
    LinguisticPhenomenaProvider getEligibleLinguisticPhenomena();

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
    List<LinguisticPhenomenonOccurrence> getDerivationChain();
}
