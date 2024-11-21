package it.unict.gallosiciliani.webapp.lexica;

import lombok.Data;

/**
 * Application properties related to lexicon view pagination.
 *
 * @author Cristiano Longo
 */
@Data
public class PagingProperties {
    /**
     * All expected pages
     */
    private LexiconPageSelector[] pages;
}
