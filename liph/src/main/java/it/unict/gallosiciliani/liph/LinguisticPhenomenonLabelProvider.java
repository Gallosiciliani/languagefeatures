package it.unict.gallosiciliani.liph;

import java.util.Locale;

/**
 * Provide labels for linguistic phenomena. It may apply to just a particular
 * set of linguistic phenomena.
 */
public interface LinguisticPhenomenonLabelProvider {
    /**
     * Get the label for the specified phenomenon
     * @param linguisticPhenomenon the phenomenon
     * @param locale label local
     * @return the label associated to the phenomenon
     * @throws IllegalArgumentException if this provider does not apply to the given phenomenon
     */
    String getLabel(final LinguisticPhenomenon linguisticPhenomenon, final Locale locale);
}
