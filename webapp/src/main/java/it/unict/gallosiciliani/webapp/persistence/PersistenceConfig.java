package it.unict.gallosiciliani.webapp.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.ledsoft.jopa.loader.BootAwareClasspathScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class PersistenceConfig {

    @Bean
    GSABox abox() throws IOException {
        return new GSABox();
    }

    @Bean
    TBox tbox() throws IOException{
        return new TBox();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(){

        final Map<String, String> props = new HashMap<>();
        //props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, properties.getNs().toString());
        // Here we set up basic storage access properties - driver class, physical location of the storage
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "notrelevant");
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName());
        // Let's use Jena TDB for storage
        props.put(JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.IN_MEMORY);
        props.put(JenaOntoDriverProperties.JENA_ISOLATION_STRATEGY, JenaOntoDriverProperties.READ_COMMITTED);
        // Use Jena's rule-based RDFS reasoner
        //props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OWLFBRuleReasonerFactoryWithTbox.class.getName());
        //props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OWLFBRuleReasonerFactoryWithTbox.class.getName());
        // View transactional changes during transaction
        //props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        // Where to look for entities
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.model");
        // Ontology language
        //props.put(JOPAPersistenceProperties.LANG, "en");
        // Persistence provider name
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        // see https://github.com/kbss-cvut/jopa/wiki/Spring-and-Spring-Boot
        props.put(JOPAPersistenceProperties.CLASSPATH_SCANNER_CLASS, BootAwareClasspathScanner.class.getName());
        return Persistence.createEntityManagerFactory("gallosiciliani", props);
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager(final EntityManagerFactory factory, final WebAppProperties props,
                                       final TBox tbox, final GSABox abox) {
        OWLFBRuleReasonerFactoryWithTbox.theInstance().setTBox(tbox);
        final EntityManager m=factory.createEntityManager();

        if (props.isLoadData()) {
            m.getTransaction().begin();
            m.unwrap(Dataset.class).getDefaultModel().add(abox.getModel());
            m.flush();
            m.getTransaction().commit();
        }
        return m;
    }
}
