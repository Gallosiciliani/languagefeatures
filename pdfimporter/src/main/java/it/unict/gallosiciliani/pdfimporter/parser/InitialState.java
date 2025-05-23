package it.unict.gallosiciliani.pdfimporter.parser;

/**
 * Initial state for {@link Parser}
 *
 * @author Cristiano Longo
 */
class InitialState extends ParsingState {
     InitialState(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    ParsingState parse(final String c, final ParsedCharType t) {
        return switch (t) {
            case WITH_POS_FONT -> throw new UnexpectedParsedCharException(c, t, this);
            case WITH_LEMMA_FONT -> new LemmaState(consumer, c);
            default -> new DiscardState(consumer);
        };
    }
}
