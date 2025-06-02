package it.unict.gallosiciliani.webapp.sparql;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/sparql")
@Slf4j
public class SPARQLRestController {

    @Autowired
    SPARQLService sparqlService;

    @PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = "text/csv")
    public String executeQuery(final SPARQLQueryForm form) throws IOException, SPARQLQueryException {
        log.info("Performing SPARQL query {}", form.getQuery());
        return sparqlService.performSelectQueryJena(form.getQuery(), ResultsFormat.FMT_RS_CSV);
    }

    @ExceptionHandler(value=SPARQLQueryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView showQueryError(final SPARQLQueryException e){
        final ModelAndView m = new ModelAndView("sparql/error-form");
        m.getModel().put("query", e.getQuery());
        m.getModel().put("error", e.getCause().getMessage());
        log.error("Error performing SPARQL query "+e.getQuery(), e.getCause());
        return m;
    }
}
