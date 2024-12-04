package it.unict.gallosiciliani.importing.iri;

/**
 * Generate IRIs from a base IRI and provide IRIs for etymologies using sequential natural numbers
 */
class SequentialLexicalEntryIRIProvider implements LexicalEntryIRIProvider{

    private final String iri;
    private int n=0;

    /**
     * @param entryIRI main IRI of the entry
     */
    SequentialLexicalEntryIRIProvider(final String entryIRI){
        this.iri=entryIRI;
    }

    @Override
    public String getLexicalEntryIRI() {
        return iri;
    }

    @Override
    public String getCanonicalFormIRI() {
        return iri+"-canonicalForm";
    }

    @Override
    public EtymologyIRIProvider getEtymologyIRIs() {
        return new SequentialEtymologyIRIProvider(iri+"-etymology"+(n++));
    }
}
