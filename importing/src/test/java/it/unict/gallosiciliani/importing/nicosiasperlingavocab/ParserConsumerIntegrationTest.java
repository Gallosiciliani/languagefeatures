package it.unict.gallosiciliani.importing.nicosiasperlingavocab;

import it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator.LexicalEntriesGenerator;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static it.unict.gallosiciliani.importing.nicosiasperlingavocab.LexicalEntryConsumerTestUtils.*;

/**
 * Test {@link it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.Parser} and
 * {@link it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator.LexicalEntriesGenerator}
 * working together.
 *
 * @author Cristiano Longo
 */
public class ParserConsumerIntegrationTest {

    private final LexicalEntryConsumerTestUtils utils;
    private final LexicalEntriesGenerator generator;
    private final VocabTestParams vocab = VocabTestParams.TEST_120_126;

    ParserConsumerIntegrationTest(){
        final String ns="https://gallosiciliani.unict.it/test#";
        final POSIndividualProvider posIndividualProvider=new POSIndividualProvider();
        utils=new LexicalEntryConsumerTestUtils(ns, posIndividualProvider);
        generator = new LexicalEntriesGenerator(utils.getLec(), ns, posIndividualProvider);
    }

    @Test
    void testParsingPage120() throws IOException {
        try(final Parser parser = new Parser(generator, vocab.getPdfFilePath())) {
            parser.parsePage(vocab.getPageNumInTestFile(120));
            utils.assertAcceptedEntries(new ExpectedVerb("andè"),
                    new ExpectedNoun("ciancianeddiera"),
                    new ExpectedNoun("stràgöla"),
                    new ExpectedNoun("tëntöria"));
            utils.verifyNoMoreInteractions();
        }
    }

    @Test
    void testParsingPage126() throws IOException {
        try(final Parser parser = new Parser(generator, vocab.getPdfFilePath())) {
            testParsingPage126(parser);
            utils.verifyNoMoreInteractions();
        }
    }

    private void testParsingPage126(final Parser parser) throws IOException {
        parser.parsePage(vocab.getPageNumInTestFile(126));
        utils.assertAcceptedEntries(new ExpectedNoun("abetinö"),
                new ExpectedNoun("àbetö"),
                new ExpectedNoun("abìlë"),
                new ExpectedNoun("abìtö"));
    }

    @Test
    void testParsingPages125and126() throws IOException {
        try(final Parser parser = new Parser(generator, vocab.getPdfFilePath())) {
            parser.parsePage(vocab.getPageNumInTestFile(125));
            testParsingPage126(parser);
            utils.verifyNoMoreInteractions();
        }
    }
}


