package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;

public class BruteForceDerivationBuilder implements DerivationBuilder{

    public static final DerivationBuilderFactory FACTORY=new DerivationBuilderFactory() {
        @Override
        public DerivationBuilder build(List<? extends LinguisticPhenomenon> phenomena, List<NearestShortestDerivation> targets) {
            return new BruteForceDerivationBuilder(phenomena, targets);
        }
    };
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final List<NearestShortestDerivation> targets;

    public BruteForceDerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, final List<NearestShortestDerivation> targets){
        this.phenomena=phenomena;
        this.targets=targets;
    }

    @Override
    public void apply(final String src){
        final DerivationPathNode currentDerivation=new DerivationPathNodeImpl(src);
        System.out.println(apply(currentDerivation, 0)+" derivations for "+src);
    }

    /**
     *
     * @param currentDerivation
     * @param currentPhenomenaIndex
     * @return number of leafs of the derivation tree rooted in the current derivation
     */
    private int apply(final DerivationPathNode currentDerivation, final int currentPhenomenaIndex) {
        if (currentPhenomenaIndex==phenomena.size()){
            submitToTargets(currentDerivation);
            return 1;
        }


        //current phenomenon not applied
        int leafs=apply(currentDerivation, currentPhenomenaIndex+1);
        //current phenomenon applied
        final LinguisticPhenomenon currentPhenomenon= phenomena.get(currentPhenomenaIndex);
        for(final String derivedString : currentPhenomenon.apply(currentDerivation.get())){
            final DerivationPathNode newDerivation=new DerivationPathNodeImpl(derivedString, currentDerivation, currentPhenomenon);
            leafs+=apply(newDerivation, currentPhenomenaIndex+1);
        }
        return leafs;
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
