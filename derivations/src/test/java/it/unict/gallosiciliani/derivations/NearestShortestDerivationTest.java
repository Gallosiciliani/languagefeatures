package it.unict.gallosiciliani.derivations;

import org.junit.jupiter.api.Test;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link NearestShortestDerivation}
 *
 * @author Cristiano Longo
 */
public class NearestShortestDerivationTest {

    private final NearestShortestDerivation d;

    NearestShortestDerivationTest(){
        d=new NearestShortestDerivation("t");
    }

    @Test
    void shouldKeepNearestDerivationSameLenght(){
        final TargetedDerivation expected=createDerivation(1,2);
        d.accept(expected);
        d.accept(createDerivation(2, 2));
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(actualIt.next(), expected.getDerivation());
        assertFalse(actualIt.hasNext(), ""+d.getDerivation());
    }

    private void shouldKeepNearestDerivation(final int currentDerivationLength, final int newDerivationLength){
        final TargetedDerivation expected=createDerivation(1,currentDerivationLength);
        shouldKeepDerivation(expected, createDerivation(2, newDerivationLength));
    }

    private void shouldKeepDerivation(final TargetedDerivation existing, final TargetedDerivation toDiscard){
        d.accept(existing);
        d.accept(toDiscard);
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(actualIt.next(), existing.getDerivation());
        assertFalse(actualIt.hasNext(), ""+d.getDerivation());
    }

    @Test
    void shouldKeepNearestDerivationAlsoIfLonger(){
        shouldKeepNearestDerivation(2,1);
    }

    @Test
    void shouldKeepNearestDerivationCaseShorter(){
        shouldKeepNearestDerivation(1,2);
    }

    @Test
    void shouldKeepSameDistanceButShorterDerivation(){
        final TargetedDerivation expected=createDerivation(1, 2);
        final TargetedDerivation longer=createDerivation(1, 3);
        shouldKeepDerivation(expected, longer);
    }

    @Test
    void shouldReplaceFartherDerivationAlsoWithLonger(){
        final TargetedDerivation farther=createDerivation(2, 2);
        final TargetedDerivation closerButLonger=createDerivation(1, 3);
        shouldReplaceDerivation(farther, closerButLonger);
    }

    @Test
    void shouldReplaceFartherDerivationWithSameLength(){
        final TargetedDerivation farther=createDerivation(2, 2);
        final TargetedDerivation closer=createDerivation(1, 2);
        shouldReplaceDerivation(farther, closer);
    }

    @Test
    void shouldReplaceFartherDerivationWithCloserAndShorter(){
        final TargetedDerivation farther=createDerivation(2, 2);
        final TargetedDerivation closer=createDerivation(1, 1);
        shouldReplaceDerivation(farther, closer);
    }

    @Test
    void shouldReplaceDerivationWithSameDistanceButShorter(){
        final TargetedDerivation farther=createDerivation(2, 2);
        final TargetedDerivation closer=createDerivation(2, 1);
        shouldReplaceDerivation(farther, closer);
    }

    private void shouldReplaceDerivation(final TargetedDerivation existing, final TargetedDerivation better){
        d.accept(existing);
        d.accept(better);
        final Iterator<DerivationPathNode> actualIt=d.getDerivation().iterator();
        assertSame(actualIt.next(), better.getDerivation());
        assertFalse(actualIt.hasNext(), ""+d.getDerivation());
    }

    private TargetedDerivation createDerivation(final int distance, final int length){
        final DerivationPathNode d=createDerivation(length);
        return new TargetedDerivation() {
            @Override
            public DerivationPathNode getDerivation() {
                return d;
            }

            @Override
            public int getDistance() {
                return distance;
            }
        };
    }

    private DerivationPathNode createDerivation(final int length){
        return length==1 ? new DerivationPathNodeImpl("x0") : //node string is not relevant
            new DerivationPathNodeImpl("x"+(length-1), null, createDerivation(length-1)); //also the phenomenon is not relevant
    }
}
