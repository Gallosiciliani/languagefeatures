package it.unict.gallosiciliani.derivations.io;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;

import java.util.Locale;

/**
 * Parse a derivation string produced by {@link DerivationIOUtil}
 */
public class DerivationParser {
    private final String phenomenonStartMarker;
    private final String phenomenonEndMarker;
    private final LinguisticPhenomenonByLabelRetriever phenomenonRetriever;

    DerivationParser(final String phenomenonStartMarker,
        final String phenomenonEndMarker,
        final LinguisticPhenomenonByLabelRetriever phenomenonRetriever){
        this.phenomenonStartMarker=phenomenonStartMarker;
        this.phenomenonEndMarker=phenomenonEndMarker;
        this.phenomenonRetriever=phenomenonRetriever;
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
        final LinguisticPhenomenon phenomenon=phenomenonRetriever.getByLabel(partPieces[1], locale);
        return new DerivationPathNodeImpl(lexicalExpression, parse(parts, i+1, locale), phenomenon);
    }
}
