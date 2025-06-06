package it.unict.gallosiciliani.webapp.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import it.unict.gallosiciliani.webapp.ontologies.TBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.ledsoft.jopa.loader.BootAwareClasspathScanner;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class PersistenceConfig {

    @Bean
    EntityManagerFactory entityManagerFactory(final WebAppProperties appProps){

        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, "https://gallosiciliani.unict.it/ns/lexica");
        // Here we set up basic storage access properties - driver class, physical location of the storage
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "nicosiaesperlinga-deriv.ttl");
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName());
        // Let's use Jena TDB for storage
        props.put(JenaOntoDriverProperties.JENA_STORAGE_TYPE, appProps.getJenaStorageType());

        //props.put(JenaOntoDriverProperties.JENA_ISOLATION_STRATEGY, JenaOntoDriverProperties.READ_COMMITTED);
        // Use Jena's rule-based RDFS reasoner
        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, ReasonerFactoryWithTbox.class.getName());
        // View transactional changes during transaction
        //props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        // Where to look for entities
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.liph.model");
        // Ontology language
        //props.put(JOPAPersistenceProperties.LANG, "en");
        // Persistence provider name
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        // see https://github.com/kbss-cvut/jopa/wiki/Spring-and-Spring-Boot
        props.put(JOPAPersistenceProperties.CLASSPATH_SCANNER_CLASS, BootAwareClasspathScanner.class.getName());
        return Persistence.createEntityManagerFactory("gallosiciliani", props);
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager(final EntityManagerFactory entityManagerFactory,
                                       final TBox tbox) {
        ReasonerFactoryWithTbox.theInstance().setTBox(tbox);
        return entityManagerFactory.createEntityManager();
    }
}
