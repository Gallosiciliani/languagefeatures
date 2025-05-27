package it.unict.gallosiciliani.pdfimporter.parser;

import it.unict.gallosiciliani.liph.util.TonicVowelAccentExplicitor;

/**
 * Add grave accent on tonic vowel (if no other accent is present) of parsed lemmas,
 * if the lemma is not polyrematic.
 * @author Cristiano Longo
 */
public class TonicVowelAccentCorrector extends LemmaCorrector{

    private final TonicVowelAccentExplicitor accentExplicitor=new TonicVowelAccentExplicitor();

    TonicVowelAccentCorrector(final ParsingDataConsumer consumer){
        super(consumer);
    }

    @Override
    String handleLemma(final String lemma) {
        if (lemma.contains(" "))
            return lemma;
        return accentExplicitor.addGraveAccent(lemma);
    }
}
