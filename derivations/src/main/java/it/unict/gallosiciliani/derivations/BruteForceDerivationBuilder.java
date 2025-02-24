package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;

public class BruteForceDerivationBuilder implements DerivationBuilder{
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final List<NearestShortestDerivation> targets;

    public BruteForceDerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, final List<NearestShortestDerivation> targets){
        this.phenomena=phenomena;
        this.targets=targets;
    }

    @Override
    public void apply(final String src){
        final DerivationPathNode currentDerivation=new DerivationPathNodeImpl(src);
        apply(currentDerivation, 0);
    }

    private void apply(final DerivationPathNode currentDerivation, final int currentPhenomenaIndex) {
        if (currentPhenomenaIndex==phenomena.size()){
            submitToTargets(currentDerivation);
            return;
        }

        //current phenomenon not applied
        apply(currentDerivation, currentPhenomenaIndex+1);
        //current phenomenon applied
        final LinguisticPhenomenon currentPhenomenon= phenomena.get(currentPhenomenaIndex);
        for(final String derivedString : currentPhenomenon.apply(currentDerivation.get())){
            final DerivationPathNode newDerivation=new DerivationPathNodeImpl(derivedString, currentDerivation, currentPhenomenon);
            apply(newDerivation, currentPhenomenaIndex+1);
        }
    }

    private void submitToTargets(final DerivationPathNode derivation){
        targets.forEach((t)->{
            final int distance= LevenshteinDistance.getDefaultInstance().apply(t.getTarget(), derivation.get());
            final TargetedDerivation d=new TargetedDerivation() {
                @Override
                public DerivationPathNode getDerivation() {
                    return derivation;
                }

                @Override
                public int getDistance() {
                    return distance;
                }
            };
            t.accept(d);
        });
    }
}
