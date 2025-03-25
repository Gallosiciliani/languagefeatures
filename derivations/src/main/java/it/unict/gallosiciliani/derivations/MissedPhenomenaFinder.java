package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

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
    public Collection<LinguisticPhenomenon> getMissedPhenomena(final DerivationPathNode n){
        final Set<LinguisticPhenomenon> performedPhenomena=new TreeSet<>(LinguisticPhenomena.COMPARATOR_BY_IRI);
        final String etymon=traverse(n, performedPhenomena);
        final Stream<LinguisticPhenomenon> expectedPhenomena=eligiblePhenomena.stream().filter((p)->!p.apply(etymon).isEmpty());
        return expectedPhenomena.filter((p)->!performedPhenomena.contains(p)).toList();
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
