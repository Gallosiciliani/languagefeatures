package it.unict.gallosiciliani.liphtools.liph.regex;

import it.unict.gallosiciliani.liphtools.util.LanguageFeatureTestHelper;
import it.unict.gallosiciliani.liphtools.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for {@link RegexLinguisticPhenomenon}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenonTest {

    private final String plainPattern = "abc";
    private final String replacement = "de";

    private final RegexLinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", plainPattern, replacement);
    private final LanguageFeatureTestHelper h = new LanguageFeatureTestHelper(t);

    @Test
    void shouldReturnFeatureIRI(){
        assertEquals("http://test,org/f", t.getIRI());
    }
    @Test
    void testReplaceSingleOccurrence(){
        h.derives("123"+plainPattern+"456", "123"+replacement+"456");
    }

    @Test
    void testReplaceMultipleOccurrences(){
        h.derives("123"+plainPattern+"456"+plainPattern+"789",
    "123"+plainPattern+"456"+replacement+"789",
        "123"+replacement+"456"+plainPattern+"789",
        "123"+replacement+"456"+replacement+"789");
    }

    @Test
    void testNoOccurrence(){
        h.notApply("123456");
    }

    @Test
    void testOccurrenceAtBeginning(){
        h.derives(plainPattern+"456"+plainPattern+"789",
            plainPattern+"456"+replacement+"789",
            replacement+"456"+plainPattern+"789",
            replacement+"456"+replacement+"789");
    }

    @Test
    void testOccurrenceAtEnd(){
        h.derives(plainPattern+"456"+plainPattern,
            plainPattern+"456"+replacement,
            replacement+"456"+plainPattern,
            replacement+"456"+replacement);
    }

    @Test
    void testUsingAGroupInReplacement(){
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "a(.)c", "d$1");
        new LanguageFeatureTestHelper(t).derives("axc", "dx");
    }

    @Test
    void testUsingTwoGroupsInReplacement(){
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "(\\S)x(\\S)", "$1b$2");
        new LanguageFeatureTestHelper(t).derives("axc", "abc");
    }

    @Test
    void testMultipleReplacements(){
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "a", "x", "y");
        new LanguageFeatureTestHelper(t).derives("1a2a",
                "1a2x",
                "1a2y",
                "1x2a",
                "1x2x",
                "1x2y",
                "1y2a",
                "1y2x",
                "1y2y");
    }

    @Test
    void testAddReplacement(){
        final RegexLinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "a", "x");
        t.addReplacement("y");
        new LanguageFeatureTestHelper(t).derives("1a2a",
                "1a2x",
                "1a2y",
                "1x2a",
                "1x2x",
                "1x2y",
                "1y2a",
                "1y2x",
                "1y2y");
    }

    @Test
    void testOverlappingPatterns(){
        final RegexLinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "aba", "x");
        new LanguageFeatureTestHelper(t).derives("ababa", "xba", "abx");
    }

    @Test
    void testPatternBeginningOfStr(){
        final RegexLinguisticPhenomenon t = new RegexLinguisticPhenomenon("http://test,org/f", "^ab", "x");
        new LanguageFeatureTestHelper(t).derives("abab", "xab");
    }

}
