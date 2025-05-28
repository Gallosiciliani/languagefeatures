package it.unict.gallosiciliani.importing.api;

import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.util.TonicVowelAccentExplicitorConsumer;

import java.util.function.Consumer;

/**
 * Build a lexicon converter which adds accents to tonic vowels (see {@link it.unict.gallosiciliani.liph.util.TonicVowelAccentExplicitor})
 * @author Cristiano Longo
 */
public class LexiconConverterFactoryWithAccentExplicitor implements LexiconConverterFactory{

    private final LexiconConverterFactory delegate;

    public LexiconConverterFactoryWithAccentExplicitor(final LexiconConverterFactory delegate){
        this.delegate=delegate;
    }
    @Override
    public LexiconConverter build(final Consumer<LexicalEntry> consumer,
                                  final IRIProvider iris,
                                  final POSIndividualProvider posIndividualProvider,
                                  final String lemmaLang) {
        return delegate.build(new TonicVowelAccentExplicitorConsumer(consumer), iris, posIndividualProvider, lemmaLang);
    }
}
