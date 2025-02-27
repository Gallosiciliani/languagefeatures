package it.unict.gallosiciliani.model.persistence;

import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Create {@link cz.cvut.kbss.jopa.model.EntityManager} instances which persist the ontology on a file
 */
@Slf4j
public class FileEntityManagerFactoryHelper extends EntityManagerFactoryHelper {

    /**
     *
     * @param filePath path of the file where the ontology will be stored
     */
    public FileEntityManagerFactoryHelper(final String filePath){
        super(Map.of(JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.FILE,
                JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, filePath));
        log.info("Created factory for file {}", filePath);
    }
}
