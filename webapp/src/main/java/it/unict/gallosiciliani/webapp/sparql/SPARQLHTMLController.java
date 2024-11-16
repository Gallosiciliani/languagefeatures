package it.unict.gallosiciliani.webapp.sparql;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * Web pages for querying the knowledge base with sparql
 */
@Controller
@RequestMapping("/sparql")
public class SPARQLHTMLController {

    @GetMapping(value={"","/"})
    public String viewForm(){
        return "sparql/form";
    }
}
