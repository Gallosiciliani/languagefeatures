package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.LanguageFeatureTestHelper;
import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for {@link FiniteStateLinguisticPhenomenon}
 * @author Cristiano Longo
 */
public class FiniteStateLinguisticPhenomenonTest {

    private final String plainPattern = "abc";
    private final String replacement = "de";

    private final FiniteStateLinguisticPhenomenon t = createTestPhenomenon();
    private final LanguageFeatureTestHelper h = new LanguageFeatureTestHelper(t);

    private FiniteStateLinguisticPhenomenon createTestPhenomenon(){
        return createTestPhenomenon(plainPattern, replacement);
    }

    private FiniteStateLinguisticPhenomenon createTestPhenomenon(final String regex, final String replacement){
        final FiniteStateLinguisticPhenomenon p=new FiniteStateLinguisticPhenomenon();
        p.setId("http://test,org/f");
        p.setMatchingPattern(regex);
        p.setReplaceWith(replacement);
        return p;
    }

    @Test
    void shouldReturnFeatureIRI(){
        assertEquals("http://test,org/f", t.getId());
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
        final LinguisticPhenomenon t = createTestPhenomenon("a(.)c", "d$1");
        new LanguageFeatureTestHelper(t).derives("axc", "dx");
    }

    @Test
    void testUsingTwoGroupsInReplacement(){
        final LinguisticPhenomenon t = createTestPhenomenon("(\\S)x(\\S)", "$1b$2");
        new LanguageFeatureTestHelper(t).derives("axc", "abc");
    }

    @Test
    void testOverlappingPatterns(){
        final LinguisticPhenomenon t = createTestPhenomenon("aba", "x");
        new LanguageFeatureTestHelper(t).derives("ababa", "xba", "abx");
    }

    @Test
    void testPatternBeginningOfStr(){
        final LinguisticPhenomenon t = createTestPhenomenon("^ab", "x");
        new LanguageFeatureTestHelper(t).derives("abab", "xab");
    }

}
