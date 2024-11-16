/*
 * JOPA Examples
 * Copyright (C) 2023 Czech Technical University in Prague
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package it.unict.gallosiciliani.webapp.persistence;

import com.github.ledsoft.jopa.spring.transaction.DelegatingEntityManager;
import com.github.ledsoft.jopa.spring.transaction.JopaTransactionManager;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableTransactionManagement
public class PersistenceConfig {

    @Bean
    public EntityManagerFactory entityManagerFactory(final WebAppProperties properties){
        log.info("Using repository path: {}.", properties.getPersistence().getOntologyPhysicalURIKey());
        log.info("Storage type: {}", properties.getPersistence().getJenaStorageType());

        final Map<String, String> props = new HashMap<>();
        //props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, properties.getNs().toString());
        // Here we set up basic storage access properties - driver class, physical location of the storage
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, properties.getPersistence().getOntologyPhysicalURIKey());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName());
        // Let's use Jena TDB for storage
        props.put(JenaOntoDriverProperties.JENA_STORAGE_TYPE, properties.getPersistence().getJenaStorageType());
        props.put(JenaOntoDriverProperties.JENA_ISOLATION_STRATEGY, JenaOntoDriverProperties.SNAPSHOT);
        // Use Jena's rule-based RDFS reasoner
        //props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OWLFBRuleReasonerFactoryWithTbox.class.getName());
        // View transactional changes during transaction
        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        // Where to look for entities
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.model");
        // Ontology language
        //props.put(JOPAPersistenceProperties.LANG, "en");
        // Persistence provider name
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());

        return Persistence.createEntityManagerFactory("jopaExample08PU", props);
    }

    @Bean
    public DelegatingEntityManager entityManager() {
        return new DelegatingEntityManager();
    }

    @Bean(name = "txManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf,
                                                         DelegatingEntityManager emProxy) {
        return new JopaTransactionManager(emf, emProxy);
    }
}
