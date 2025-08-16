package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link MissedPhenomenaFinder}
 *
 * @author Cristiano Longo
 */
public class MissedPhenomenaFinderTest {

    @Test
    void shouldReturnMissedPhenomena(){
        // x-p->y-q->z
        final LinguisticPhenomenon p= mock(LinguisticPhenomenon.class);
        when(p.getId()).thenReturn("http://test.org/p");
        when(p.apply("x")).thenReturn(Set.of("y"));
        final LinguisticPhenomenon q= mock(LinguisticPhenomenon.class);
        when(q.getId()).thenReturn("http://test.org/q");
        when(q.apply("x")).thenReturn(Set.of("y1"));
        final MissedPhenomenaFinder finder=new MissedPhenomenaFinder(List.of(p,q));

        final DerivationPathNode x=new DerivationPathNodeImpl("x");
        final DerivationPathNode y=new DerivationPathNodeImpl("y", p, x);

        final DerivationPhenomena actual=finder.getMissedPhenomena(y);
        final Iterator<LinguisticPhenomenon> actualExpectedIt=actual.getSuitablePhenomena().iterator();
        assertSame(p, actualExpectedIt.next());
        assertSame(q, actualExpectedIt.next());
        assertFalse(actualExpectedIt.hasNext());

        final Iterator<LinguisticPhenomenon> actualMissedIt=actual.getMissedPhenomena().iterator();
        assertSame(q,actualMissedIt.next());
        assertFalse(actualMissedIt.hasNext());
    }
}
