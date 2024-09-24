package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Given a target (nicosian) lexema, accepts derivations but save
 * only the nearest ones
 */
public class NearestDerivation implements Consumer<DerivationPathNode> {

    private final String target;

    @Getter
    private int distance;
    @Getter
    private Set<DerivationPathNode> derivation;

    public NearestDerivation(final String target){
        this.target=target;
        distance=Integer.MAX_VALUE;
        //compare by string representation, as all the derivations in this set
        //will have the same distance to the target (i.e. the minimum distance).
        derivation=new TreeSet<>(Comparator.comparing(Object::toString));
    }

    @Override
    public void accept(DerivationPathNode n) {
        final int newDistance = LevenshteinDistance.getDefaultInstance().apply(n.get(), target);
        if (newDistance>distance)
            return;
        if (newDistance<distance)
            derivation.clear();
        distance=newDistance;
        derivation.add(n);
    }
}
