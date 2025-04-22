package it.unict.gallosiciliani.importing.iri;

/**
 * Provide IRIs for linguistic phenomena occurrences in a etymology
 */
class SequentialPhenomenonOccurrenceIRIProvider implements PhenomenonOccurrenceIRIProvider {

    private final String iri;

    /**
     *
     * @param iri IRI of the etymology individual
     */
    SequentialPhenomenonOccurrenceIRIProvider(final String iri){
        this.iri=iri;
    }

    @Override
    public String getOccurrenceIRI() {
        return iri;
    }

    @Override
    public String getIntermediateFormIRI() {
        return iri+"-target";
    }
}
