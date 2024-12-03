package it.unict.gallosiciliani.importing.pdf.parser;

/**
 * The parser just encountered a conjunctor after a lemma
 */
public class LemmaConjunctorState extends ParsingState{
    protected LemmaConjunctorState(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    ParsingState parse(final String c, final ParsedCharType t) {
        if (c.isBlank())
            return this;
        return switch (t) {
            case WITH_LEMMA_FONT -> {
                consumer.conjunction();
                yield new LemmaState(consumer, c);
            }
            case WITH_POS_FONT -> new POSState(consumer, c);
            default -> new DiscardState(consumer);
        };
    }
}
