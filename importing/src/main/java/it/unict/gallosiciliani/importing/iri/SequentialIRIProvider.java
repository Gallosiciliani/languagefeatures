package it.unict.gallosiciliani.importing.iri;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

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

    @Override
    public LexicalEntryIRIProvider getLexicalEntryIRIs(final LexicalEntry entry) {
        return new SequentialLexicalEntryIRIProvider(entry.getId());
    }
}
