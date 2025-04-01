package it.unict.gallosiciliani.liph;

import java.util.Locale;

/**
 * Provide a set of linguistic phenomena by label
 * @author Cristiano Longo
 */
public interface LinguisticPhenomenonByLabelRetriever {

    /**
     * Get a phenomenon with the specified label
     *
     * @param label phenomenon label
     * @param locale current locale
     * @return the phenomenon with the specified label
     */
    LinguisticPhenomenon getByLabel(final String label, final Locale locale);
}
