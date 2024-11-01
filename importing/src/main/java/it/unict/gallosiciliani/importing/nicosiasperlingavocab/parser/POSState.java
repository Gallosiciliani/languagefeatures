package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

/**
 * The parser is getting the lemma type (PartOfSpeech)
 * @author Cristiano Longo
 */
public class POSState extends ParsingState {
    private final StringBuffer pos;

    /**
     * @param consumer the extraction results consumer
     * @param c        the first character of the type
     */
    POSState(final ParsingDataConsumer consumer, final String c){
        super(consumer);
        pos=new StringBuffer(c);
    }

    @Override
    ParsingState parse(final String c, final ParsedCharType t) {
        if (c.isBlank() || t==ParsedCharType.WITH_POS_FONT){
            pos.append(c);
            return this;
        }
        final String posString=pos.toString().trim();
        if (!posString.isEmpty())
            consumer.pos(pos.toString().trim());
        return t==ParsedCharType.WITH_LEMMA_FONT ? new LemmaState(consumer, c) : new DiscardState(consumer);
    }

    @Override
    public String toString(){
        return "POSState("+pos +")";
    }
}
