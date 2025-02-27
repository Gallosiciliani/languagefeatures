package it.unict.gallosiciliani.sicilian;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.model.lemon.lime.Lime;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.Ontolex;
import it.unict.gallosiciliani.model.persistence.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.model.persistence.InMemoryEntityManagerFactoryHelper;
import it.unict.gallosiciliani.util.OntologyLoader;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Helper class for the ontology of nouns and verbs in the Gallo-Italic variety spoken in Nicosia and Sperlinga.
 */
public class NicosiaESperlinga extends OntologyLoader {
    public static final String NS="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#";
    public static final String LEXICON_IRI=NS+"lexicon";
    private final EntityManagerFactoryHelper emFactory;
    private final EntityManager entityManager;


    NicosiaESperlinga() throws IOException {
        super("nicosiaesperlinga.ttl");
        emFactory=new InMemoryEntityManagerFactoryHelper();
        entityManager=emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.unwrap(Dataset.class).getDefaultModel().add(super.getModel());
        entityManager.flush();
        entityManager.getTransaction().commit();
        super.getModel().close();
    }

    @Override
    public Model getModel(){
        return entityManager.unwrap(Dataset.class).getDefaultModel();
    }

    @Override
    public void close() {
        super.close();
        emFactory.close();
    }

    Stream<Form> getAllForms(){
        final String formsQuery="SELECT ?f WHERE {\n"+
                "\t<"+LEXICON_IRI+"> <"+Lime.ENTRY_OBJ_PROPERTY+"> ?e. \n"+
                "\t?e <"+Ontolex.CANONICAL_FORM_OBJ_PROPERTY+"> ?f .\n"+
                "}";
        return entityManager.createNativeQuery(formsQuery, Form.class).getResultStream();
    }
}
