package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.util.OntologyItem;
import it.unict.gallosiciliani.projects.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.ArrayList;
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

    @GetMapping("liph")
    String getLiph(final Model model){
        model.addAttribute("title", liph.getName());
        model.addAttribute("comment", liph.getComment());
        model.addAttribute("classes", asInternalItems(liph.getClasses(), liph.getNamespace()));
        model.addAttribute("objproperties", asInternalItems(liph.getObjProperties(), liph.getNamespace()));
        model.addAttribute("dataproperties", asInternalItems(liph.getDataProperties(), liph.getNamespace()));
        return "ontologies/viewLiph.html";
    }

    private List<HashedOntologyInternalItem> asInternalItems(final List<OntologyItem> items, final String ontologyNS){
        final List<HashedOntologyInternalItem> res=new ArrayList<>(items.size());
        for(final OntologyItem i : items)
            res.add(new HashedOntologyInternalItem(i, ontologyNS));
        return res;
//        return items.stream().map((i)->{return new HashedOntologyInternalItem(i, ontologyNS);}).toList();
    }

    @GetMapping("gs-features")
    String getGsFeatures(final Model model){
        model.addAttribute("title", gsFeatures.getName());
        model.addAttribute("phenomena", gsFeatures.getRegexLinguisticPhenomena());
        return "ontologies/viewGSFeatures.html";
    }

    @GetMapping(value={"projects", "projects/", "projects/", "projects/gallosiciliani2023Project"})
    String getGallosiciliani2023Project(final Model model){
        model.addAttribute("project", projects.getGallosiciliani2023Project());
        //we need the following to sort the project results
        model.addAttribute("results", List.of(projects.getLiph(), projects.getGsFeatures(), projects.getNicosiaesperlinga()));
        return "ontologies/viewGallosiciliani2023Project.html";
    }
}
