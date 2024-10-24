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
    ParsingState blank(final String c) {
        return withPOSFont(c);
    }

    @Override
    ParsingState withLemmaFont(final String c) {
        consumer.pos(pos.toString().trim());
        return new LemmaState(consumer, c);
    }

    @Override
    ParsingState withPOSFont(String c) {
        pos.append(c);
        return this;
    }

    @Override
    ParsingState withOtherFont(final String c) {
        consumer.pos(pos.toString().trim());
        return new DiscardState(consumer);
    }

    @Override
    public String toString(){
        return "POSState("+pos +")";
    }
}
