package it.unict.gallosiciliani.importing.api;

import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;

import java.util.function.Consumer;

/**
 * Build an instance of {@link LexiconConverter} which converts files with specific file format
 *
 * @author Cristiano Longo
 */
public interface LexiconConverterFactory {
    /**
     * Build the converter
     *
     * @param consumer where generated entries will be sent
     * @param iris provider to get the entry IRIs
     * @param posIndividualProvider Part Of Speech individuals
     * @return the converter
     */
    LexiconConverter build(final Consumer<LexicalEntry> consumer,
                           final IRIProvider iris,
                           final POSIndividualProvider posIndividualProvider);
}
