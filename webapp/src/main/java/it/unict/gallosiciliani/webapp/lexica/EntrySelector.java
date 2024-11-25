package it.unict.gallosiciliani.webapp.lexica;

import lombok.Data;

/**
 * Selection criteria for lexical entries
 */
@Data
public class EntrySelector {

    public static EntrySelector ALL = new EntrySelector();

    /**
     * Value meaning that the selection does not apply
     */
    public static final String ANY_SELECTOR = "any";

    /**
     * Select entries by part of speech
     */
    private String pos = ANY_SELECTOR;

    /**
     * Select entries with some features of the specified type
     */
    private String featureType = ANY_SELECTOR;

    /**
     * Page number. It refers to pages in application.properties and
     * stored as {@link LexiconPageSelector} instances.
     */
    private int page = 0;
}
