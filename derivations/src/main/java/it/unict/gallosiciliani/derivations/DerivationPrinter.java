package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;

import java.util.Locale;

/**
 * Helper class to print derivations in human-readable form
 *
 * @author Cristiano Longo
 */
public class DerivationPrinter {
    private final LinguisticPhenomenonLabelProvider labelProvider;

    public DerivationPrinter(final LinguisticPhenomenonLabelProvider labelProvider){
        this.labelProvider=labelProvider;
    }

    /**
     * Print the derivation ending with the given node
     * @param n the {@link DerivationPathNode}
     * @param locale locale for linguistic phenomena labels
     * @return string representing all the path ending in this node
     */
    public String print(final DerivationPathNode n, final Locale locale){
        if (n.prev()==null)
            return n.get();
        return n.get()+"<-"+labelProvider.getLabel(n.getLinguisticPhenomenon(), locale)+"--"+ print(n.prev(), locale);
    }

}
