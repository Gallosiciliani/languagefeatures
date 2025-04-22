package it.unict.gallosiciliani.importing.iri;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

/**
 * Provides IRI for generated items
 * @author Cristiano Longo
 */
public interface IRIProvider {

    /**
     * Build IRIs for a novel lexical entry
     *
     * @return expected IRIs for a lexical entries
     */
    LexicalEntryIRIProvider getLexicalEntryIRIs();

    /**
     * Build an IRI provider for an existing lexical entry
     *
     * @return expected IRIs for a lexical entries
     */
    LexicalEntryIRIProvider getLexicalEntryIRIs(final LexicalEntry entry);
}
