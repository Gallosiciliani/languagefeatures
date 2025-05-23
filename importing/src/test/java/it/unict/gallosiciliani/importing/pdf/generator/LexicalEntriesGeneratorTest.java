package it.unict.gallosiciliani.importing.pdf.generator;
import it.unict.gallosiciliani.importing.pdf.LexicalEntryConsumerTestUtils;
import it.unict.gallosiciliani.importing.partofspeech.POSExamples;
import it.unict.gallosiciliani.importing.partofspeech.POS;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test for {@link LexicalEntriesGenerator}
 * @author Cristiano Longo
 */
public class LexicalEntriesGeneratorTest {
    private static final String NS = "http://gallosiciliani.unict.it/test#";

    private final LexicalEntryConsumerTestUtils utils;
    private final LexicalEntriesGenerator pdc;
    private final Runnable sendNounWithIgnored;
    private final Runnable sendVerbWithIgnored;


    LexicalEntriesGeneratorTest(){
        final POSIndividualProvider p = new POSIndividualProvider();
        utils=new LexicalEntryConsumerTestUtils(NS,p);
        pdc=new LexicalEntriesGenerator(utils.getLec(), NS, p);
        sendNounWithIgnored=sendWithIgnored(pdc, POS.NOUN);
        sendVerbWithIgnored=sendWithIgnored(pdc, POS.VERB);
    }

    private static Runnable sendWithIgnored(final LexicalEntriesGenerator pdc, final POS pos){
        return () -> {
            pdc.pos(POSExamples.getExamples(POS.IGNORED)[0]);
            pdc.pos(POSExamples.getExamples(pos)[0]);
            pdc.pos(POSExamples.getExamples(POS.IGNORED)[1]);
            pdc.pos(POSExamples.getExamples(pos)[1]);
            pdc.pos(POSExamples.getExamples(POS.IGNORED)[2]);
        };
    }

    /**
     * A pos as first event is illegal
     */
    @Test
    void shouldThrowExceptionOnPOSIgnoredAsFirstEvent(){
        assertThrows(RuntimeException.class, () -> pdc.pos(POSExamples.getExamples(POS.IGNORED)[0]));
    }

    /**
     * A pos as first event is illegal
     */
    @Test
    void shouldThrowExceptionOnPOSNounsAsFirstEvent(){
        assertThrows(RuntimeException.class, () -> pdc.pos(POSExamples.getExamples(POS.NOUN)[0]));
    }

    /**
     * A pos as first event is illegal
     */
    @Test
    void shouldThrowExceptionOnPOSVerbAsFirstEvent(){
        assertThrows(RuntimeException.class, () -> pdc.pos(POSExamples.getExamples(POS.VERB)[0]));
    }


    @Test
    void shouldIgnoreLemmaWithPosNotRepresentingNounsOrVerbs(){
        pdc.lemma("a lemma");
        for(final String ignored : POSExamples.getExamples(POS.IGNORED)){
            pdc.pos(ignored);
            verifyNoInteractions(utils.getLec());
        }
        checkIsWorking();
    }


    /**
     * Just check that the generator continues working
     */
    private void checkIsWorking(){
        final String lemmanoun = "justAnotherLemmaNoun";
        pdc.lemma(lemmanoun);
        pdc.pos(POSExamples.getExamples(POS.NOUN)[0]);
        utils.assertAcceptedNoun(lemmanoun);

        final String lemmaverb = "justAnotherLemmaVerb";
        pdc.lemma(lemmaverb);
        pdc.pos(POSExamples.getExamples(POS.VERB)[0]);
        utils.assertAcceptedVerb(lemmaverb);
    }

    /**
     * For each lemma with a POS recognized as noun, a corresponding lexical
     * entry is created
     */
    @Test
    void shouldReturnNoun(){
        shouldReturnLemmaWithPOS(POS.NOUN);
    }

    /**
     * Lemmas not related to POS are ignored
     */
    @Test
    void shouldIgnoreLemmasWithoutPOS(){
        pdc.lemma("discard me");

        checkIsWorking();
    }

    @Test
    void shouldReturnNounWhenInterwowenWithIgnored(){
        shouldReturnLemmaWhenInterwowenWithIgnored(POS.NOUN);
    }

