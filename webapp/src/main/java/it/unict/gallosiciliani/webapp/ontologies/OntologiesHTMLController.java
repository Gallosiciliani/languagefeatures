package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.projects.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Provide ontologies as HTML
 */
@Controller
@RequestMapping("/ns/")
public class OntologiesHTMLController {

    @Autowired
    LinguisticPhenomena liph;

    @Autowired
    GSFeatures gsFeatures;

    @Autowired
    Projects projects;

    @GetMapping(value = "liph", params = "!ttl", produces = "!text/turtle")
    String getLiph(final Model model){
        model.addAttribute("title", liph.getName());
        model.addAttribute("comment", liph.getComment());
        model.addAttribute("classes", liph.getClasses());
        model.addAttribute("objproperties", liph.getObjProperties());
        model.addAttribute("dataproperties", liph.getDataProperties());
        return "ontologies/viewLiph.html";
    }

    @GetMapping(value = "gs-features", params = "!ttl", produces = "!text/turtle")
    String getGsFeatures(final Model model){
        model.addAttribute("title", gsFeatures.getName());
        model.addAttribute("comment", gsFeatures.getComment());
        model.addAttribute("categories", gsFeatures.getCategories());
        return "ontologies/viewGSFeatures.html";
    }

    @GetMapping(value={"projects", "projects/", "projects/", "projects/gallosiciliani2023Project"}, params="!ttl", produces = "!text/turtle")
    String getGallosiciliani2023Project(final Model model){
        model.addAttribute("project", projects.getGallosiciliani2023Project());
        //we need the following to sort the project results
        model.addAttribute("results", List.of(projects.getLiph(), projects.getGsFeatures(), projects.getNicosiaesperlinga()));
        return "ontologies/viewGallosiciliani2023Project.html";
    }
}
