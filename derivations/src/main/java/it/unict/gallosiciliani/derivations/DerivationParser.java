package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;

import java.util.Collection;
import java.util.Locale;

/**
 * Parse a derivation string produced by {@link DerivationIOUtil}
 */
public class DerivationParser {
    private final String phenomenonStartMarker;
    private final String phenomenonEndMarker;
    private final LinguisticPhenomenonLabelProvider labelProvider;
    private final Collection<? extends LinguisticPhenomenon> availablePhenomena;

    DerivationParser(final String phenomenonStartMarker,
        final String phenomenonEndMarker,
        final LinguisticPhenomenonLabelProvider labelProvider,
        final Collection<? extends LinguisticPhenomenon> availablePhenomena){
        this.phenomenonStartMarker=phenomenonStartMarker;
        this.phenomenonEndMarker=phenomenonEndMarker;
        this.labelProvider=labelProvider;
        this.availablePhenomena=availablePhenomena;
    }

    /**
     * Parse a string representing a derivation
     *
     * @param derivationString   derivation string, as produced by print
     * @param locale             locale for linguistic phenomena labels
     * @return the derivation encoded in the string
     */
    public DerivationPathNode parse(final String derivationString, final Locale locale){
        final String[] parts=derivationString.split(phenomenonEndMarker);
        return parse(parts, 0, locale);
    }

    private DerivationPathNode parse(final String[] parts, final int i, final Locale locale){
        // a piece has the form <lexicalExpression><phenomenonStarterMark><phenomenonLabel>
        final String[] partPieces=parts[i].split(phenomenonStartMarker);
        if (partPieces.length==1)
            return new DerivationPathNodeImpl(partPieces[0]);
        //else, we assume there are just two part pieces, the former is the lexical expression an
        //the latter is the phenomenon label
        final String lexicalExpression=partPieces[0];
        final LinguisticPhenomenon phenomenon=getPhenomenonByLabel(partPieces[1], locale);
        return new DerivationPathNodeImpl(lexicalExpression, parse(parts, i+1, locale), phenomenon);
    }

    /**
     * Get the phenomenon with the specified label
     * @param label phenomenon label
     * @param locale label locale
     * @return the phenomenon with the specified label
     * @throws IllegalArgumentException if no phenomenon with the given label exists in availablePhenomena
     */
    private LinguisticPhenomenon getPhenomenonByLabel(final String label, final Locale locale) {
        for(final LinguisticPhenomenon p: availablePhenomena)
            if (labelProvider.getLabel(p, locale).equals(label))
                return p;
        throw new IllegalArgumentException("No phenomenon with label "+label);
    }
}
