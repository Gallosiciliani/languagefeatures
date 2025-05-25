package it.unict.gallosiciliani.liph.util;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.*;
import java.util.function.Consumer;

/**
 * Implementation of {@link it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever} assuming that
 * labels are reported using the rdfs:label property
 */
public class LinguisticPhenomenonByLabelRetrieverImpl implements LinguisticPhenomenonByLabelRetriever, Consumer<LinguisticPhenomenon> {
    private final Map<String, LinguisticPhenomenon> lpByLabel=new TreeMap<>();

    /**
     * Factory method that build a retriever for the specified phenomena
     * @param phenomena a set of phenomena which had to be recognized by the produced retriever
     * @return a retriever for the specified phenomena
     */
    public static LinguisticPhenomenonByLabelRetriever build(final List<LinguisticPhenomenon> phenomena){
        final LinguisticPhenomenonByLabelRetrieverImpl r=new LinguisticPhenomenonByLabelRetrieverImpl();
        phenomena.forEach(r);
        return r;
    }

    @Override
    public LinguisticPhenomenon getByLabel(String label, Locale locale) {
        return lpByLabel.get(label);
    }

    @Override
    public void accept(LinguisticPhenomenon linguisticPhenomenon) {
        if (lpByLabel.put(linguisticPhenomenon.getLabel(), linguisticPhenomenon)!=null)
            throw new IllegalArgumentException("Duplicate phenomenon with label "+linguisticPhenomenon.getLabel());
    }
}
