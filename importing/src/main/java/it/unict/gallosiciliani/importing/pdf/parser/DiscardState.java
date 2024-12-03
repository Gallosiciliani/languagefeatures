package it.unict.gallosiciliani.importing.pdf.parser;

import lombok.extern.slf4j.Slf4j;

/**
 * Parsing a part which is neither Lemma nor POS
 */
@Slf4j
class DiscardState extends ParsingState {
    DiscardState(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    ParsingState parse(final String c, final ParsedCharType t) {
        if (c.isBlank()) return this;
        return switch (t) {
            case WITH_LEMMA_FONT -> new LemmaState(consumer, c);
            case WITH_POS_FONT -> new POSState(consumer, c);
            default -> this;
        };
    }

    @Override
    public String toString(){
        return "Discard";
    }
}
