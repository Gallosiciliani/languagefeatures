package it.unict.gallosiciliani.pdfimporter.parser;

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
class AccentedWithDiacriticsCorrector extends LemmaCorrector {
    private final Set<String> preserveAccentedO= Set.of("póverö", "cónciö");

    AccentedWithDiacriticsCorrector(final ParsingDataConsumer consumer){
        super(consumer);
    }

    @Override
    String handleLemma(String lemma) {
        if (preserveAccentedO.contains(lemma))
            return lemma;
        return lemma.replaceAll("ó","ö̀").replaceAll("é", "ë̀");
    }
}
