package it.unict.gallosiciliani.pdfimporter.generator;

import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.SequentialIRIProvider;
import it.unict.gallosiciliani.pdfimporter.parser.ParsingDataConsumer;
import it.unict.gallosiciliani.importing.partofspeech.POS;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.partofspeech.UnexpectedPOSStringException;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Convert lemmas and POS obtained by parsing the PDF into corresponding lexical entires.
 *
 * @author Cristiano Longo
 */
@Slf4j
public class LexicalEntriesGenerator implements ParsingDataConsumer{

    private GeneratorState currentState;

    public LexicalEntriesGenerator(final Consumer<LexicalEntry> consumer, final IRIProvider iris,
                                   final POSIndividualProvider posIndividualProvider){
        final WellKnownDuplicatesHandler duplicatesHandler=new WellKnownDuplicatesHandler();
        currentState=new GeneratorInitialState(new GeneratorStateParams() {
            @Override
            public Consumer<LexicalEntry> getConsumer() {
                return consumer;
            }

            @Override
            public IRIProvider getIRIProvider() {
                return iris;
            }

            @Override
            public POSIndividualProvider getPOSIndividualProvider() {
                return posIndividualProvider;
            }

            @Override
            public WellKnownDuplicatesHandler getDuplicatesHandler() {
                return duplicatesHandler;
            }
        });
    }

    public LexicalEntriesGenerator(final Consumer<LexicalEntry> consumer, final String namespace,
                                   final POSIndividualProvider posIndividualProvider){
        this(consumer, new SequentialIRIProvider(namespace), posIndividualProvider);
    }

    @Override
    public void lemma(final String lemma) {
        currentState=currentState.lemma(lemma);
        log.debug("state transition lemma \"{}\" -> {}", lemma, currentState);
    }

    @Override
    public void pos(final String posStr) {
        switch (getPOS(posStr)){
            case NOUN: currentState=currentState.noun();
            break;
            case VERB: currentState=currentState.verb();
            break;
            default: currentState=currentState.ignoredPOS();
        }
        log.debug("state transition pos \"{}\" -> {}", posStr, currentState);
    }

    private POS getPOS(final String posString){
        try {
            return POS.get(posString);
        } catch (final UnexpectedPOSStringException e) {
            log.warn("Unexpected POS", e);
            return e.getSuggestedPOS();
        }
    }

    @Override
    public void conjunction() {
        currentState=currentState.conjunction();
        log.debug("state transition conjunction -> {}", currentState);
    }
}
