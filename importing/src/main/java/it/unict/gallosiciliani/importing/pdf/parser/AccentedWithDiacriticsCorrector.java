package it.unict.gallosiciliani.importing.pdf.parser;

import java.util.Set;

/**
 * The current version of PDFBox, when run on the PDF containing the vocabulary,
 * parse ö̀ as ó. However, there are just two lemmas between nouns and verbs which
 * originally contains ó:
 * Page 781: póverö
 * Page 339: cónciö
 * Analogously, ë̀ is recognized as é.
 * This corrector replace ó with ö̀ and é with ë̀ in all lemmas except these two.
 */
class AccentedWithDiacriticsCorrector implements ParsingDataConsumer {

    private final ParsingDataConsumer consumer;
    private final Set<String> preserveAccentedO= Set.of("póverö", "cónciö");

    AccentedWithDiacriticsCorrector(final ParsingDataConsumer consumer){
        this.consumer=consumer;
    }

    @Override
    public void lemma(final String lemma) {
        if (preserveAccentedO.contains(lemma))
            consumer.lemma(lemma);
        else
            consumer.lemma(lemma.replaceAll("ó","ö̀").replaceAll("é", "ë̀"));
    }

    @Override
    public void pos(String pos) {
        consumer.pos(pos);
    }

    @Override
    public void conjunction() {
        consumer.conjunction();
    }
}
