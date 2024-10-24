package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

/**
 * Parsing state of the extractor
 *
 * @author Cristiano Longo
 */
abstract class ParsingState {

    protected final ParsingDataConsumer consumer;

    protected ParsingState(final ParsingDataConsumer consumer){
        this.consumer=consumer;
    }

    /**
     * The current character font is the one expected for lemmas
     * @param c the current charter
     * @return destination state
     */
    abstract ParsingState withLemmaFont(final String c);

    /**
     * The current character font is the one expected for Part Of Speech
     * @param c the current charter
     * @return destination state
     */
    abstract ParsingState withPOSFont(final String c);

    /**
     * The current character is not recognized neither as lemma nor as POS
     * @param c the current charter
     * @return destination state
     */
    abstract ParsingState withOtherFont(final String c);

    /**
     * Encountered a blank character
     * @param c the blank character
     * @return destination state
     */
    abstract ParsingState blank(final String c);
}
