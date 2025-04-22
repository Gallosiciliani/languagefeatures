package it.unict.gallosiciliani.importing.iri;

/**
 * Provide IRIs for {@link it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence}
 * individuals.
 */
public interface PhenomenonOccurrenceIRIProvider {
    /**
     * IRI of the novel occurrence
     *
     * @return IRI of the linguistic phenomenon occurrence
     */
    String getOccurrenceIRI();

    /**
     * IRI for the occurrence target
     * @return available iri for occurrence target
     */
    String getIntermediateFormIRI();
}
