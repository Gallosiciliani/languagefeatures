package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.function.Predicate;

/**
 * Given a target (nicosian) lexema, accepts derivations but save
 * only the nearest and shortest ones
 */
@Getter
public class NearestShortestDerivation implements Predicate<DerivationPathNode> {

    private final String target;

    private int distance;
    private int length;
    private final Collection<DerivationPathNode> derivation;

    public NearestShortestDerivation(final String target){
        this.target=target;
        distance=length=Integer.MAX_VALUE;
        //compare by string representation, as all the derivations in this set
        //will have the same distance to the target (i.e. the minimum distance).
        derivation=new LinkedList<>();
    }

    @Override
    public boolean test(final DerivationPathNode n) {
        final long startTime=System.currentTimeMillis();
        final int newDistance = LevenshteinDistance.getDefaultInstance().apply(n.get(), target);
        final long elapsedTime=System.currentTimeMillis()-startTime;
        if (elapsedTime>10)
            System.out.println("LevenshteinDistance elapsed time "+elapsedTime);
        if (newDistance>distance)
            return false;
        if (newDistance==distance && n.length()>length)
            return true;
        if (newDistance<distance || n.length()<length)
            derivation.clear();
        distance=newDistance;
        length=n.length();
        derivation.add(n);
        return newDistance!=0;
    }
}
