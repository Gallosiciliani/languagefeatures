package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import java.io.IOException;
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
     * @return number of lemmas with an etymon
     * @throws IOException if unable to write to the output stream
     */
    public int write(final Appendable out, final LinguisticPhenomenonLabelProvider phenomenonLabelProvider, final Locale locale) throws IOException {
        final DerivationPrinter derivationPrinter=new DerivationPrinter(phenomenonLabelProvider);
        int n=0;
        for(final ShortestDerivation entry: lemmaToDerivations.values()) {
            if (entry.getDerivation().isEmpty())
                continue;
            n++;
            for (final DerivationPathNode d : entry.getDerivation())
                out.append(derivationPrinter.print(d, locale)).append("\n");
        }
        return n;
    }

}
