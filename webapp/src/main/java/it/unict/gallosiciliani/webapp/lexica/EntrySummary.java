package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.model.lemon.ontolex.Form;

import java.net.URI;
import java.util.SortedSet;

/**
 * Model object to display a list of entries
 */
public interface EntrySummary {

    /**
     * Describe a subcomponent (or the whole) of the lating etymon
     */
    interface EtymonComponent{

        /**
         * The string representing the component (may be with '-' to indicate that it is a prefix or a suffix)
         * @return human readable representation of the component
         */
        String getName();

        /**
         * Provide a link to an external lexical resource related to this component
         * @return a URI representing a resource about this component, or null
         */
        URI getExternalLink();
    }

    /**
     * Written representation of the canonical form of the entry (i.e. the lemma)
     * @return the lemma
     */
    String getWrittenRep();

    /**
     * Get the label to show the part of speech
     * @return part of speech
     */
    String getPartOfSpeech();


    /**
     * Components of the latin etymon. May be there is just one component, if the etymon is simple.
     * May be there are no components at all in the case the etymon is not available.
     *
     * @return SortedSet of {@link Form}
     */
    SortedSet<Form> getLatinEtymon();

    /**
     * Labels representing the phonetic features of this entry
     *
     * @return labels of phonetic features
     */
    SortedSet<String> getPhoneticFeatureLabels();
}