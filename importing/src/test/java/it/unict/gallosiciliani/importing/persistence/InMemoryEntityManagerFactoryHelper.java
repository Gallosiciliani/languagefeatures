package it.unict.gallosiciliani.importing.persistence;

import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;

import java.util.Map;

/**
 * Create an Entity Manager Factory which will use an in-memory jena model for tests.
 *
 * @author Cristiano Longo
 */
public class InMemoryEntityManagerFactoryHelper extends EntityManagerFactoryHelper {

    public InMemoryEntityManagerFactoryHelper() {
        super(Map.of(JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.IN_MEMORY,
                JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "http://test.org/ontology"));
    }
}
