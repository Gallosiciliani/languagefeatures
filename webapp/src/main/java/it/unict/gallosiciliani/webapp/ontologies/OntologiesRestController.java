package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.projects.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Provides the feature ontologies
 */
@RestController
@RequestMapping("/ns/")
public class OntologiesRestController {

    @Autowired
    LinguisticPhenomena liph;

    @Autowired
    GSFeatures gsFeatures;

    @Autowired
    ABox abox;

    @Autowired
    Projects projects;

    /**
     * Provide the base ontology to define language features.
     * @return the ontology in TTL format
     */
    @GetMapping(value = {"liph", "liph/ttl"}, produces = "text/turtle")
    public String getLanguageFeaturesOntology(){
        return liph.getOntologyAsStr();
    }

    /**
     * Provide the base ontology to define language features.
     * @return the ontology in TTL format
     */
    @GetMapping(value = "liph/"+LinguisticPhenomena.VERSION, produces = "text/turtle")
    public String getLanguageFeaturesOntologyWithVersion(){
        return getLanguageFeaturesOntology();
    }

    /**
     * Provide the previous version of the liph ontology.
     * @return the ontology in TTL format
     */
    @GetMapping(value = "liph/1.2.0", produces = "text/turtle")
    public RedirectView getLiph120(){
        return new RedirectView("https://raw.githubusercontent.com/Gallosiciliani/languagefeatures/refs/heads/1.12.0/liph/src/main/resources/liph.ttl");
    }

    /**
     * Provide the ontology with features defined in the scope of the Galloitalici project
     * @return the ontology in TTL format
     */
    @GetMapping(value = {"gs-features", "gs-features/ttl"}, produces = "text/turtle")
    public String getGSFeaturesOntology(){
        return gsFeatures.getOntologyAsStr();
    }

    /**
     * Provide the ontology with features defined in the scope of the Galloitalici project
     * @return the ontology in TTL format
     */
    @GetMapping(value = "gs-features/"+GSFeatures.VERSION, produces = "text/turtle")
    public String getGSFeaturesOntologyWithVersion(){
        return getGSFeaturesOntology();
    }

    /**
     * Provide a ttl version of the data stored in the ABox
     * @return ABox in TTL format
     */
    @GetMapping(value = {"lexica/nicosiaesperlinga", "lexica/nicosiaesperlinga/ttl", "lexica", "lexica/"}, produces = "text/turtle")
    public String getAbox(){
        return abox.getOntologyAsStr();
    }

    @GetMapping(value = {"projects", "projects/", "projects/gallosiciliani2023Project"}, produces = "text/turtle")
    public String getGalloSiciliani2023Project(){
        return projects.getOntologyAsStr();
    }
}
