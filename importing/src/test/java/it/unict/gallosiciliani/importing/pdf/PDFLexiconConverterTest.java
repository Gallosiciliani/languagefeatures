package it.unict.gallosiciliani.importing.pdf;

import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import org.junit.jupiter.api.Test;

import static it.unict.gallosiciliani.importing.pdf.LexicalEntryConsumerTestUtils.*;
import java.io.IOException;

/**
 * Test for {@link it.unict.gallosiciliani.importing.pdf.PDFLexiconConverter}
 * @author Cristiano Longo
 */
public class PDFLexiconConverterTest {

    private final LexicalEntryConsumerTestUtils utils;

    PDFLexiconConverterTest(){
        final String ns="https://gallosiciliani.unict.it/test#";
        final POSIndividualProvider posIndividualProvider=new POSIndividualProvider();
        utils=new LexicalEntryConsumerTestUtils(ns, posIndividualProvider);
    }

    @Test
    void testParsingPage585() throws IOException {
        final PDFLexiconConverter converter=new PDFLexiconConverter(utils.getLec(),utils.getExpectedIRIProvider(),
                utils.getPosProvider(), VocabTestParams.TEST_585.getPageNumInTestFile(585), VocabTestParams.TEST_585.getPageNumInTestFile(585));
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
