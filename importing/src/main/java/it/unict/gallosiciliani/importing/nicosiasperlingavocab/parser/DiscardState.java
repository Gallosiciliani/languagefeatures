package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

import lombok.extern.slf4j.Slf4j;

/**
 * Parsing a part which is neither Lemma nor POS
 */
@Slf4j
class DiscardState extends ParsingState {
    private final StringBuffer discarded = new StringBuffer();
    DiscardState(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    ParsingState withLemmaFont(String c) {
        return new LemmaState(consumer, c);
    }

    @Override
    ParsingState withPOSFont(final String c) {
        return new POSState(consumer, c);
    }

    @Override
    ParsingState blank(final String c) {
        return withOtherFont(c);
    }

    @Override
    ParsingState withOtherFont(final String c) {
        discarded.append(c);
        return this;
    }

    @Override
    public String toString(){
        return "OtherState("+discarded+")";
    }
}
