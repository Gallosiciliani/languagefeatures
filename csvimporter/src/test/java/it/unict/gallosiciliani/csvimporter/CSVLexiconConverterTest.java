package it.unict.gallosiciliani.csvimporter;

import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link CSVLexiconConverter}
 *
 * @author Cristiano Longo
 */
public class CSVLexiconConverterTest {

    private interface ExpectedLatinEtym{

        String getLabel();
        String getWrittenRep();
        URI getSeeAlso();
    }

    private interface ExpectedEntry{
        String getLemma();
        String getPartOfSpeech();
        /**
         * A label describing the etymology
         * @return null if etymon is not available
         */
        String getEtymologyLabel();

        /**
         * Get the expected latin etyms, sorted by written representation
         *
         * @return expected latin etyms
         */
        ExpectedLatinEtym[] getLatinEtyms();
    }

    @Test
    void testImportFileWith3Rows() throws IOException {
        final POSIndividualProvider posProvider=new POSIndividualProvider();
        final String ns="http://test.org/entry#";
        final LexicalEntryConsumerTestUtils utils=new LexicalEntryConsumerTestUtils(ns, posProvider);

        final CSVLexiconConverter converter=new CSVLexiconConverter(utils.getLec(), utils.getExpectedIRIProvider(),
                utils.getPosProvider()
        );
        converter.read(getPdfFilePath("nicosia-test.csv"));

        final ExpectedLatinEtym expectedHabitu = new ExpectedLatinEtym() {
            @Override
            public String getLabel() {
                return "HABĬTU";
            }

            @Override
            public String getWrittenRep() {
                return "habitu";
            }

            @Override
            public URI getSeeAlso() {
                return URI.create("http://test.org/lemmas/habitu");
            }
        };

        final ExpectedLatinEtym expectedInu = new ExpectedLatinEtym() {
            @Override
            public String getLabel() {
                return "-ĪNU";
            }

            @Override
            public String getWrittenRep() {
                return "inu";
            }

            @Override
            public URI getSeeAlso() {
                return null;
            }
        };

        final Iterator<LexicalEntry> storedEntriesIt=utils.capture(3).iterator();

        final LexicalEntry first = storedEntriesIt.next();
        checkImportedEntry(new ExpectedEntry() {
            @Override
            public String getLemma() {
                return "abusè";
            }

            @Override
            public String getPartOfSpeech() {
                return LexInfo.VERB_INDIVIDUAL;
            }

            @Override
            public String getEtymologyLabel() {
                return null;
            }

            @Override
            public ExpectedLatinEtym[] getLatinEtyms() {
                return new ExpectedLatinEtym[0];
            }
        }, first);

        final LexicalEntry second = storedEntriesIt.next();
        checkImportedEntry(new ExpectedEntry() {
            @Override
            public String getLemma() {
                return "abetìnö";
            }

            @Override
            public String getPartOfSpeech() {
                return LexInfo.NOUN_INDIVIDUAL;
            }

            @Override
            public String getEtymologyLabel() {
                return "HABĬTU+-ĪNU";
            }

            @Override
            public ExpectedLatinEtym[] getLatinEtyms() {
                return new ExpectedLatinEtym[]{expectedHabitu, expectedInu};
            }
        }, second);

        final LexicalEntry third = storedEntriesIt.next();
        checkImportedEntry(new ExpectedEntry() {
            @Override
            public String getLemma() {
                return "àbetö";
            }

            @Override
            public String getPartOfSpeech() {
                return LexInfo.NOUN_INDIVIDUAL;
            }


            @Override
            public String getEtymologyLabel() {
                return "HABĬTU";
            }

            @Override
            public ExpectedLatinEtym[] getLatinEtyms() {
                return new ExpectedLatinEtym[]{expectedHabitu};
            }
        }, third);

        utils.verifyNoMoreInteractions();
    }

    private void checkImportedEntry(final ExpectedEntry expected, final LexicalEntry actual){
        assertEquals(expected.getLemma(), actual.getCanonicalForm().getWrittenRep().get());
        assertEquals(expected.getPartOfSpeech() , actual.getPartOfSpeech().getId());

        final Set<Etymology> etimologies = actual.getEtymology();
        assertEquals(1, etimologies.size());
        final Etymology actualEtymology = etimologies.iterator().next();
        assertEquals(expected.getEtymologyLabel(), actualEtymology.getLabel());

        final EtyLink l = actualEtymology.getStartingLink();
        assertEquals(actual.getId(), l.getEtyTarget().getId());
        assertEquals(actual.getCanonicalForm().getId(), l.getEtySubTarget().getId());
        checkLatinEtyms(expected.getLatinEtyms(), l);
    }


    /**
     *
     * @param expected expected latin etymologies, sorted by written representation
     * @param actualEtyLink the link in the etymology to be checked
     */
    private void checkLatinEtyms(final ExpectedLatinEtym[] expected, final EtyLink actualEtyLink){
        final TreeSet<Form> actualFormsSorted = new TreeSet<>(Comparator.comparing((f)-> f.getWrittenRep().get()));
        actualFormsSorted.addAll(actualEtyLink.getEtySubSource());
        final Iterator<Form> actualFormsIt = actualFormsSorted.iterator();
        for(final ExpectedLatinEtym expectedEtym : expected){
            assertTrue(actualFormsIt.hasNext(), "Too less latin etyms. "+expectedEtym.getWrittenRep()+" not found.");
            final Form actualEtym = actualFormsIt.next();
            assertEquals(expectedEtym.getLabel(), actualEtym.getLabel());
            assertEquals(expectedEtym.getWrittenRep(), actualEtym.getWrittenRep().get());
            assertEquals(expectedEtym.getSeeAlso(), actualEtym.getSeeAlso());
        }
        assertFalse(actualFormsIt.hasNext());
    }

    /**
     * Path of the PDF file which has to be parsed
     */
    public static String getPdfFilePath(final String pdfFileName){
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(pdfFileName)).toString().substring("file:".length());
    }

}
