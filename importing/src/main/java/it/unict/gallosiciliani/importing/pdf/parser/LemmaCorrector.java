package it.unict.gallosiciliani.importing.pdf.parser;

/**
 * Base for classes which update lemmas in some way
 */
abstract class LemmaCorrector implements ParsingDataConsumer{
    private final ParsingDataConsumer consumer;

    /**
     *
     * @param consumer which will receive the updated lemmas
     */
    LemmaCorrector(final ParsingDataConsumer consumer){
        this.consumer=consumer;
    }

    abstract String handleLemma(final String lemma);

    @Override
    public final void lemma(final String lemma) {
        consumer.lemma(handleLemma(lemma));
    }

    @Override
    public final void pos(String pos) {
        consumer.pos(pos);
    }

    @Override
    public final void conjunction() {
        consumer.conjunction();
    }
}
