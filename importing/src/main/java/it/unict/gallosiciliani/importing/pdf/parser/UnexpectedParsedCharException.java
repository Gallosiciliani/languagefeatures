package it.unict.gallosiciliani.importing.pdf.parser;

/**
 * An unexpected character has been encountered during parsing
 */
public class UnexpectedParsedCharException extends RuntimeException{
    /**
     * @param c the unexpected character
     * @param t the unexpected character type (this is relevant)
     * @param s the state of the parser when the character has been encountered
     */
    UnexpectedParsedCharException(final String c, final ParsedCharType t, final ParsingState s){
        super("Unexpected character \""+c+"\" with type "+t+" in state "+s);
    }
}
