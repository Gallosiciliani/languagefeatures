package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BruteForceDerivationBuilder implements DerivationBuilder{

    private final List<? extends LinguisticPhenomenon> phenomena;
    private final ShortestDerivationMap targets;

    public BruteForceDerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, final List<String> targets){
        this.phenomena=phenomena;
        this.targets=new ShortestDerivationMap(targets);
    }

    @Override
    public void apply(final String src){
        final DerivationPathNode currentDerivation=new DerivationPathNodeImpl(src);
        System.out.println(apply(currentDerivation, 0)+" derivations for "+src);
    }

    /**
     *
     * @param currentDerivation derivation branch
     * @param currentPhenomenaIndex next phenomenon
     * @return number of leafs of the derivation tree rooted in the current derivation
     */
    private int apply(final DerivationPathNode currentDerivation, final int currentPhenomenaIndex) {
        if (currentPhenomenaIndex==phenomena.size()){
            targets.accept(currentDerivation);
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

    /**
     * write a set of rows representing the current derivations for the target lemmas.
     *
     * @param out                     the output stream
     * @param phenomenonLabelProvider to print phenomena
     * @param locale locale
     * @throws IOException if unable to write to the output stream
     */
    public int write(final Appendable out, final LinguisticPhenomenonLabelProvider phenomenonLabelProvider, final Locale locale) throws IOException {
        return targets.write(out, phenomenonLabelProvider, locale);
    }
}
