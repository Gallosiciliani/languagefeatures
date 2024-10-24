package it.unict.gallosiciliani.importing.partofspeech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link POS}
 */
public class POSTest {

    @Test
    void testNouns(){
        shouldReturnPOS(POS.NOUN, POSExamples.NOUN);
    }

    void shouldReturnPOS(final POS expected, final String...posStr){
        for(final String str: posStr)
            try {
                assertSame(expected, POS.get(str));
            } catch (final UnexpectedPOSStringException e) {
                throw new RuntimeException(e);
            }
    }

    @Test
    void testVerbs(){
        shouldReturnPOS(POS.VERB, POSExamples.VERB);
    }

    @Test
    void testIgnored(){
        shouldReturnPOS(POS.IGNORED, POSExamples.IGNORED);
    }

    @Test
    void testUnexpectedNoun(){
        shouldThrowExceptionOnUnexpectedPOS(POS.NOUN, UnexpectedPOSStringException.suggestedNouns);
    }

    @Test
    void testUnexpectedVerb(){
        shouldThrowExceptionOnUnexpectedPOS(POS.VERB, UnexpectedPOSStringException.suggestedVerbs);
    }

    @Test
    void testUnexpectedIgnored(){
        shouldThrowExceptionOnUnexpectedPOS(POS.IGNORED, UnexpectedPOSStringException.suggestedIgnored);
    }

    private void shouldThrowExceptionOnUnexpectedPOS(final POS expectedSuggestedPOS, final String[] examples){
        for(final String posString: examples) {
            final UnexpectedPOSStringException actual = assertThrows(UnexpectedPOSStringException.class, () -> POS.get(posString));
            assertEquals(posString, actual.getPosString());
            assertEquals(expectedSuggestedPOS, actual.getSuggestedPOS());
        }
    }
}
