package it.unict.gallosiciliani.derivations;

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
        final LinguisticPhenomenon p=mock(LinguisticPhenomenon.class);
        when(p.getId()).thenReturn("http://test.org/p");
        when(p.apply("x")).thenReturn(Set.of("y"));
        final LinguisticPhenomenon q=mock(LinguisticPhenomenon.class);
        when(q.getId()).thenReturn("http://test.org/q");
        when(q.apply("x")).thenReturn(Set.of("y1"));
        final MissedPhenomenaFinder finder=new MissedPhenomenaFinder(List.of(p,q));

        final DerivationPathNode x=new DerivationPathNodeImpl("x");
        final DerivationPathNode y=new DerivationPathNodeImpl("y",x,p);

        final Iterator<LinguisticPhenomenon> actualIt=finder.getMissedPhenomena(y).iterator();
        assertSame(q,actualIt.next());
        assertFalse(actualIt.hasNext());
    }
}
