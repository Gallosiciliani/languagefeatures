package it.unict.gallosiciliani.importing.iri;

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
}
