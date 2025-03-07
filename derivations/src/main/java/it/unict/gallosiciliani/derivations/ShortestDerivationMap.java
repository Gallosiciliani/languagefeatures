package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * @author Cristiano Longo
 */
public class ShortestDerivationMap implements Consumer<DerivationPathNode> {
    private final Map<String, ShortestDerivation> lemmaToDerivations=new TreeMap<>();

    public ShortestDerivationMap(final List<String> expectedTargets){
        expectedTargets.forEach((lemma) -> lemmaToDerivations.put(lemma, new ShortestDerivation()));
    }

    @Override
    public void accept(final DerivationPathNode d) {
        final ShortestDerivation derivationsForLemma=lemmaToDerivations.get(d.get());
        if (derivationsForLemma!=null)
            derivationsForLemma.accept(d);
    }

    /**
     * write a set of rows representing the current derivations for the target lemmas.
     *
     * @param out                     the output stream
     * @param phenomenonLabelProvider to print phenomena
     * @param locale    locale
     * @throws IOException if unable to write to the output stream
     */
    public void write(final Appendable out, final LinguisticPhenomenonLabelProvider phenomenonLabelProvider, final Locale locale) throws IOException {
        final DerivationPrinter derivationPrinter=new DerivationPrinter(phenomenonLabelProvider);
        for(final ShortestDerivation entry: lemmaToDerivations.values())
            for(final DerivationPathNode d: entry.getDerivation())
                derivationPrinter.print(d,locale);
    }

}
