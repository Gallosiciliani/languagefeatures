package it.unict.gallosiciliani.webapp.lexica;

import lombok.Data;

/**
 * Represent a page of the lexicon in the paging mechanism used to visit the
 * lexicon.
 */
@Data
public class LexiconPageSelector {

    public LexiconPageSelector(){
        //intentionally empty
    }

    public LexiconPageSelector(final String label, final String selector){
        this.label=label;
        this.selector=selector;
    }

    /**
     * Label indicating the page in the page selector
     */
    private String label;

    /**
     * A regular expression indicating all the lemmas that belong to this page
     */
    private String selector;
}
