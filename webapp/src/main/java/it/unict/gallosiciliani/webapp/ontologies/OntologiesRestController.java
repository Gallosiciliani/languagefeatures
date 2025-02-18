package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.webapp.persistence.GSABox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

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
    GSABox abox;

    /**
     * Provide the base ontology to define language features.
     * @return the ontology in TTL format
     */
    @GetMapping(value = "liph", produces = "text/turtle")
    public String getLanguageFeaturesOntology(){
        return liph.getOntologyAsStr();
    }

    /**
     * Provide the ontology with features defined in the scope of the Galloitalici project
     * @return the ontology in TTL format
     */
    @GetMapping(value = "gs-features", produces = "text/turtle")
    public String getGSFeaturesOntology(){
        return gsFeatures.getOntologyAsStr();
    }

    /**
     * Provide a ttl version of the data stored in the ABox
     * @return ABox in TTL format
     */
    @GetMapping(value = {"lexica/nicosiaesperlinga", "lexica", "lexica/"}, produces = "text/turtle")
    public String getAbox(){
        return abox.getOntologyAsStr();
    }

    /**
     * Redirect to the web page of lexica when the requested mime type is HTML
     * @return ABox in TTL format
     */
    @GetMapping(value = {"lexica/", "lexica", "lexica/nicosiaesperlinga"}, produces = "text/html")
    public RedirectView redirectAboxHTML(){
        return new RedirectView("/lexica/");
    }
}
