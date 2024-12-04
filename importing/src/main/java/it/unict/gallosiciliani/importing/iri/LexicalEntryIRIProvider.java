package it.unict.gallosiciliani.importing.iri;

/**
 * Expected IRIs for a lexical entries
 */
public interface LexicalEntryIRIProvider {
    /**
     * IRI of the whole entry
     *
     * @return IRI of the entry
     */
    String getLexicalEntryIRI();

    /**
     * IRI of the entry canonical form
     *
     * @return IRI of canonical form
     */
    String getCanonicalFormIRI();

    /**
     * Provide IRIs for a novel etymology of this entry. Calling this method multiple times
     * produces different IRIs.
     *
     * @return IRIs for a novel etymology
     */
    EtymologyIRIProvider getEtymologyIRIs();
}
