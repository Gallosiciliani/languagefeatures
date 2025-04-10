package it.unict.gallosiciliani.webapp.sparql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Tests for {@link SPARQLHTMLController}
 * @author Cristiano Longo
 */
@WebMvcTest(SPARQLHTMLController.class)
public class SPARQLHTMLControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnFormWithPostEmptyField() throws Exception {
        mockMvc.perform(get("/sparql")).andExpect(status().isOk())
                .andExpect(xpath("//form//textarea[@name='query']").string(""));

    }
}
