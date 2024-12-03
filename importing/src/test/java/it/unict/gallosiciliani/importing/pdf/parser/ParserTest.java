package it.unict.gallosiciliani.importing.pdf.parser;

import it.unict.gallosiciliani.importing.pdf.VocabTestParams;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Mockito.inOrder;


import java.io.IOException;

/**
 * Test cases for {@link Parser}
 * @author Cristiano Longo
 *
 * NOTE: test is disabled, it requires the nicosiasperlinga.pdf file which cannot be published
 */
public class ParserTest {

    private final VocabTestParams testParams=VocabTestParams.TEST_120_126;
//    private final VocabTestParams testParams=VocabTestParams.VOCAB_FILE;
    private final ParsingDataConsumer c;

    ParserTest(){
        c=Mockito.mock(ParsingDataConsumer.class);
    }

    /**
     * The page with examples
     */
    @Test
    void testPage120() throws IOException {
        try(final Parser p = new Parser(c, testParams.getPdfFilePath())) {
            p.parsePage(testParams.getPageNumInTestFile(120));
            final InOrder o = inOrder(c);
            o.verify(c).lemma("andè");
            o.verify(c).pos("verbo");
            o.verify(c).lemma("ciancianeddiera");
            o.verify(c).pos("sost.femm.");
            o.verify(c).lemma("cö böna gana");
            o.verify(c).pos("avv.");
            o.verify(c).lemma("proprietà");
            o.verify(c).lemma("stràgöla");
            o.verify(c).pos("sost.femm.");
            o.verify(c).lemma("tëntöria");
            o.verify(c).pos("sost.femm.massa");
            o.verifyNoMoreInteractions();
        }
    }

    @Test
    void testParsingPage125() throws IOException {
        try(final Parser p = new Parser(c, testParams.getPdfFilePath())) {
            p.parsePage(testParams.getPageNumInTestFile(125));
            final InOrder o = inOrder(c);
            testParsingPage125(c,o);
            o.verifyNoMoreInteractions();
        }
    }

    private void testParsingPage125(final ParsingDataConsumer c, final InOrder o){
        o.verify(c).lemma("a");
        o.verify(c).pos("congiunz.sub.fin.");
        o.verify(c).lemma("â");
        o.verify(c).pos("prep.art.");
        o.verify(c).lemma("a armacoö");
        o.verify(c).pos("avv.locat.");
        o.verify(c).lemma("a bandë bandë");
        o.verify(c).pos("avv.");
        o.verify(c).lemma("abàsciö");
        o.verify(c).pos("paraverbo ottat.");
        o.verify(c).lemma("abastanza");
        o.verify(c).pos("avv.");
        o.verify(c).lemma("a bataghjön");
        o.verify(c).pos("avv.");
        o.verify(c).lemma("â beddëzza!");
        o.verify(c).pos("paraverbo sociale");
        o.verify(c).lemma("a bèddela tanta sperta muòire nê maë dâ böfa");
        o.verify(c).pos("paraverbo dichiar.");
        o.verify(c).lemma("â benedezziön");
        o.verify(c).pos("avv.");
        o.verify(c).lemma("abentö");
    }


    @Test
    void testParsingPage126() throws IOException {
        try(final Parser p = new Parser(c, testParams.getPdfFilePath())) {
            p.parsePage(testParams.getPageNumInTestFile(126));
            final InOrder o = inOrder(c);
            testParsingPage126(c,o);
            o.verifyNoMoreInteractions();
        }
    }

    private void testParsingPage126(final ParsingDataConsumer c, final InOrder o){
        o.verify(c).lemma("abetinö");
        o.verify(c).pos("sost.masch.");
        o.verify(c).lemma("àbetö");
        o.verify(c).pos("sost.masch.");
        o.verify(c).lemma("a bìfera");
        o.verify(c).pos("agg. inv.");
        o.verify(c).lemma("abìlë");
        o.verify(c).pos("sost.pl.");
        o.verify(c).lemma("abitè");
        o.verify(c).lemma("abìtö");
        o.verify(c).pos("sost.masch.");
        o.verify(c).lemma("a böca verta");
        o.verify(c).pos("agg. inv.");
        o.verify(c).lemma("a böcöë");
        o.verify(c).conjunction();
        o.verify(c).lemma("a böcön");
        o.verify(c).pos("agg. inv.");
        o.verify(c).lemma("àbölö");
        o.verify(c).pos("agg. solo masch.");
        o.verify(c).lemma("abonè");
        o.verify(c).pos("paraverbo dichiar.");
        o.verify(c).lemma("abonè");
        o.verify(c).conjunction();
        o.verify(c).lemma("a bonè");
        o.verify(c).conjunction();
        o.verify(c).lemma("a bonebonè");
        o.verify(c).pos("avv.preverb.");
        o.verify(c).lemma("â bönöra");
        o.verify(c).pos("avv.");
    }

    @Test
    void testParsingPages125and126() throws IOException {
        try(final Parser p = new Parser(c, testParams.getPdfFilePath())) {
            p.parsePage(testParams.getPageNumInTestFile(125));
            p.parsePage(testParams.getPageNumInTestFile(126));
            final InOrder o = inOrder(c);
            testParsingPage125(c,o);
            testParsingPage126(c,o);
            o.verifyNoMoreInteractions();
        }
    }

    @Test
    void testParsingMultiplePages() throws IOException {
        try(final Parser p=new Parser(c, testParams.getPdfFilePath())){
            p.parsePages(testParams.getPageNumInTestFile(125), testParams.getPageNumInTestFile(126));
            final InOrder o = inOrder(c);
            testParsingPage125(c,o);
            testParsingPage126(c,o);
            o.verifyNoMoreInteractions();
        }
    }

    /**
     * The page with accented letters with diacritics
     */
    @Test
    void testPage514() throws IOException {
        try(final Parser p = new Parser(c, VocabTestParams.TEST_514.getPdfFilePath())) {
            p.parsePage(VocabTestParams.TEST_514.getPageNumInTestFile(514));
            final InOrder o = inOrder(c);
            o.verify(c).lemma("götaö");
            o.verify(c).pos("sost.masch.massa");
            o.verify(c).lemma("götarösö");
            o.verify(c).pos("agg.");
            o.verify(c).lemma("gotë");
            o.verify(c).lemma("gö̀tera");
            o.verify(c).pos("sost.femm. solo sing.");
//            o.verifyNoMoreInteractions();
        }
    }
}
