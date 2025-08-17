package it.unict.gallosiciliani.webapp.workflow;

import it.unict.gallosiciliani.liph.util.OntologyLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The ontology describing the workflow
 */
@Component
public class Workflow extends OntologyLoader {
    public Workflow() throws IOException {
        super("workflow/workflow.ttl", "https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga/workflow");
    }
}
