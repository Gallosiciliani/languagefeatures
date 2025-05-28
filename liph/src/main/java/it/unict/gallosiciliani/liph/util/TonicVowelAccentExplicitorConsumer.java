package it.unict.gallosiciliani.liph.util;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.function.Consumer;

/**
 * Add grave accent to tonic vowels in the canonical form o lexical entries.
 * @author Cristiano Longo
 */
public class TonicVowelAccentExplicitorConsumer implements Consumer<LexicalEntry> {
    private final TonicVowelAccentExplicitor accentExplicitor=new TonicVowelAccentExplicitor();
    private final Consumer<LexicalEntry> consumer;

    public TonicVowelAccentExplicitorConsumer(final Consumer<LexicalEntry> consumer){
        this.consumer=consumer;
    }
    @Override
    public void accept(final LexicalEntry lexicalEntry) {
        final Form f=lexicalEntry.getCanonicalForm();
        if (f!=null && f.getWrittenRep()!=null)
            for(final String lang: f.getWrittenRep().getLanguages())
                f.getWrittenRep().set(lang, accentExplicitor.addGraveAccent(f.getWrittenRep().get(lang)));
        consumer.accept(lexicalEntry);
    }
}
