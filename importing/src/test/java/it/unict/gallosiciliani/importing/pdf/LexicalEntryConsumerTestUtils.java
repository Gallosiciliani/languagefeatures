package it.unict.gallosiciliani.importing.pdf;

import it.unict.gallosiciliani.importing.partofspeech.POS;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.SequentialIRIProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import lombok.Getter;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Utility for tests involving a {@link LexicalEntry} consumer.
 *
 * @author Cristiano Longo
 */
public class LexicalEntryConsumerTestUtils {

    private interface LexicalEntryConsumer extends Consumer<LexicalEntry>{

    }

    public static class Expected{
        public final String lemma;
        public final POS pos;
        private Expected(final String lemma, final POS pos){
            this.lemma=lemma;
            this.pos=pos;
        }
    }

    public static class ExpectedNoun extends Expected{
        public ExpectedNoun(final String lemma){
            super(lemma, POS.NOUN);
        }
    }

    public static class ExpectedVerb extends Expected{
        public ExpectedVerb(final String lemma){
            super(lemma, POS.VERB);
        }
    }

    private final String ns;
    @Getter
    private final POSIndividualProvider posProvider;
    @Getter
    private final IRIProvider expectedIRIProvider;
    @Getter
    private final Consumer<LexicalEntry> lec;
    private final InOrder o;
    //number of received entries
    private int generatedEntries=0;


    /**
     *
     * @param ns expected namespace for accepted entries
     * @param posProvider expected Part of Speech individuals
     */
    public LexicalEntryConsumerTestUtils(final String ns, final POSIndividualProvider posProvider){
        lec=mock(LexicalEntryConsumer.class);
        o=inOrder(lec);
        this.ns=ns;
        this.posProvider=posProvider;
        this.expectedIRIProvider=new SequentialIRIProvider(ns);
    }

    public void assertAcceptedEntries(final Expected...expected){
        final List<LexicalEntry> captured=capture(expected.length);
        for(int i=0; i<expected.length; i++){
            final Expected e=expected[i];
            final LexicalEntry a=captured.get(i);
            checkLemma(e.lemma, a);
            checkPOS(e.pos, a);
        }
    }

    /**
     * Capture accepted entries
     *
     * @param numExpected number of expected entries
     * @return accepted entries
     */
    public List<LexicalEntry> capture(final int numExpected){
        final ArgumentCaptor<LexicalEntry> actualEntryCaptor = ArgumentCaptor.forClass(LexicalEntry.class);
        o.verify(lec, times(numExpected)).accept(actualEntryCaptor.capture());
        return actualEntryCaptor.getAllValues();
    }

    /**
     * Check whether the given entry has expectec IRIs and lemma
     * @param expectedLemma expected lemma
     * @param actual entry to be checked
     */
    private void checkLemma(final String expectedLemma, final LexicalEntry actual){
        final String expectedIri=ns+"entry"+(generatedEntries++);
        assertEquals(expectedIri, actual.getId());
        assertEquals(expectedIri + "-canonicalForm", actual.getCanonicalForm().getId());
        assertEquals(expectedLemma, actual.getCanonicalForm().getWrittenRep().get());
    }

    /**
     * Check whether the given entry has the specified part of speech.
     * @param expectedPOS expectex POS
     * @param actual entry to be checked
     */
    private void checkPOS(final POS expectedPOS, final LexicalEntry actual){
        switch (expectedPOS){
            case NOUN -> assertEquals(LexInfo.NOUN_INDIVIDUAL, actual.getPartOfSpeech().getId());
            case VERB -> assertSame(LexInfo.VERB_INDIVIDUAL, actual.getPartOfSpeech().getId());
            default -> throw new IllegalArgumentException("Test error: unexpected POS "+expectedPOS);
        }

    }

    public List<LexicalEntry> assertAcceptedEntry(final POS pos, final String...lemmas){
        final List<LexicalEntry> actuals = capture(lemmas.length);
        for(int i=0; i<lemmas.length; i++) {
            final LexicalEntry actual = actuals.get(i);
            checkLemma(lemmas[i], actuals.get(i));
            checkPOS(pos, actual);
        }
        return actuals;
    }

    public void assertAcceptedEntry(POS expectedPOS, final Form...expectedForms){
        final List<LexicalEntry> actuals = capture(expectedForms.length);
        for(int i=0; i<expectedForms.length; i++) {
            final String expectedIri=ns+"entry"+(generatedEntries++);
            final Form expectedForm=expectedForms[i];
            final LexicalEntry actual = actuals.get(i);
            assertEquals(expectedIri, actual.getId());
            assertSame(expectedForm, actual.getCanonicalForm());
            checkPOS(expectedPOS, actual);
        }
    }

    /**
     * Check that a noun has been received
     * @param lemma the expected lemma
     */
    public void assertAcceptedNoun(final String...lemma){
        assertAcceptedEntry(POS.NOUN, lemma);
    }

    /**
     * Check that a noun has been received
     * @param lemma the expected lemma
     */
    public void assertAcceptedVerb(final String...lemma){
        assertAcceptedEntry(POS.VERB, lemma);
    }

    /**
     * See {@link InOrder}
     */
    public void verifyNoMoreInteractions(){
        o.verifyNoMoreInteractions();
    }
}
