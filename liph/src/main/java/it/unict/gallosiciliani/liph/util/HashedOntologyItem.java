package it.unict.gallosiciliani.liph.util;

/**
 * {@link OntologyItem} belonging to an ontology which uses hashed IRIs
 *
 * @author Cristiano Longo
 */
public abstract class HashedOntologyItem implements OntologyItem{

    private final String id;
    private final String iri;

    protected HashedOntologyItem(final String iri, final String ontologyNS){
            this.id=iri.substring(ontologyNS.length());
            this.iri=iri;
    }

    /**
     * Fragment identifier of the IRI, assuming that the object is in the
     * ontology namespace.
     */
    public final String getId(){
        return id;
    }

    @Override
    public final String getIri(){
        return iri;
    }
}
