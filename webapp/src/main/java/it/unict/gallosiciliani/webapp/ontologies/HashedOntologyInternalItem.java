package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.liph.util.OntologyItem;
import lombok.Getter;

/**
 * {@link OntologyItem} belonging to an ontology which uses hashed IRIs
 */
public class HashedOntologyInternalItem implements OntologyItem{
    private final OntologyItem src;
    /**
     * Fragment identifier of the IRI, assuming that the object is in the
     * ontology namespace.
     */
    @Getter
    private final String id;

    /**
     *
     * @param src the ontology item
     * @param ns ontology namespace
     */
    public HashedOntologyInternalItem(final OntologyItem src, final String ns){
        this.src=src;
        this.id =src.getIri().substring(ns.length());
    }

    @Override
    public String getIri() {
        return src.getIri();
    }

    @Override
    public String getLabel() {
        return src.getLabel();
    }

    @Override
    public String getComment() {
        return src.getComment();
    }
}
