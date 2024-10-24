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
    ParsingState blank(final String c) {
        return withLemmaFont(c);
    }

    @Override
    ParsingState withLemmaFont(String c) {
        lemma.append(c);
        return this;
    }

    @Override
    ParsingState withPOSFont(final String c) {
        consumer.lemma(lemma.toString().trim());
        return new POSState(consumer, c);
    }

    @Override
    ParsingState withOtherFont(final String c) {
        consumer.lemma(lemma.toString().trim().replaceAll("-",""));
        return isConjunction(c) ? new LemmaConjunctorState(consumer) : new DiscardState(consumer);
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
