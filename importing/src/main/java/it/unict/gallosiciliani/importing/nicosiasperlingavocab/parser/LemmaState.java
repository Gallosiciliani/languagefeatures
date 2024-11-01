package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

/**
 * The parser is searching for a candidate lemma string.
 * Lemmas are placed at the beginning of a line.
 */
class LemmaState extends ParsingState {
    private final StringBuffer lemma;


    LemmaState(final ParsingDataConsumer consumer, final String c){
        super(consumer);
        lemma =new StringBuffer(c);
    }

    @Override
    ParsingState parse(final String c, final ParsedCharType t) {
        if (c.isBlank() || t==ParsedCharType.WITH_LEMMA_FONT){
            lemma.append(c);
            return this;
        }

        consumer.lemma(lemma.toString().trim().replaceAll("-",""));

        return t==ParsedCharType.WITH_POS_FONT ? new POSState(consumer, c) :
                isConjunction(c) ? new LemmaConjunctorState(consumer) : new DiscardState(consumer);
    }

    /**
     * A conjunction is a char separating different forms of the same lemma
     * @param c the character
     * @return true if the character is a conjunction
     */
    private boolean isConjunction(final String c){
        return "o".equals(c) || "e".equals(c);
    }

    @Override
    public String toString(){
        return "LemmaState("+ lemma +")";
    }
}
