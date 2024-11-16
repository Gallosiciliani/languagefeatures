package it.unict.gallosiciliani.webapp.persistence;


import lombok.Data;

/**
 * Persistence-specific configuration parameters
 * See <a href="https://kbss.felk.cvut.cz/jenkins/job/jopa-stable/javadoc/cz/cvut/kbss/jopa/model/JOPAPersistenceProperties.html">JOPAPersistenceProperties</a>
 * and <a href="https://kbss.felk.cvut.cz/jenkins/job/jopa-stable/javadoc/cz/cvut/kbss/ontodriver/jena/config/JenaOntoDriverProperties.html">JenaOntoDriverProperties</a>
 */
@Data
public class PersistenceProperties {
    private String ontologyPhysicalURIKey;

    // Available storage types using the {@link cz.cvut.kbss.ontodriver.jena.JenaDataSource}
    private String jenaStorageType;
}