    /**
     * For each lemma with a POS recognized as verb, a corresponding lexical
     * entry is created
     */
    @Test
    void shouldReturnVerb(){
        shouldReturnLemmaWithPOS(POS.VERB);
    }

    @Test
    void shouldReturnVerbWhenInterwowenWithIgnored(){
        shouldReturnLemmaWhenInterwowenWithIgnored(POS.VERB);
    }

    private void shouldReturnLemmaWithPOS(final POS expectedPOS){
        for(final String posStr : POSExamples.getExamples(expectedPOS)){
            final String lemma = "lemmaFor"+posStr;
            pdc.lemma(lemma);
            pdc.pos(posStr);
            utils.assertAcceptedEntry(expectedPOS, lemma);
        }

        checkIsWorking();
    }

    private void shouldReturnLemmaWhenInterwowenWithIgnored(final POS pos){
        final String lemma = "theLemma";
        pdc.lemma("theLemma");
        pdc.pos(POSExamples.getExamples(POS.IGNORED)[0]);
        pdc.pos(POSExamples.getExamples(pos)[0]);
        utils.assertAcceptedEntry(pos,lemma);

        pdc.pos(POSExamples.getExamples(POS.IGNORED)[1]);
        pdc.pos(POSExamples.getExamples(pos)[1]);
        pdc.pos(POSExamples.getExamples(POS.IGNORED)[2]);

        checkIsWorking();
    }


    @Test
    void testNOUNWithIgnored(){
        testLemmaWithPOSAndIgnored(POS.NOUN);
    }

    @Test
    void testVERBWithIgnored(){
        testLemmaWithPOSAndIgnored(POS.VERB);
    }

    private void testLemmaWithPOSAndIgnored(final POS expectedPOS){
        for(final String nounsPosStr : POSExamples.getExamples(expectedPOS)){
            final String lemma = "lemmaFor"+nounsPosStr;
            pdc.lemma(lemma);
            pdc.pos(POSExamples.getExamples(POS.IGNORED)[0]);
            pdc.pos(nounsPosStr);
            pdc.pos(POSExamples.getExamples(POS.IGNORED)[1]);
            utils.assertAcceptedEntry(expectedPOS, lemma);
        }
        checkIsWorking();
    }

    @Test
    void shouldThrowExceptionOnInvalidPOSString(){
        assertThrows(RuntimeException.class, () -> {
            pdc.lemma("a lemma");
            pdc.pos("invalid pos");
        });
    }

    //MULTIPLE FORMS, ONE POS
    /**
     * Some dictionary entries contain multiple forms. An example is
     * "abonè o a bonè e a bonebonè"
     * In this case we create multiple entries, one for each form.
     */
    @Test
    void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndNOUNPos(){
        shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePos(POS.NOUN);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndNOUNPosInterwovenByIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePosInterwovenByIgnored(POS.NOUN);
    }

    /**
     * Analogous to shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndNOUNPos, but for VERBs
     */
    @Test
    void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndVERBPos(){
        shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePos(POS.VERB);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndVERBPosInterwovenByIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePosInterwovenByIgnored(POS.VERB);
    }

    private void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePos(final POS expectedPOS){
        final String[] expectedLemmas={"abonè", "a bonè", "a bonebonè"};

        //this one has to be discarded
        pdc.lemma("discard me");

        sendConjunctedLemmas(expectedLemmas);
        //here the entries are produced
        pdc.pos(POSExamples.getExamples(expectedPOS)[0]);

        utils.assertAcceptedEntry(expectedPOS, expectedLemmas);

        utils.verifyNoMoreInteractions();
        checkIsWorking();
    }

    private void sendConjunctedLemmas(final String[] expectedLemmas){
        for(int i=0; i<expectedLemmas.length-1; i++) {
            pdc.lemma(expectedLemmas[i]);
            pdc.conjunction();
        }
        pdc.lemma(expectedLemmas[expectedLemmas.length-1]);
    }
    private void shouldCreateMultipleEntriesForDictionaryEntriesWithMultipleFormsAndOnePosInterwovenByIgnored(final POS expectedPOS) {
        final String[] expectedLemmas={"abonè", "a bonè", "a bonebonè"};

        sendConjunctedLemmas(expectedLemmas);
        //here the entries are produced
        pdc.pos(POSExamples.getExamples(expectedPOS)[0]);
        sendWithIgnored(pdc, expectedPOS).run();

        utils.assertAcceptedEntry(expectedPOS, expectedLemmas);
        utils.verifyNoMoreInteractions();
        checkIsWorking();
    }

