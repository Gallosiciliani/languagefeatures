package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.function.Predicate;

/**
 * Given a target (nicosian) lexema, accepts derivations but save
 * only the nearest and shortest ones
 */
@Getter
@Slf4j
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
        final int newDistance = LevenshteinDistance.getDefaultInstance().apply(n.get(), target);
        if (newDistance>distance)
            //check if this derivation is going worst or better
            return n.prev()==null ||
                    LevenshteinDistance.getDefaultInstance().apply(n.prev().get(), target)>=newDistance;
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
