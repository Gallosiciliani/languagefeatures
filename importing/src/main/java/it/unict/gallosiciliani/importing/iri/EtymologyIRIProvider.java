package it.unict.gallosiciliani.importing.iri;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;

/**
 * Provide IRIs for an etymology of a specified {@link LexicalEntry}
 */
public interface EtymologyIRIProvider {
    /**
     * IRI of the novel etymology
     *
     * @return IRI of the etymology
     */
    String getEtymolgyIRI();

    /**
     * IRI of the first {@link EtyLink} of this
     * etymology.
     *
     * @return IRI of first EtyLink
     */
    String getEtyLinkIRI();

    /**
     * Create an IRI for a novel etySource for the first link of the etymology
     * @return IRI for a novel etySource
     * TODO consider to remove, the etymon should be already placed in the knowledge base
     */
    String getEtySourceIRI();

    /**
     * IRIs for {@link it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence} individuals
     * involved in this etymology
     * @return IRI for a novel {@link it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence}
     */
    PhenomenonOccurrenceIRIProvider getLinguisticPhenomenaOccurrencesIRIs();

}
