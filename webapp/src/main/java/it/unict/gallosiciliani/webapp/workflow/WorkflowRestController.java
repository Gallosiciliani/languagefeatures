package it.unict.gallosiciliani.webapp.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Just provide the workflow ttl file
 * @author Cristiano Longo
 */
@RestController
@RequestMapping("/ns/lexica/nicosiaesperlinga/workflow")
public class WorkflowRestController {

    @Autowired
    Workflow workflow;

    @GetMapping(value = {"","/"}, produces = "text/turtle")
    String workflow(){
        return workflow.getOntologyAsStr();
    }
}
