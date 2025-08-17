package it.unict.gallosiciliani.projects;


import it.unict.gallosiciliani.liph.util.OntologyItem;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import it.unict.gallosiciliani.projects.model.eurio.Project;
import it.unict.gallosiciliani.projects.model.eurio.Result;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

/**
 * The ontology providing just information
 */
@Getter
public class Projects extends OntologyLoader {
    public static final String IRI = "https://gallosiciliani.unict.it/ns/projects";
    public static final String NS = IRI+"/";
    private final Project gallosiciliani2023Project;
    private final Result liph=new Result();
    private final Result gsFeatures=new Result();
    private final Result nicosiaesperlinga=new Result();

    /**
     * Create the ontology from the resource in the classpath
     *
     */
    public Projects() throws IOException {
        super("projects.ttl", IRI);
        gallosiciliani2023Project=createGallosiciliani2023Project();
    }

    /**
     * The gallosiciliani2023Project individual, as reported in the ttl file
     */
    private Project createGallosiciliani2023Project(){
        final Project p=new Project();
        p.setId(NS+"gallosiciliani2023Project");
        final OntologyItem pi=retrieve(p.getId());
        p.setLabel(pi.getLabel());
        p.setComment(pi.getComment());

        liph.setId("https://gallosiciliani.unict.it/ns/liph");
        gsFeatures.setId("https://gallosiciliani.unict.it/ns/gs-features");
        nicosiaesperlinga.setId("https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#lexicon");
        p.setHasResult(Set.of(liph, gsFeatures, nicosiaesperlinga));
        return p;
    }
}
