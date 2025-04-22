package it.unict.gallosiciliani.importing.iri;

/**
 * Provide IRIs for an etymology, using natural numbers
 */
class SequentialEtymologyIRIProvider implements EtymologyIRIProvider {

    private final String iri;
    private int n;
    private int occurrencesNum;

    /**
     *
     * @param iri IRI of the etymology individual
     */
    SequentialEtymologyIRIProvider(final String iri){
        this.iri=iri;
    }

    @Override
    public String getEtymolgyIRI() {
        return iri;
    }

    @Override
    public String getEtyLinkIRI() {
        return iri+"-fstLink";
    }

    @Override
    public String getEtySourceIRI() {
        return iri+"-source"+(n++);
    }

    @Override
    public PhenomenonOccurrenceIRIProvider getLinguisticPhenomenaOccurrencesIRIs() {
        return new SequentialPhenomenonOccurrenceIRIProvider(iri+"-occurrence"+(occurrencesNum++));
    }
}
