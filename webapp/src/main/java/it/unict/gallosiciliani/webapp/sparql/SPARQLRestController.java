package it.unict.gallosiciliani.webapp.sparql;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/sparql")
@Slf4j
public class SPARQLRestController {

    @Autowired
    SPARQLService sparqlService;

    @PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = "text/csv")
    public String urlEncodedPostQueryCSV(final SPARQLQueryForm form) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format text/csv", form.getQuery());
        return sparqlService.performSelectQuery(form.getQuery(), ResultsFormat.FMT_RS_CSV);
    }

    @PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = "application/sparql-results+xml")
    public String urlEncodedPostQueryXML(final SPARQLQueryForm form) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format application/sparql-results+xml", form.getQuery());
        return sparqlService.performSelectQuery(form.getQuery(), ResultsFormat.FMT_RS_XML);
    }

    @PostMapping(value={"","/"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = "application/sparql-results+json")
    public String urlEncodedPostQueryJSON(final SPARQLQueryForm form) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format application/sparql-results+json", form.getQuery());
        return sparqlService.performSelectQuery(form.getQuery(), ResultsFormat.FMT_RS_JSON);
    }


    @GetMapping(value={"","/"}, produces = "text/csv")
    public String getQueryCSV(final @RequestParam String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format text/csv", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_CSV);
    }

    @GetMapping(value={"","/"}, produces = "application/sparql-results+xml")
    public String getQueryXML(final @RequestParam String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format application/sparql-results+xml", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_XML);
    }

    @GetMapping(value={"","/"}, produces = "application/sparql-results+json")
    public String getQueryJSON(final @RequestParam String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} with response format application/sparql-results+json", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_JSON);
    }

    @PostMapping(value={"","/"}, consumes = "application/sparql-query", produces = "text/csv")
    public String directPostQueryCSV(final @RequestBody String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} using POST direct with response format text/csv", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_CSV);
    }

    @PostMapping(value={"","/"}, consumes = "application/sparql-query", produces = "application/sparql-results+xml")
    public String directPostQueryXML(final @RequestBody String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} using post direct with response format application/sparql-results+xml", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_XML);
    }

    @PostMapping(value={"","/"}, consumes = "application/sparql-query", produces = "application/sparql-results+json")
    public String directPostQueryJSON(final @RequestBody String query) throws SPARQLQueryException {
        log.info("Performing SPARQL query {} using post direct with response format application/sparql-results+json", query);
        return sparqlService.performSelectQuery(query, ResultsFormat.FMT_RS_JSON);
    }

    @ExceptionHandler(value=SPARQLQueryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView showQueryError(final SPARQLQueryException e){
        final ModelAndView m = new ModelAndView("sparql/error-form");
        m.getModel().put("query", e.getQuery());
        m.getModel().put("error", e.getCause().getMessage());
        log.error("Error performing SPARQL query {}", e.getQuery(), e.getCause());
        return m;
    }
}
