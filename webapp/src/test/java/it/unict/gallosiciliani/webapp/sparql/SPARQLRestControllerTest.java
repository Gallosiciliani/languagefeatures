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

import java.net.URI;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void testdUrlEncodedPostQueryReturnResultsAsCSV() throws Exception, SPARQLQueryException {
        sendQueryUrlEncodedPost(ResultsFormat.FMT_RS_CSV);
    }

    private void sendQueryUrlEncodedPost(final ResultsFormat resultsFormat) throws SPARQLQueryException, Exception {
        final String query = "SELECT ?x ?y ?x WHERE {?x ?y ?z}";
        final String expected = "expected result";
        when(sparqlService.query(query, resultsFormat)).thenReturn(expected);


        mockMvc.perform(post("/sparql").param("query", query).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(resultsFormat.getSymbol()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(resultsFormat.getSymbol()))
                .andExpect(content().string(expected));
    }

    @Test
    void testUrlEncodedPostReturnResultsAsXML() throws Exception, SPARQLQueryException {
        sendQueryUrlEncodedPost(ResultsFormat.FMT_RS_XML);
    }

    @Test
    void testUrlEncodedPostReturnResultsAsJson() throws Exception, SPARQLQueryException {
        sendQueryUrlEncodedPost(ResultsFormat.FMT_RS_JSON);
    }

    @Test
    void testGetQueryReturnResultsAsCSV() throws Exception, SPARQLQueryException {
        sendQueryGet(ResultsFormat.FMT_RS_CSV);
    }

    private void sendQueryGet(final ResultsFormat resultsFormat) throws SPARQLQueryException, Exception {
        final String query = "SELECT ?x ?y ?x WHERE {?x ?y ?z}";
        final String expected = "expected result";
        when(sparqlService.query(query, resultsFormat)).thenReturn(expected);

        mockMvc.perform(get(URI.create("/sparql")).param("query", query).accept(resultsFormat.getSymbol()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(resultsFormat.getSymbol()))
                .andExpect(content().string(expected));
    }

    @Test
    void testGetQueryReturnResultsAsXML() throws Exception, SPARQLQueryException {
        sendQueryGet(ResultsFormat.FMT_RS_XML);
    }

    @Test
    void testGetQueryReturnResultsAsJson() throws Exception, SPARQLQueryException {
        sendQueryGet(ResultsFormat.FMT_RS_JSON);
    }

    @Test
    void testPostQueryReturnResultsAsCSV() throws Exception, SPARQLQueryException {
        sendQueryPostDirect(ResultsFormat.FMT_RS_CSV);
    }

    private void sendQueryPostDirect(final ResultsFormat resultsFormat) throws SPARQLQueryException, Exception {
        final String query = "SELECT ?x ?y ?x WHERE {?x ?y ?z}";
        final String expected = "expected result";
        when(sparqlService.query(query, resultsFormat)).thenReturn(expected);

        mockMvc.perform(post("/sparql").contentType("application/sparql-query").content(query).accept(resultsFormat.getSymbol()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(resultsFormat.getSymbol()))
                .andExpect(content().string(expected));
    }

    @Test
    void testPostQueryReturnResultsAsXML() throws Exception, SPARQLQueryException {
        sendQueryPostDirect(ResultsFormat.FMT_RS_XML);
    }

    @Test
    void testPostQueryReturnResultsAsJson() throws Exception, SPARQLQueryException {
        sendQueryPostDirect(ResultsFormat.FMT_RS_JSON);
    }

    @Test
    void shouldReturn400OnWrongQuery() throws Exception, SPARQLQueryException {
        final String query = "wrongquery";
        final OWLPersistenceException cause = new OWLPersistenceException("error cause");

        when(sparqlService.query(query, ResultsFormat.FMT_RS_CSV)).thenThrow(new SPARQLQueryException(query, cause));
        final Locale locale = Locale.ENGLISH;

        final String[] errorMessageParameters = {cause.getMessage()};
        final String expectedErrorMessage = messageSource.getMessage("galloitailici.kb.sparql.error", errorMessageParameters, locale);

        mockMvc.perform(post("/sparql").param("query", query).contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(ResultsFormat.FMT_RS_CSV.getSymbol()).locale(locale))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(xpath("//form//textarea[@name='query']").string(query))
                .andExpect(content().string(containsString(expectedErrorMessage)));
    }
}