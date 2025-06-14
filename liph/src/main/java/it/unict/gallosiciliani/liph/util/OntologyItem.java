package it.unict.gallosiciliani.liph.util;

import lombok.Getter;

/**
 * A generic ontology item, may be a class, a property or an individual
 */
@Getter
public class OntologyItem {

    private final String iri;
    private final String label;
    private final String comment;

    public OntologyItem(final String iri, final String label, final String comment){
        this.iri=iri;
        this.label=label;
        this.comment=comment;
    }
}
