package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;


/**
 * Parsing state of the extractorhttps://ceur-ws.org/Vol-3809/paper2.pdf
 *
 * @author Cristiano Longo
 */
abstract class ParsingState {

    protected final ParsingDataConsumer consumer;

    protected ParsingState(final ParsingDataConsumer consumer){
        this.consumer=consumer;
    }

    /**
     * Parse the next char c, which has been recognized as of type t
     * @param c encountered character
     * @param t recognized type for the specified character
     * @return destination state
     */
    abstract ParsingState parse(final String c, final ParsedCharType t);
}
