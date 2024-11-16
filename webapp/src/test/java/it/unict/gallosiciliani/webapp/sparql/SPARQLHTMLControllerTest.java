package it.unict.gallosiciliani.webapp.sparql;

import it.unict.gallosiciliani.webapp.WebAppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Tests for {@link SPARQLHTMLController}
 * @author Cristiano Longo
 */
@WebMvcTest(SPARQLHTMLController.class)
public class SPARQLHTMLControllerTest {
    private static final String NS = "http://test.org/";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SPARQLService sparqlService;

    @MockBean
    WebAppProperties props;

    @BeforeEach
    void setNs(){
        final URI ns = URI.create(NS);
        when(props.getNs()).thenReturn(ns);
    }

    @Test
    void shouldReturnFormWithPostEmptyField() throws Exception {
        mockMvc.perform(get("/sparql")).andExpect(status().isOk())
                .andExpect(xpath("//form//textarea[@name='query']").string(""));

    }
}
