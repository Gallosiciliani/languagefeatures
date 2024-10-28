package it.unict.gallosiciliani.importing.partofspeech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link POS}
 */
public class POSTest {

    @Test
    void testNouns(){
        shouldReturnPOS(POS.NOUN);
    }

    void shouldReturnPOS(final POS expected){
        for(final String str: POSExamples.getExamples(expected))
            try {
                assertSame(expected, POS.get(str), "POS string "+str);
            } catch (final UnexpectedPOSStringException e) {
                throw new RuntimeException(e);
            }
    }

    @Test
    void testVerbs(){
        shouldReturnPOS(POS.VERB);
    }

    @Test
    void testIgnored(){
        shouldReturnPOS(POS.IGNORED);
    }

    @Test
    void testUnexpectedNoun(){
        shouldThrowExceptionOnUnexpectedPOS(POS.NOUN);
    }

    @Test
    void testUnexpectedVerb(){
        shouldThrowExceptionOnUnexpectedPOS(POS.VERB);
    }

    @Test
    void testUnexpectedIgnored(){
        shouldThrowExceptionOnUnexpectedPOS(POS.IGNORED);
    }

    private void shouldThrowExceptionOnUnexpectedPOS(final POS expectedSuggestedPOS){
        for(final String posString: POSExamples.getUnexpectedExamples(expectedSuggestedPOS)) {
            final UnexpectedPOSStringException actual = assertThrows(UnexpectedPOSStringException.class, () -> POS.get(posString), "POS String "+posString);
            assertEquals(posString, actual.getPosString());
            assertEquals(expectedSuggestedPOS, actual.getSuggestedPOS());
        }
    }
}
