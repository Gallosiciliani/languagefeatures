package it.unict.gallosiciliani.webapp.workflow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Provide the workflow performed to generate the NicosiaESperling ontology
 */
@Controller
@RequestMapping("/ns/lexica/nicosiaesperlinga/workflow")
public class WorkflowHTMLController {

    @GetMapping(value = "", params = "!ttl", produces = "!text/turtle")
    View workflowRedirect(){
        return new RedirectView("/ns/lexica/nicosiaesperlinga/workflow/");
    }

    @GetMapping(value = "/", params = "!ttl", produces = "!text/turtle")
    String workflow(){
        return "workflow/viewWorkflow.html";
    }
}
