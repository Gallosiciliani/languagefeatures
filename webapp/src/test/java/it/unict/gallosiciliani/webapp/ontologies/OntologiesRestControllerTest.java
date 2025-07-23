package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import it.unict.gallosiciliani.projects.Projects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value={OntologiesRestController.class})
public class OntologiesRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LinguisticPhenomena liph;

    @MockBean
    GSFeatures gsFeatures;

    @MockBean
    ABox abox;

    @MockBean
    Projects projects;

    @Test
    void testAutowire(){
        assertNotNull(liph);
    }

    @Test
    void shouldReturnLiphOntologyTTL() throws Exception {
        //shouldReturnOntology("/ns/liph", liph);
        shouldReturnOntology("/ns/liph?ttl", liph, false);
    }

    @Test
    void shouldReturnGSFeaturesOntologyTTL() throws Exception {
        shouldReturnOntology("/ns/gs-features", gsFeatures);
        shouldReturnOntology("/ns/gs-features?ttl", gsFeatures, false);
    }

    @Test
    void shouldReturnProjects() throws Exception {
        shouldReturnOntology("/ns/projects", projects);
        shouldReturnOntology("/ns/projects/", projects);
        shouldReturnOntology("/ns/projects?ttl", projects, false);
        shouldReturnOntology("/ns/projects/gallosiciliani2023Project", projects);
    }

    @Test
    void shouldReturnAboxTTL() throws Exception {
        shouldReturnAboxTTL("/ns/lexica");
        shouldReturnAboxTTL("/ns/lexica/");
        shouldReturnAboxTTL("/ns/lexica/nicosiaesperlinga");
        shouldReturnAboxTTL("/ns/lexica/nicosiaesperlinga?ttl", false);
    }

    private void shouldReturnAboxTTL(final String path) throws Exception {
        shouldReturnAboxTTL(path, true);
    }

        private void shouldReturnAboxTTL(final String path, final boolean requestTTL) throws Exception {
        final String expectedFile="test";
        when(abox.getOntologyAsStr()).thenReturn(expectedFile);
        mockMvc.perform(requestTTL ? get(path)
                        .accept("text/turtle"): get(path)).andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle;charset=UTF-8"))
                .andExpect(content().string(expectedFile));
    }

    /**
     * Test that the ontology is returned as a raw file when the mime-type requested is TTL
     * @param path relative path for the request
     * @param o the mock for the ontology loader
     */
    private void shouldReturnOntology(final String path, final OntologyLoader o) throws Exception {
        shouldReturnOntology(path, o, true);
    }

    /**
     * Test that the ontology is returned as a raw file
     * @param path relative path for the request
     * @param o the mock for the ontology loader
     * @param ttl set ttl as requested mime-type
     */
    private void shouldReturnOntology(final String path, final OntologyLoader o, final boolean ttl) throws Exception {
        final String expectedFile="test";
        when(o.getOntologyAsStr()).thenReturn(expectedFile);
        final MockHttpServletRequestBuilder get=get(path);
        mockMvc.perform(ttl ? get.accept("text/turtle") : get).andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle;charset=UTF-8"))
                .andExpect(content().string(expectedFile));
    }

}
