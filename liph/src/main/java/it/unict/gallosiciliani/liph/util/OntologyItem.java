package it.unict.gallosiciliani.liph.util;


/**
 * A generic ontology item, may be a class, a property or an individual
 */
public interface OntologyItem {

    /**
     * IRI characterizing the ontology item
     * @return ontology IRI
     */
    String getIri();

    /**
     * Item label
     * @return Item label
     */
    String getLabel();

    /**
     * Human readable item description
     * @return item comment
     */
    String getComment();
}
