package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

/**
 * Provides IRI for generated items
 * @author Cristiano Longo
 */
public interface IRIProvider {

    /**
     * @return the IRI of the lexicon
     */
    String getLexiconIRI();

    interface LexicalEntryIRIProvider{
        String getLexicalEntryIRI();
        String getCanonicalFormIRI();
    }

    LexicalEntryIRIProvider getLexicalEntryIRIs();
}
