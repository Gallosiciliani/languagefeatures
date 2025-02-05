package it.unict.gallosiciliani.liph.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link RegexLinguisticPhenomenaConflictsDetector}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenaConflictsDetectorTest {
    @Test
    void shouldDetectConflict(){
        final RegexLinguisticPhenomenaConflictsDetector d=new RegexLinguisticPhenomenaConflictsDetector();
        final RegexLinguisticPhenomenon p=new RegexLinguisticPhenomenon("http://test.org/p", "[ab]");
        final RegexLinguisticPhenomenon q=new RegexLinguisticPhenomenon("http://test.org/q", "[bc]");
        d.accept(p);
        d.accept(q);
        assertFalse(d.getConflicts().isEmpty());
    }
}
