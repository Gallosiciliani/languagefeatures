package it.unict.gallosiciliani.importing.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide {@link EntityManager} instances
 */
public class EntityManagerFactoryHelper implements AutoCloseable{

    private final EntityManagerFactory factory;

    protected EntityManagerFactoryHelper(final Map<String,String> storageSpecificConfiguration){
        final Map<String, String> configuration=basicConfiguration();
        configuration.putAll(storageSpecificConfiguration);
        factory=Persistence.createEntityManagerFactory("the-persistence-unit", configuration);

    }

    public EntityManager createEntityManager(){
        return factory.createEntityManager();
    }

    @Override
    public final void close(){
        factory.close();
    }

    /**
     * Configuration properties, excluding storage type and storage file name
     * @return basic configuration properties
     */
    private static Map<String,String> basicConfiguration(){
        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.model");
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName());
//        // View transactional changes during transaction
//        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());

        return props;
    }
}
