package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;

import java.util.function.Consumer;

/**
 * Construction parameters for the generator
 */
interface GeneratorStateParams{
    /**
     * Consumer which will receive entries
     */
    Consumer<LexicalEntry> getConsumer();

    /**
     * Provide IRIs for generated entries
     * @return IRI provider
     */
    IRIProvider getIRIProvider();

    /**
     * Provide individuals which have to be used for Part Of Speech in Lexical Entries
     * @return POS individuals provider
     */
    POSIndividualProvider getPOSIndividualProvider();

    /**
     * Detect and handle duplicates
     * @return a duplicates handler
     */
    WellKnownDuplicatesHandler getDuplicatesHandler();
}
