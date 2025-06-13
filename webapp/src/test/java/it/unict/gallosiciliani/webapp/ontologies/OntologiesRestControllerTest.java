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
        shouldReturnOntologyInTTLFormat("/ns/liph", liph);
    }

    @Test
    void shouldReturnGSFeaturesOntologyTTL() throws Exception {
        shouldReturnOntologyInTTLFormat("/ns/gs-features", gsFeatures);
    }

    @Test
    void shouldReturnProjects() throws Exception {
        shouldReturnOntologyInTTLFormat("/ns/projects", projects);
        shouldReturnOntologyInTTLFormat("/ns/projects/", projects);
        shouldReturnOntologyInTTLFormat("/ns/projects/gallosiciliani2023Project", projects);
    }

    @Test
    void shouldReturnAboxTTL() throws Exception {
        shouldReturnAboxTTL("/ns/lexica");
        shouldReturnAboxTTL("/ns/lexica/");
        shouldReturnAboxTTL("/ns/lexica/nicosiaesperlinga");
    }

    private void shouldReturnAboxTTL(final String path) throws Exception {
        final String expectedFile="test";
        when(abox.getOntologyAsStr()).thenReturn(expectedFile);
        mockMvc.perform(get(path)
                        .accept("text/turtle")).andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle;charset=UTF-8"))
                .andExpect(content().string(expectedFile));
    }

    /**
     * Test that the ontology is returned as a raw file when the mime-type requested is TTL
     * @param path relative path for the request
     * @param o the mock for the ontology loader
     */
    private void shouldReturnOntologyInTTLFormat(final String path, final OntologyLoader o) throws Exception {
        final String expectedFile="test";
        when(o.getOntologyAsStr()).thenReturn(expectedFile);
        mockMvc.perform(get(path)
                        .accept("text/turtle")).andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle;charset=UTF-8"))
                .andExpect(content().string(expectedFile));
    }
}
