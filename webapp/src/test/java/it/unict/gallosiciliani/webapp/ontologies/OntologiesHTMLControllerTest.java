package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.webapp.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Test returning ontologies as HTML
 */
@WebMvcTest(value={OntologiesHTMLController.class})
public class OntologiesHTMLControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GSFeatures gsFeatures;


    private ResultActions getGSFeaturesHTMLPage() throws Exception {
        return mockMvc.perform(get("/ns/gs-features").accept(MediaType.TEXT_HTML)).andExpect(status().isOk());
    }

    @Test
    void shouldShowTitle() throws Exception {
        final String expectedTitle="expected title";
        when(gsFeatures.getName()).thenReturn(expectedTitle);
        getGSFeaturesHTMLPage().andExpect(xpath("//h1").string(expectedTitle));
    }

    @Test
    void shouldShowPhenomena() throws Exception {
        when(gsFeatures.getName()).thenReturn("a title");

        final TestUtil util=new TestUtil();
        final LinguisticPhenomenon p1=util.createPhenomenon(GSFeatures.NS);
        final LinguisticPhenomenon p2=util.createPhenomenon(GSFeatures.NS);

        final List<LinguisticPhenomenon> phenomena=List.of(p1,p2);
        when(gsFeatures.getRegexLinguisticPhenomena()).thenReturn(phenomena);

        getGSFeaturesHTMLPage().andExpect(xpath("//table/tr/td[1]").string(p1.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//table/tr/td[2]").string(p1.getComment()));
    }

}
