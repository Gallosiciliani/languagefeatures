package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

/**
 * Generate IRI sequentially using natural numbers
 */
public class SequentialNatIRIProvider implements IRIProvider{
    private final String namespace;
    private int n = 0;

    public SequentialNatIRIProvider(final String namespace){
        this.namespace=namespace;
    }

    @Override
    public String getLexiconIRI() {
        return namespace+"lexicon";
    }

    @Override
    public LexicalEntryIRIProvider getLexicalEntryIRIs() {
        final String iri=namespace+"entry"+(n++);
        final String canonicalFormIri=iri+"-canonicalForm";
        return new LexicalEntryIRIProvider() {
            @Override
            public String getLexicalEntryIRI() {
                return iri;
            }

            @Override
            public String getCanonicalFormIRI() {
                return canonicalFormIri;
            }
        };
    }
}
