package it.unict.gallosiciliani.pdfimporter.parser;

/**
 * Remove spurious dots at the beginning of lemmas. See pag. 422 döprichè and pag. 687 nsaluchessë
 * @author Cristiano Longo.
 */
public class RemoveSpuriousDotsCorrector extends LemmaCorrector{
    /**
     * @param consumer which will receive the updated lemmas
     */
    RemoveSpuriousDotsCorrector(final ParsingDataConsumer consumer) {
        super(consumer);
    }

    @Override
    String handleLemma(final String lemma) {
        return lemma.startsWith(". ") ? lemma.substring(2) : lemma;
    }
}
