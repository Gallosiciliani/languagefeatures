package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
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
    private final Set<DerivationPathNode> derivation;

    public NearestShortestDerivation(final String target){
        this.target=target;
        distance=length=Integer.MAX_VALUE;
        //compare by string representation, as all the derivations in this set
        //will have the same distance to the target (i.e. the minimum distance).
        derivation=new TreeSet<>(Comparator.comparing(Object::toString));
    }

    @Override
    public boolean test(DerivationPathNode n) {
        final int newDistance = LevenshteinDistance.getDefaultInstance().apply(n.get(), target);
        if (newDistance>distance)
            return false;
        if (newDistance==distance && n.length()>length)
            return true;
        if (newDistance<distance || n.length()<length)
            derivation.clear();
        distance=newDistance;
        length=n.length();
        derivation.add(n);
        return true;
    }
}
