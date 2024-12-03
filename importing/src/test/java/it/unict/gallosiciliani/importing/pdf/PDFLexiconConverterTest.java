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
    void testParsingPage514() throws IOException {
        final PDFLexiconConverter converter=new PDFLexiconConverter(utils.getLec(),utils.getExpectedIRIProvider(),
                utils.getPosProvider(), VocabTestParams.TEST_514.getPageNumInTestFile(514), VocabTestParams.TEST_514.getPageNumInTestFile(514));
        converter.read(VocabTestParams.TEST_514.getPdfFilePath());
        utils.assertAcceptedEntries(new ExpectedNoun("götaö"),
                new ExpectedNoun("gö̀tera"),
                new ExpectedNoun("gòterë"),
                new ExpectedNoun("gotö"),
                new ExpectedNoun("göugagnö"),
                new ExpectedNoun("gövernantë"),
                new ExpectedVerb("gövernè"),
                new ExpectedNoun("gövernö"),
                new ExpectedNoun("gracialuora"),
                new ExpectedNoun("grada"));
        utils.verifyNoMoreInteractions();
    }

}
