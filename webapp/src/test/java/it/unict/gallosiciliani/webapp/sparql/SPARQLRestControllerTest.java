package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.exceptions.OWLPersistenceException;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for {@link SPARQLRestController}
 * @author Cristiano Longo
 */
@WebMvcTest(SPARQLRestController.class)
public class SPARQLRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SPARQLService sparqlService;

    @Autowired
    private MessageSource messageSource;

    @Test
    void shouldReturnResultsAsCSV() throws Exception, SPARQLQueryException {
        final String query = "SELECT ?x ?y ?x WHERE {?x ?y ?z}";
        final String expected = "?x,?y,?z\n\r" + "x,y,z\n\r";
        when(sparqlService.performSelectQueryJena(query, ResultsFormat.FMT_RS_CSV)).thenReturn(expected);

        mockMvc.perform(post("/sparql").param("query", query).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(content().string(expected));
    }

    @Test
    void shouldReturn400OnWrongQuery() throws Exception, SPARQLQueryException {
        final String query = "wrongquery";
        final OWLPersistenceException cause = new OWLPersistenceException("error cause");

        when(sparqlService.performSelectQueryJena(query, ResultsFormat.FMT_RS_CSV)).thenThrow(new SPARQLQueryException(query, cause));
        final Locale locale = Locale.ENGLISH;

        final String[] errorMessageParameters = {cause.getMessage()};
        final String expectedErrorMessage = messageSource.getMessage("galloitailici.kb.sparql.error", errorMessageParameters, locale);

        mockMvc.perform(post("/sparql").param("query", query).contentType(MediaType.APPLICATION_FORM_URLENCODED).locale(locale))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(xpath("//form//textarea[@name='query']").string(query))
                .andExpect(content().string(containsString(expectedErrorMessage)));
    }
}