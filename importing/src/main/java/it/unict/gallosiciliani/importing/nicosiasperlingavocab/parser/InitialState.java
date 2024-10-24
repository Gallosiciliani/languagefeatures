package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

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
    ParsingState withLemmaFont(final String c) {
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

    @Override
    ParsingState blank(final String c) {
        return this;
    }
}
