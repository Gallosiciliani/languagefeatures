package it.unict.gallosiciliani.liph.regex;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link RegexLinguisticPhenomenaConflictsDetector}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenaConflictsDetectorTest {
    @Test
    void shouldDetectConflict() {
        final RegexLinguisticPhenomenaConflictsDetector d = new RegexLinguisticPhenomenaConflictsDetector();
        final RegexLinguisticPhenomenon p = new RegexLinguisticPhenomenon("http://test.org/p", "[ab]");
        final RegexLinguisticPhenomenon q = new RegexLinguisticPhenomenon("http://test.org/q", "[bc]");
        d.accept(p);
        d.accept(q);

        final Map<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> actualConflicts = d.getConflicts();

        final Iterator<Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>>> actualMap = actualConflicts.entrySet().iterator();
        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry1 = actualMap.next();
        assertSame(p, entry1.getKey());
        final Set<RegexLinguisticPhenomenon> conflicting1 = entry1.getValue();
        assertEquals(Set.of(p, q), conflicting1);
        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry2 = actualMap.next();
        assertSame(q, entry2.getKey());
        assertSame(conflicting1, entry2.getValue());
        assertFalse(actualMap.hasNext());
    }

    @Test
    void shouldDetectIndirectConflict() {
        final RegexLinguisticPhenomenaConflictsDetector d = new RegexLinguisticPhenomenaConflictsDetector();
        //p is coflicting with q
        final RegexLinguisticPhenomenon p = new RegexLinguisticPhenomenon("http://test.org/p", "[ab]");
        final RegexLinguisticPhenomenon q = new RegexLinguisticPhenomenon("http://test.org/q", "[bc]");
        //r is coflicting with q but not with q
        final RegexLinguisticPhenomenon r = new RegexLinguisticPhenomenon("http://test.org/r", "[cd]");
        d.accept(p);
        d.accept(q);
        d.accept(r);

        final Iterator<Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>>> actual=d.getConflicts().entrySet().iterator();
        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry1=actual.next();
        assertSame(p, entry1.getKey());
        final Set<RegexLinguisticPhenomenon> actualConflicts=entry1.getValue();
        assertEquals(actualConflicts, Set.of(p,q,r));

        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry2=actual.next();
        assertSame(q, entry2.getKey());
        assertSame(actualConflicts, entry2.getValue());

        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry3=actual.next();
        assertSame(r, entry3.getKey());
        assertSame(actualConflicts, entry3.getValue());

        assertFalse(actual.hasNext());
    }

    @Test
    void shouldEveryPhenomenonConflictWithItself() {
        final RegexLinguisticPhenomenaConflictsDetector d = new RegexLinguisticPhenomenaConflictsDetector();
        //p is coflicting with q
        final RegexLinguisticPhenomenon p = new RegexLinguisticPhenomenon("http://test.org/p", "[ab]");
        d.accept(p);

        final Iterator<Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>>> actual=d.getConflicts().entrySet().iterator();
        final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> entry1=actual.next();
        assertSame(p, entry1.getKey());
        assertEquals(Set.of(p), entry1.getValue());
    }
}