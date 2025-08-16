package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.*;

/**
 * Given a derivation, get all the phenomena that were eligible to be applied bus have not.
 * A phenomenon is said to be eligible for a lexical expression if it would produce any change when applied to the expression.

 * @author Cristiano Longo.
 */
public class MissedPhenomenaFinder {
    private final List<LinguisticPhenomenon> eligiblePhenomena;

    public MissedPhenomenaFinder(final List<LinguisticPhenomenon> eligiblePhenomena){
        this.eligiblePhenomena=eligiblePhenomena;
    }

    /**
     *
     * @param n derivation
     * @return missed phenomena
     */
    public DerivationPhenomena getMissedPhenomena(final DerivationPathNode n){
        return getMissedPhenomena(n,LinguisticPhenomena.COMPARATOR_BY_IRI);
    }

    /**
     * @param n derivation
     * @return missed phenomena
     */
    public DerivationPhenomena getMissedPhenomena(final DerivationPathNode n, final Comparator<LinguisticPhenomenon> sorter){
        final Set<LinguisticPhenomenon> performedPhenomena=new TreeSet<>(sorter);
        final String etymon=traverse(n, performedPhenomena);
        final SortedSet<LinguisticPhenomenon> expected=new TreeSet<>(sorter);
        final SortedSet<LinguisticPhenomenon> missed=new TreeSet<>(sorter);
        eligiblePhenomena.forEach((p)->{
            if (!p.apply(etymon).isEmpty()){
                expected.add(p);
                if (!performedPhenomena.contains(p))
                    missed.add(p);
            }
        });
        return new DerivationPhenomena(){
            @Override
            public Set<LinguisticPhenomenon> getSuitablePhenomena() {
                return expected;
            }

            @Override
            public SortedSet<LinguisticPhenomenon> getMissedPhenomena() {
                return missed;
            }
        };
    }

    /**
     * Traverse a derivation to find all phenomena and the etymon
     * @param n the derivation
     * @param performedPhenomena return parameters where all the phenomena will be added
     * @return the etymon
     */
    private String traverse(final DerivationPathNode n, final Collection<LinguisticPhenomenon> performedPhenomena){
        if (n.prev()==null)
            return n.get();
        performedPhenomena.add(n.getLinguisticPhenomenon());
        return traverse(n.prev(), performedPhenomena);
    }
}
