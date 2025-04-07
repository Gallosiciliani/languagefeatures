package it.unict.gallosiciliani.liph.util;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.apache.jena.query.QueryExecutionFactory.create;

/**
 * Check one or more conditions on an ontology
 */
public class OntologyCheckUtils {

    /**
     * Check using an ask query
     */
    private interface AskQueryCheckDescription{

        /**
         * Content of the where clause of the ask query to be used for the check
         * @return ask query
         */
        String getWhereClause();

        /**
         * Human readable description of the check
         * @return check label
         */
        String getLabel();
    }

    private final Model model;
    private final List<Callable<Boolean>> checks;
    private final List<Callable<Boolean>> failed;

    /**
     * Create an (empty) checker for the ontology provided by the
     * specified model.
     *
     * @param model model of the ontology
     */
    public OntologyCheckUtils(final Model model){
        this.model=model;
        checks=new LinkedList<>();
        failed=new LinkedList<>();
    }

    /**
     * Add a check based on a ask query
     * @param checkDescription description
     */
    private void add(final AskQueryCheckDescription checkDescription){
        final Query q = QueryFactory.create("ASK {"+checkDescription.getWhereClause()+"}");

        checks.add(new Callable<>() {
            @Override
            public Boolean call(){
                try (QueryExecution e = create(q, model)) {
                    return e.execAsk();
                }
            }

            public String toString(){
                return checkDescription.getLabel();
            }
        });
    }
    /**
     * Add a check for the existence of the specified object property in the ontology
     * @param objectPropertyIRI IRI of the object property to be checked
     * @return this object
     */
    public OntologyCheckUtils objectPropertyExists(final String objectPropertyIRI){
        add(new AskQueryCheckDescription() {
            @Override
            public String getWhereClause() {
                return "<"+objectPropertyIRI+"> <"+ RDF.type+"> <"+ OWL.ObjectProperty+">";
            }

            @Override
            public String getLabel() {
                return "Object property "+objectPropertyIRI+" exists";
            }
        });
        return this;
    }

    /**
     * Add a check for the existence of the specified annotation property in the ontology
     * @param annotationPropertyIRI IRI of the property which has to be searched
     * @return this object
     */
    public OntologyCheckUtils annotationPropertyExists(final String annotationPropertyIRI){
        add(new AskQueryCheckDescription() {
            @Override
            public String getWhereClause() {
                return "<"+annotationPropertyIRI+"> <"+ RDF.type+"> <"+ OWL.AnnotationProperty+">";
            }

            @Override
            public String getLabel() {
                return "Annotation property "+annotationPropertyIRI+" exists";
            }
        });
        return this;
    }

    /**
     * Add a check for the existence of the specified class in the ontology
     * @param classIRI IRI of the class to be searched
     */
    public OntologyCheckUtils classExists(final String classIRI){
        add(new AskQueryCheckDescription() {
            @Override
            public String getWhereClause() {
                return "<"+classIRI+"> <"+ RDF.type+"> <"+ OWL.Class+">";
            }

            @Override
            public String getLabel() {
                return "Class "+classIRI+"exists";
            }
        });
        return this;
    }

    /**
     * Perform all checks
     * @return true if all checks succeded, false otherwhise
     */
    public boolean check() throws Exception {
        failed.clear();
        for(Callable<Boolean> check : checks)
            if (!check.call())
                failed.add(check);
        return failed.isEmpty();
    }

    /**
     * Just concatenate descriptions of all the failed checks
     * @return a string enumerating all failed test separated by semicolon
     */
    public String getFailureMessage(){
        final StringBuilder msg = new StringBuilder();
        for(final Callable<Boolean> check: failed)
            msg.append(failed).append(";");
        return msg.toString();
    }
}
