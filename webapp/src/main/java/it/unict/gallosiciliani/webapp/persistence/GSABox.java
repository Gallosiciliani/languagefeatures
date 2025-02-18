package it.unict.gallosiciliani.webapp.persistence;

import it.unict.gallosiciliani.util.OntologyLoader;

import java.io.IOException;

/**
 * Load all data
 *
 * @author Cristiano Longo
 */
public class GSABox extends OntologyLoader {
    GSABox() throws IOException {
        super("nicosiaesperlinga.ttl");
    }

}
