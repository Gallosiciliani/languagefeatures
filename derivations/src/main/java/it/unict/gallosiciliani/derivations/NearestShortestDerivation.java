package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Given a target (nicosian) lexema, accepts derivations but save
 * only the nearest and shortest ones
 */
@Slf4j
public class NearestShortestDerivation implements DerivationsToTargetContainer {

    @Getter
    private final String target;
    @Getter
    private final Collection<DerivationPathNode> derivation;

    @Getter
    private int distance;
    private int length;

    public NearestShortestDerivation(final String target){
        this.target=target;
        distance=length=Integer.MAX_VALUE;
        //compare by string representation, as all the derivations in this set
        //will have the same distance to the target (i.e. the minimum distance).
        derivation=new LinkedList<>();
    }

    @Override
    public void accept(final TargetedDerivation d) {
        if (d.getDistance()>distance || d.getDistance()==distance && d.getDerivation().length()>length)
            return;
        if (d.getDistance()<distance || d.getDerivation().length()<length)
            derivation.clear();
        distance=d.getDistance();
        length=d.getDerivation().length();
        derivation.add(d.getDerivation());
    }
}
