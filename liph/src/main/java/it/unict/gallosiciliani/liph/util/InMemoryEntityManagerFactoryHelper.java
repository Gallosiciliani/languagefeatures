package it.unict.gallosiciliani.liph.util;

import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Create an Entity Manager Factory which will use an in-memory jena model for tests.
 *
 * @author Cristiano Longo
 */
public class InMemoryEntityManagerFactoryHelper extends EntityManagerFactoryHelper {

    private static Map<String, String> getStorageSpecificConfiguration(final Map<String, String> additionalConfiguration) {
        final Map<String, String> c=new TreeMap<>();
        c.put(JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.IN_MEMORY);
        c.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "http://test.org/ontology");
        c.putAll(additionalConfiguration);
        return c;
    }

    public InMemoryEntityManagerFactoryHelper() {
        this(Collections.emptyMap());
    }

    public InMemoryEntityManagerFactoryHelper(final Map<String,String> additionalConfiguration) {
        super(getStorageSpecificConfiguration(additionalConfiguration));
    }

}
