package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

/**
 * Provide ontologies as HTML
 */
@Controller
@RequestMapping("/ns/")
public class OntologiesHTMLController {

    @Autowired
    GSFeatures gsFeatures;

    @GetMapping("gs-features")
    String getGsFeatures(final Model model){
        model.addAttribute("title", gsFeatures.getName());
        model.addAttribute("phenomena", gsFeatures.getRegexLinguisticPhenomena());
        return "ontologies/viewGSFeatures.html";
    }
}
