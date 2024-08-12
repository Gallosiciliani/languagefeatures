package it.unict.gallosiciliani;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

import static org.apache.jena.query.QueryExecutionFactory.create;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utilities to test ontologies loaded from local files
 * @author Cristiano Longo
 */
public class OntologyTestUtils {

    private final Model model;

    /**
     * Create utilities to test the ontology contained in the specified model
     * @param model the model which will be tested
     */
    public OntologyTestUtils(final Model model){
        this.model=model;
    }

    public void checkObjectPropertyExists(final String objectPropertyIRI){
        final Query q = QueryFactory.create("ASK {<"+objectPropertyIRI+"> <"+ RDF.type+"> <"+ OWL.ObjectProperty+">}");
        try (QueryExecution e = create(q, model)) {
            assertTrue(e.execAsk(), "Unable to get property "+objectPropertyIRI);
        }
    }

    public void checkAnnotationPropertyExists(final String objectPropertyIRI){
        final Query q = QueryFactory.create("ASK {<"+objectPropertyIRI+"> <"+ RDF.type+"> <"+ OWL.AnnotationProperty+">}");
        try (QueryExecution e = create(q, model)) {
            assertTrue(e.execAsk());
        }
    }

    public void checkClassExists(final String classIRI){
        final Query q = QueryFactory.create("ASK {<"+classIRI+"> <"+ RDF.type+"> <"+ OWL.Class+">}");
        try (QueryExecution e = create(q, model)) {
            assertTrue(e.execAsk());
        }
    }

}
