package it.unict.gallosiciliani.derivations.strategy;
import it.unict.gallosiciliani.derivations.ComparatorByString;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationsToTargetContainer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for {@link TargetedDerivationStrategy}
 *
 * @author Cristiano Longo
 */
public class TargetedDerivationStrategyTest {
    private final DerivationsToTargetContainer consumer=getContainer("xyz");
    private final DerivationPathNode abc=getDerivation("abc"); //distance 3 to target
    private final DerivationPathNode abz=getDerivation("abz"); //distance 2 to target
    private final DerivationPathNode ayz=getDerivation("ayz"); //distance 1 to target
    private final DerivationPathNode xbz=getDerivation("xbz"); //distance 1 to target
    private final DerivationPathNode abcd=getDerivation("abcd"); //distance 4 to target

    private DerivationPathNode getDerivation(final String s){
        final DerivationPathNode d=mock(DerivationPathNode.class);
        when(d.get()).thenReturn(s);
        when(d.toString()).thenReturn(s);
        return d;
    }

    private DerivationsToTargetContainer getContainer(final String target){
        final DerivationsToTargetContainer c=mock(DerivationsToTargetContainer.class);
        when(c.getTarget()).thenReturn(target);
        return c;
    }

    /**
     * Using {@link NearestStrategySelector}
     */
    @Test
    void testNearestToTarget(){
        final DerivationStrategy s=new TargetedDerivationStrategy(abc, consumer, NearestStrategySelector.FACTORY);
        final Collection<DerivationStrategy> actual=s.branch(List.of(abz,ayz,xbz,abcd));
        final List<DerivationStrategy> actualSorted=new ArrayList<>(actual);
        actualSorted.sort(ComparatorByString.IN);
        final Iterator<DerivationStrategy> actualIt=actualSorted.iterator();
        assertSame(ayz, actualIt.next().getDerivation());
        assertSame(xbz, actualIt.next().getDerivation());
        assertFalse(actualIt.hasNext());
    }

    /**
     * Using {@link NearestStrategySelector}
     */
    @Test
    void shouldNearestToTargetReturnItselfIfTheCase(){
        final DerivationStrategy s=new TargetedDerivationStrategy(ayz, consumer, NearestStrategySelector.FACTORY);
        final Collection<DerivationStrategy> actual=s.branch(List.of(abz,xbz,abcd));
        final List<DerivationStrategy> actualSorted=new ArrayList<>(actual);
        actualSorted.sort(ComparatorByString.IN);
        final Iterator<DerivationStrategy> actualIt=actualSorted.iterator();
        assertSame(ayz, actualIt.next().getDerivation());
        assertSame(xbz, actualIt.next().getDerivation());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldConsumeWhenReachTheTarget(){
        final DerivationPathNode n=getDerivation(consumer.getTarget());
        final TargetedDerivationStrategy s=new TargetedDerivationStrategy(n, consumer, null);
        verify(consumer).accept(s);
    }

    /**
     * Using {@link NotFartherStrategySelector}
     */
    @Test
    void testNotFartherToTarget(){
        final DerivationStrategy s=new TargetedDerivationStrategy(abc, consumer, NotFartherStrategySelector.FACTORY);
        final Collection<DerivationStrategy> actual=s.branch(List.of(abz,ayz,xbz,abcd));
        final List<DerivationStrategy> actualSorted=new ArrayList<>(actual);
        actualSorted.sort(ComparatorByString.IN);
        final Iterator<DerivationStrategy> actualIt=actualSorted.iterator();
        assertSame(abc, actualIt.next().getDerivation());
        assertSame(abz, actualIt.next().getDerivation());
        assertSame(ayz, actualIt.next().getDerivation());
        assertSame(xbz, actualIt.next().getDerivation());
        assertFalse(actualIt.hasNext());
    }

}
