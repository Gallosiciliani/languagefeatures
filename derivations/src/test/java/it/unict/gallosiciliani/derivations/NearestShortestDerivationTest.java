package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link NearestShortestDerivation}
 *
 * @author Cristiano Longo
 */
public class NearestShortestDerivationTest {

    private static final String TARGET = "target";
    private static final String ONE_TO_TARGET = "targxt";

    private final NearestShortestDerivation d;
    private final DerivationPathNode firstDerivation;

    NearestShortestDerivationTest(){
        d=new NearestShortestDerivation(TARGET);
        firstDerivation=createDerivation("source", ONE_TO_TARGET);
        assertTrue(d.test(firstDerivation));
    }

    @Test
    void shouldKeepNearestDerivation(){
        final DerivationPathNode neartestPath=createDerivation("source", TARGET);
        assertTrue(d.test(neartestPath));
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(actualIt.next(), neartestPath);
        assertFalse(actualIt.hasNext(), ""+d.getDerivation());
    }

    @Test
    void shouldKeepSameDistanceBugShorterDerivation(){
        final DerivationPathNode shorterPath=createDerivation(ONE_TO_TARGET);
        assertTrue(d.test(shorterPath));
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(actualIt.next(), shorterPath);
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldDiscardFartherDerivations(){
        assertFalse(d.test(createDerivation("another source")));
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(firstDerivation, actualIt.next());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldDiscardLongerDerivations(){
        assertTrue(d.test(createDerivation("source0", "source1", ONE_TO_TARGET)));
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(firstDerivation, actualIt.next());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldKeepSameDistanceLenghtDerivations(){
        final DerivationPathNode anotherDerivation=createDerivation("zelem", ONE_TO_TARGET);
        assertTrue(d.test(anotherDerivation));

        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        final DerivationPathNode actual1=actualIt.next();
        final DerivationPathNode actual2=actualIt.next();
        assertFalse(actualIt.hasNext());
        assertSame(firstDerivation, actual1);
        assertSame(anotherDerivation, actual2);
    }

    @Test
    void shouldReturnFalseIfTheNovelDerivationIsFartherThanThePreviousOnes(){
        final String twoToTarget="targxy";
        assertFalse(d.test(createDerivation("source", twoToTarget)));
    }

    @Test
    void shouldReturnFalseIfTheNovelDerivationIsNearestThanThePreviousOnes(){
        assertTrue(d.test(createDerivation("source", "source0", TARGET)));
    }

    @Test
    void shouldReturnTrueIfNovelDerivationHasTheSameNonZeroDistanceThanThePreviousOnes(){
        final String anotherOneToTarget="targey";
        assertTrue(d.test(createDerivation("source", anotherOneToTarget)));
    }

    private DerivationPathNode createDerivation(final String...forms) {
        return createDerivation(forms.length-1, forms);
    }

    private DerivationPathNode createDerivation(final int i, final String...forms){
        final DerivationPathNode prev=i==0? null: createDerivation(i-1, forms);
        return new DerivationPathNode() {
            @Override
            public String get() {
                return forms[i];
            }

            @Override
            public DerivationPathNode prev() {
                return prev;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return null;
            }

            @Override
            public int length() {
                return i+1;
            }

            @Override
            public String toString(){
                if (prev==null)
                    return forms[i];
                return forms[i]+"<-"+prev;
            }

        };
    }
}
