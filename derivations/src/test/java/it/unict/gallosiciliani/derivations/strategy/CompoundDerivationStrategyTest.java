package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.ComparatorByString;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test for {@link CompoundDerivationStrategy}
 * @author Cristiano Longo
 */
public class CompoundDerivationStrategyTest {

    @Test
    void shouldFollowAllBranchesOfChildStrategies() {

        /* SCENARIO
         * The linguistic phenomenon p derives y and z from x.
         */
        final DerivationPathNode x = mock(DerivationPathNode.class);
        when(x.toString()).thenReturn("x");
        final DerivationPathNode xy = mock(DerivationPathNode.class);
        when(xy.toString()).thenReturn("xy");
        final DerivationPathNode xz = mock(DerivationPathNode.class);
        when(xz.toString()).thenReturn("xz");
        final Collection<DerivationPathNode> expectedDerivations = List.of(xy, xz);

        /* The two strategies s1x and s2x, both characterized by the derivation x, when applied to [y,z], behave as follows:
         *
         * - s1x follows y and x itself, i.e. it returns two strategies s1xy ad s1x, related to the derivations x->y and x, respectively;
         * - s2x follows both y and z, i.e. it returns two strategies s2xy and s2xz, related to x->y and x->z, respectively.
         */
        final DerivationStrategy s1x = mock(DerivationStrategy.class);
        when(s1x.getDerivation()).thenReturn(x);
        when(s1x.toString()).thenReturn("s1x");
        final DerivationStrategy s1xy = mock(DerivationStrategy.class);
        when(s1xy.getDerivation()).thenReturn(xy);
        when(s1xy.toString()).thenReturn("s1xy");
        when(s1x.branch(eq(expectedDerivations))).thenReturn(List.of(s1x, s1xy));

        final DerivationStrategy s2x = mock(DerivationStrategy.class);
        when(s2x.getDerivation()).thenReturn(x);
        when(s2x.toString()).thenReturn("s2x");
        final DerivationStrategy s2xy = mock(DerivationStrategy.class);
        when(s2xy.getDerivation()).thenReturn(xy);
        when(s2xy.toString()).thenReturn("s2xy");
        final DerivationStrategy s2xz = mock(DerivationStrategy.class);
        when(s2xz.getDerivation()).thenReturn(xz);
        when(s2xz.toString()).thenReturn("s2xz");
        when(s2x.branch(eq(expectedDerivations))).thenReturn(List.of(s2xy, s2xz));

        /*
         * A compound strategy c, built on s1x and s2x, thus
         * should return three compound strategies cx, cxy, cxz when applied to [x, y], such that
         * - cx corresponds to the derivation x and has s1x as child;
         * - cxy corresponds to the derivation x->y and has s1xy and s2xy as children;
         * - cxz corresponds to the derivation x->z and has s2xz as child.
         */
        final DerivationStrategy c = new CompoundDerivationStrategy(List.of(s1x, s2x));
        final Collection<DerivationStrategy> actual = c.branch(expectedDerivations);
        final List<DerivationStrategy> actualSorted = new ArrayList<>(actual);
        actualSorted.sort(ComparatorByString.IN);
        final Iterator<DerivationStrategy> actualIt = actualSorted.iterator();

        //we can't get the children of a compound strategy, so we call end and check that it is called on the expected children
        final DerivationStrategy cx = actualIt.next();
        assertEquals(x, cx.getDerivation());
        cx.end();
        verify(s1x).end();
        verify(s2x, never()).end();
        verify(s1xy, never()).end();
        verify(s2xy, never()).end();
        verify(s2xz, never()).end();

        final DerivationStrategy cxy = actualIt.next();
        assertEquals(xy, cxy.getDerivation());
        cxy.end();
        verify(s1xy).end();
        verify(s2xy).end();
        verify(s2xz, never()).end();
        verify(s2x, never()).end();

        final DerivationStrategy cxz = actualIt.next();
        assertEquals(xz, cxz.getDerivation());
        cxz.end();
        verify(s2xz).end();
        verify(s2x, never()).end();

        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldEndAllChildrenOnEnd() {
        final DerivationStrategy s1 = mock(DerivationStrategy.class);
        final DerivationStrategy s2 = mock(DerivationStrategy.class);
        final CompoundDerivationStrategy c = new CompoundDerivationStrategy(List.of(s1, s2));
        c.end();
        verify(s1).end();
        verify(s2).end();
    }
}