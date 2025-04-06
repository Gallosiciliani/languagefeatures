package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.util.OntologyLoader;

import java.io.IOException;

/**
 * Load data in the abox file
 *
 * @author Cristiano Longo
 */
public class ABox extends OntologyLoader {
    ABox() throws IOException {
        super("nicosiaesperlinga.ttl");
    }

}
