package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Provide both {@link LinguisticPhenomena} and {@link GSFeatures} ontologies in a single model
 * @author Cristiano Longo
 */
public class TBox {

    //where Language Features and GSKB Featrues ontologies are merged
    public final Model all;

    public TBox(final LinguisticPhenomena linguisticPhenomena, final GSFeatures gsFeatures) {
        OntDocumentManager.getInstance().addModel(GSFeatures.IRI, gsFeatures.getModel());
        all=createMergeOntology(linguisticPhenomena.getModel(), gsFeatures.getModel());
    }

    private Model createMergeOntology(final Model a, final Model b){
        final Model o = ModelFactory.createOntologyModel();
        o.add(a);
        o.add(b);

        //TODO replace existing OWL ontology individuals with an appropriate one
        return o;
    }
}
