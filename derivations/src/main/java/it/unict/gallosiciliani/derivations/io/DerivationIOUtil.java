package it.unict.gallosiciliani.derivations.io;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;

import java.util.Locale;

/**
 * Helper class to print derivations in human-readable form
 *
 * @author Cristiano Longo
 */
public class DerivationIOUtil {
    public static final String PHENOMENON_START_MARKER="<-";
    public static final String PHENOMENON_END_MARKER="--";
    private final LinguisticPhenomenonLabelProvider labelProvider;

    public DerivationIOUtil(final LinguisticPhenomenonLabelProvider labelProvider){
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
        return n.get()+PHENOMENON_START_MARKER+labelProvider.getLabel(n.getLinguisticPhenomenon(), locale)+PHENOMENON_END_MARKER+ print(n.prev(), locale);
    }

    /**
     * Get a derivation parser which recognizes the specified phenomena
     * @param retriever to retrieve phenomena by label
     * @return a derivation parser
     */
    public DerivationParser getParser(final LinguisticPhenomenonByLabelRetriever retriever){
        return new DerivationParser(PHENOMENON_START_MARKER, PHENOMENON_END_MARKER, retriever);
    }
}
