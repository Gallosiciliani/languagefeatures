package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Given a target (nicosian) lexema, accepts derivations but save
 * only the nearest and shortest ones
 */
public class NearestShortestDerivation implements Consumer<DerivationPathNode> {

    private final String target;

    @Getter
    private int distance;
    @Getter
    private int length;
    @Getter
    private Set<DerivationPathNode> derivation;

    public NearestShortestDerivation(final String target){
        this.target=target;
        distance=length=Integer.MAX_VALUE;
        //compare by string representation, as all the derivations in this set
        //will have the same distance to the target (i.e. the minimum distance).
        derivation=new TreeSet<>(Comparator.comparing(Object::toString));
    }

    @Override
    public void accept(DerivationPathNode n) {
        final int newDistance = LevenshteinDistance.getDefaultInstance().apply(n.get(), target);
        if (newDistance>distance || (newDistance==distance && n.length()>length))
            return;
        if (newDistance<distance || n.length()<length)
            derivation.clear();
        distance=newDistance;
        length=n.length();
        derivation.add(n);
    }
}