    // SINGLE FORM, MULTIPLE POS
    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndPosNounAndVerb(){
        shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndTwoPos(() -> pdc.pos(POSExamples.getExamples(POS.NOUN)[0]),
                POS.NOUN, () -> pdc.pos(POSExamples.getExamples(POS.VERB)[0]), POS.VERB);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndPosNounAndVerbInterwovenByIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndTwoPos(sendNounWithIgnored, POS.NOUN,
                sendVerbWithIgnored, POS.VERB);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndPosVerbAndNoun(){
        shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndTwoPos(() -> pdc.pos(
                POSExamples.getExamples(POS.VERB)[0]), POS.VERB, () -> pdc.pos(POSExamples.getExamples(POS.NOUN)[0]), POS.NOUN);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndPosVerbAndNounInterwovenByIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndTwoPos(sendVerbWithIgnored, POS.VERB,
                sendNounWithIgnored, POS.NOUN);
    }

    private void shouldCreateMultipleEntriesForDictionaryEntryWithOneFormAndTwoPos(final Runnable sendPos1,
                                                                                   POS expectedPOS1, final Runnable sendPos2, POS expectedPOS2){
        final String expectedLemma="a lemma";

        pdc.lemma(expectedLemma);

        sendPos1.run();
        final LexicalEntry actualFirstEntry = utils.assertAcceptedEntry(expectedPOS1, expectedLemma).get(0);

        //another entry, with the same canonical form of the first one, is produced.
        sendPos2.run();
        utils.assertAcceptedEntry(expectedPOS2, actualFirstEntry.getCanonicalForm());

        utils.verifyNoMoreInteractions();

        checkIsWorking();
    }


    // MULTIPLE FORMS, MULTIPLE POS

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndNOunAndVerbPos(){
        shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndTwoPos(()-> pdc.pos(
                        POSExamples.getExamples(POS.NOUN)[0]), POS.NOUN, ()-> pdc.pos(
                                POSExamples.getExamples(POS.VERB)[0]), POS.VERB);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndNounAndVerbInterwovenWithIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndTwoPos(sendNounWithIgnored, POS.NOUN,
                sendVerbWithIgnored, POS.VERB);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndVerbAndNounPos(){
        shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndTwoPos(()-> pdc.pos(
                        POSExamples.getExamples(POS.VERB)[0]),
                POS.VERB, ()-> pdc.pos(POSExamples.getExamples(POS.NOUN)[0]), POS.NOUN);
    }

    @Test
    void shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndVerbAndNounInterwovenWithIgnored(){
        shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndTwoPos(sendVerbWithIgnored, POS.VERB, sendNounWithIgnored, POS.NOUN);
    }

    private void shouldCreateMultipleEntriesForDictionaryEntryWithMultipleFormsAndTwoPos(final Runnable sendPos1,
                                                                                         POS expectedPOS1, final Runnable sendPos2, POS expectedPOS2){
        final String[] expectedLemmas={"a lemma","another lemma"};

        sendConjunctedLemmas(expectedLemmas);
        sendPos1.run();
        final List<LexicalEntry> actualEntries1=utils.assertAcceptedEntry(expectedPOS1, expectedLemmas);

        //another entry, with the same canonical form of the first one, is produced.
        sendPos2.run();

        utils.assertAcceptedEntry(expectedPOS2, actualEntries1.get(0).getCanonicalForm(), actualEntries1.get(1).getCanonicalForm());

        utils.verifyNoMoreInteractions();
        checkIsWorking();
    }

    @Test
    void shouldAvoidWellKnownDuplicateLemmas(){
        pdc.lemma("cömenazziön");
        pdc.pos("sost.masch.");
        utils.assertAcceptedNoun("cömenazziön");

        pdc.lemma("cömprëndö");
        pdc.pos("verbo");
        utils.assertAcceptedVerb("cömprëndö");

        pdc.lemma("cömenazziön");
        pdc.pos("sost.femm.");
        utils.verifyNoMoreInteractions();
        checkIsWorking();
    }
}

