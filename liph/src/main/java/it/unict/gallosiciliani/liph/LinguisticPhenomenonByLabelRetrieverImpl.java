package it.unict.gallosiciliani.liph;

import java.util.Collection;
import java.util.Locale;

/**
 * Provide a set of linguistic phenomena by labels
 *
 * @author Cristiano Longo
 */
public class LinguisticPhenomenonByLabelRetrieverImpl implements LinguisticPhenomenonByLabelRetriever {
    private final Collection<? extends LinguisticPhenomenon> recognizedPhenomena;
    private final LinguisticPhenomenonLabelProvider labelProvider;

    public LinguisticPhenomenonByLabelRetrieverImpl(final Collection<? extends LinguisticPhenomenon> recognizedPhenomena,
                                                    final LinguisticPhenomenonLabelProvider labelProvider){
        this.recognizedPhenomena=recognizedPhenomena;
        this.labelProvider=labelProvider;
    }

    @Override
    public LinguisticPhenomenon getByLabel(final String label, final Locale locale){
        for(final LinguisticPhenomenon p: recognizedPhenomena)
            if (labelProvider.getLabel(p, locale).equals(label))
                return p;
        throw new IllegalArgumentException("Not recognized phenomenon label "+label);
    }
}
