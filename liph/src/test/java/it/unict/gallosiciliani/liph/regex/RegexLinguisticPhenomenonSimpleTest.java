package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.LanguageFeatureTestHelper;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link RegexLinguisticPhenomenonSimple}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenonSimpleTest {
    private final String plainPattern = "abc";
    private final String replacement = "de";

    private final LinguisticPhenomenon t = new RegexLinguisticPhenomenonSimple("http://test,org/f", plainPattern, replacement);
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
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenonSimple("http://test,org/f", "a(.)c", "d$1");
        new LanguageFeatureTestHelper(t).derives("axc", "dx");
    }

    @Test
    void testUsingTwoGroupsInReplacement(){
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenonSimple("http://test,org/f", "(\\S)x(\\S)", "$1b$2");
        new LanguageFeatureTestHelper(t).derives("axc", "abc");
    }


    @Test
    void testPatternBeginningOfStr(){
        final LinguisticPhenomenon t = new RegexLinguisticPhenomenonSimple("http://test,org/f", "^ab", "x");
        new LanguageFeatureTestHelper(t).derives("abab", "xab");
    }

}
