package it.unict.gallosiciliani.pdfimporter;

import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import it.unict.gallosiciliani.pdfimporter.LexicalEntryConsumerTestUtils.*;

/**
 * Test for {@link it.unict.gallosiciliani.pdfimporter.PDFLexiconConverter}
 * @author Cristiano Longo
 */
public class PDFLexiconConverterTest {

    private static final String EXPECTED_LANG="expectedLang";
    private final LexicalEntryConsumerTestUtils utils;

    PDFLexiconConverterTest(){
        final String ns="https://gallosiciliani.unict.it/test#";
        final POSIndividualProvider posIndividualProvider=new POSIndividualProvider();
        utils=new LexicalEntryConsumerTestUtils(ns, posIndividualProvider, EXPECTED_LANG);
    }

    @Test
    void testParsingPage585() throws IOException {
        final PDFLexiconConverter converter=new PDFLexiconConverter(utils.getLec(),utils.getExpectedIRIProvider(),
                utils.getPosProvider(), VocabTestParams.TEST_585.getPageNumInTestFile(585), VocabTestParams.TEST_585.getPageNumInTestFile(585), EXPECTED_LANG);
        converter.read(VocabTestParams.TEST_585.getPdfFilePath());
        utils.assertAcceptedEntries(
                new ExpectedVerb("mbiẕẕarrìscessë"),
                new ExpectedVerb("mbiẕẕarrìsciö"),
                new ExpectedNoun("mböcadörö"),
                new ExpectedNoun("mböcamöschë"),
                new ExpectedVerb("mböchessë"),
                new ExpectedNoun("mböcönada"),
                new ExpectedVerb("mböddörönè"),
                new ExpectedVerb("mböfölìsciö"),
                new ExpectedVerb("mböfölönè"),
                new ExpectedVerb("mböïnessë"),
                new ExpectedNoun("mbömbö"),
                new ExpectedVerb("mbö̀rdessë"));
        utils.verifyNoMoreInteractions();
    }

}
