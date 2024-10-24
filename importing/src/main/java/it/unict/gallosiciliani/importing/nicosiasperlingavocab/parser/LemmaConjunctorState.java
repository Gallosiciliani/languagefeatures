package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

/**
 * The parser just encountered a conjunctor after a lemma
 */
public class LemmaConjunctorState extends ParsingState{
    protected LemmaConjunctorState(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    ParsingState blank(String c) {
        //one or more blanks allowed after conjunctor
        return this;
    }

    @Override
    ParsingState withLemmaFont(final String c){
        consumer.conjunction();
        return new LemmaState(consumer, c);
    }

    @Override
    ParsingState withPOSFont(final String c) {
        return new POSState(consumer, c);
    }

    @Override
    ParsingState withOtherFont(final String c) {
        return new DiscardState(consumer);
    }
}
