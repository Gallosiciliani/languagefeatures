package it.unict.gallosiciliani.importing.iri;

/**
 * Generate IRI sequentially using natural numbers
 */
public class SequentialIRIProvider implements IRIProvider{
    private final String namespace;
    private int n = 0;

    public SequentialIRIProvider(final String namespace){
        this.namespace=namespace;
    }

    @Override
    public LexicalEntryIRIProvider getLexicalEntryIRIs() {
        return new SequentialLexicalEntryIRIProvider(namespace+"entry"+(n++));
    }
}
