package it.unict.gallosiciliani.webapp.persistence;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;

/**
 * Provide both {@link LinguisticPhenomena} and {@link GSFeatures} ontologies in a single model
 * @author Cristiano Longo
 */
public class TBox {
    public final LinguisticPhenomena linguisticPhenomena;
    public final GSFeatures gsFeatures;

    //where Language Features and GSKB Featrues ontologies are merged
    public final Model all;

    public TBox() throws IOException {
        linguisticPhenomena = new LinguisticPhenomena();
        gsFeatures = GSFeatures.loadLocal();
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
