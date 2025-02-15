package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.derivations.DerivationPrinter;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Test for {@link DerivationHTMLController}
 */
@WebMvcTest(value={DerivationHTMLController.class})
public class DerivationHTMLControllerTest {

    private final Locale locale = Locale.ITALIAN;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DerivationService derivationService;

    @Test
    void shouldGetTheEmptyForm() throws Exception {
        mockMvc.perform(get("/derivation/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(xpath("//input[@name='lemma']/@value").string(""))
                .andExpect(xpath("//input[@name='etymon']/@value").string(""))
                .andExpect(xpath("//ul").doesNotExist());

    }

    @Test
    void shouldReportSubmittedLemmaAndEtymon() throws Exception {
        final String expectedLemma = "expectedLemma";
        final String expectedEtymon = "expectedEtymon";
        when(derivationService.derives(any(), any())).thenReturn(new NearestShortestDerivation(expectedLemma));
        mockMvc.perform(post("/derivation/").param("lemma", expectedLemma).param("etymon", expectedEtymon))
                .andExpect(status().isOk())
                .andExpect(xpath("//input[@name='lemma']/@value").string(expectedLemma))
                .andExpect(xpath("//input[@name='etymon']/@value").string(expectedEtymon));
    }

    @Test
    void shouldReportDerivations() throws Exception {
        final String lemma = "aLemma";
        final String etymon = "eEtymon";

        final LinguisticPhenomenon p=mock(LinguisticPhenomenon.class);
        when(p.getIRI()).thenReturn(GSFeatures.NS+"p");

        final LinguisticPhenomenon q=mock(LinguisticPhenomenon.class);
        when(q.getIRI()).thenReturn(GSFeatures.NS+"q");

        final DerivationPathNode d0=mock(DerivationPathNode.class);
        when(d0.get()).thenReturn(lemma);
        when(d0.getLinguisticPhenomenon()).thenReturn(p);
        when(d0.prev()).thenReturn(new DerivationPathNodeImpl(etymon));

        final DerivationPathNode d1=mock(DerivationPathNode.class);
        when(d1.get()).thenReturn(lemma);
        when(d1.getLinguisticPhenomenon()).thenReturn(q);
        when(d1.prev()).thenReturn(new DerivationPathNodeImpl(etymon));


        final NearestShortestDerivation expectedDerivations=mock(NearestShortestDerivation.class);
        when(expectedDerivations.getDerivation()).thenReturn(List.of(d0, d1));

        when(derivationService.derives(etymon, lemma)).thenReturn(expectedDerivations);

        final DerivationPrinter printer=new DerivationPrinter(GSFeatures.LABEL_PROVIDER_ID);
        final String d0AsStr=printer.print(d0, locale);
        final String d1AsStr=printer.print(d1, locale);

        mockMvc.perform(post("/derivation/").param("lemma", lemma).param("etymon", etymon))
                .andExpect(status().isOk())
                .andExpect(xpath("//ul[@id='derivations']/li[1]").string(d0AsStr))
                .andExpect(xpath("//ul[@id='derivations']/li[2]").string(d1AsStr))
                .andExpect(xpath("//ul[@id='derivations']/li[3]").doesNotExist());
    }

}