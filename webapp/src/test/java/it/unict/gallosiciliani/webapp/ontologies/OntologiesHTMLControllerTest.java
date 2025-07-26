package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.gs.GSFeaturesCategory;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import it.unict.gallosiciliani.projects.Projects;
import it.unict.gallosiciliani.projects.model.eurio.Project;
import it.unict.gallosiciliani.projects.model.eurio.Result;
import it.unict.gallosiciliani.webapp.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Set;

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
    LinguisticPhenomena liph;

    @MockBean
    Projects projects;

    @MockBean
    GSFeatures gsFeatures;

    final HashedOntologyItem liphc1= createTestLiphItem("c1");
    final HashedOntologyItem liphc2= createTestLiphItem("c2");

    private ResultActions getHTMLPage(final String pageIRI) throws Exception {
        return mockMvc.perform(get(pageIRI).accept(MediaType.TEXT_HTML)).andExpect(status().isOk());
    }

    //PROJECT
    private ResultActions getGallosiciliani2023Page() throws Exception {
        return getHTMLPage("/ns/projects/gallosiciliani2023Project");
    }

    private Project setupTestProject(){
        final Project p=new Project();
        p.setLabel("expected title");
        p.setComment("expected description");

        when(projects.getGallosiciliani2023Project()).thenReturn(p);

        final Result dummyResult=new Result();
        dummyResult.setId("http://test.org/dummyResult");
        dummyResult.setLabel("Dummy result");

        when(projects.getLiph()).thenReturn(dummyResult);
        when(projects.getGsFeatures()).thenReturn(dummyResult);
        when(projects.getNicosiaesperlinga()).thenReturn(dummyResult);
        return p;
    }

    @Test
    void shouldShowTitleInGallosiciliani2023ProjectPage() throws Exception {
        final Project p=setupTestProject();
        getGallosiciliani2023Page().andExpect(xpath("//h1").string(p.getLabel()));
    }

    @Test
    void shouldShowCommentInGallosiciliani2023ProjectPage() throws Exception {
        final Project p=setupTestProject();
        getGallosiciliani2023Page().andExpect(xpath("//p").string(p.getComment()));
    }

    @Test
    void shouldShowResultsInGallosiciliani2023ProjectPage() throws Exception {
        final Project p=setupTestProject();
        final Result r1=new Result();
        r1.setId("http://test.org/result1");
        r1.setLabel("result 1 label");
        when(projects.getLiph()).thenReturn(r1);
        final Result r2=new Result();
        r2.setId("http://test.org/result2");
        r2.setLabel("result 2 label");
        when(projects.getGsFeatures()).thenReturn(r2);
        final Result r3=new Result();
        r3.setId("http://test.org/result3");
        r3.setLabel("result 3 label");
        when(projects.getNicosiaesperlinga()).thenReturn(r3);

        p.setHasResult(Set.of(r1, r2, r3));

        getGallosiciliani2023Page()
                .andExpect(xpath("//a[@href='"+r1.getId()+"']").string(r1.getLabel()))
                .andExpect(xpath("//a[@href='"+r2.getId()+"']").string(r2.getLabel()))
                .andExpect(xpath("//a[@href='"+r3.getId()+"']").string(r3.getLabel()));
    }

    //LIPH
    private ResultActions getLiphHTMLPage() throws Exception {
        return getHTMLPage("/ns/liph");
    }

    @Test
    void shouldShowTitleInLiphPage() throws Exception {
        final String expectedTitle="expected title";
        when(liph.getName()).thenReturn(expectedTitle);
        getLiphHTMLPage().andExpect(xpath("//h1").string(expectedTitle));
    }

    @Test
    void shouldShowCommentInLiphPage() throws Exception {
        final String expectedComment="expected comment";
        when(liph.getComment()).thenReturn(expectedComment);
        getLiphHTMLPage().andExpect(xpath("//p").string(expectedComment));
    }


    @Test
    void shouldShowLiphClasses() throws Exception {
        when(liph.getNamespace()).thenReturn(LinguisticPhenomena.NS);
        when(liph.getClasses()).thenReturn(List.of(liphc1,liphc2));
        testLiphItems();
    }

    private void testLiphItems() throws Exception {
        getLiphHTMLPage().andExpect(xpath("//h3[@id='c1']").string(liphc1.getLabel()))
                .andExpect(xpath("//h3[@id='c1']/following-sibling::p").string(liphc1.getComment()))
                .andExpect(xpath("//h3[@id='c2']").string(liphc2.getLabel()))
                .andExpect(xpath("//h3[@id='c2']/following-sibling::p").string(liphc2.getComment()));
    }

    @Test
    void shouldShowLiphObjProperties() throws Exception {
        when(liph.getNamespace()).thenReturn(LinguisticPhenomena.NS);
        when(liph.getObjProperties()).thenReturn(List.of(liphc1,liphc2));
        testLiphItems();
    }

    @Test
    void shouldShowLiphDataProperties() throws Exception {
        when(liph.getNamespace()).thenReturn(LinguisticPhenomena.NS);
        when(liph.getDataProperties()).thenReturn(List.of(liphc1,liphc2));
        testLiphItems();
    }


    @Test
    void shouldShowLiphItems() throws Exception {
        final HashedOntologyItem c1= createTestLiphItem("c1");
        final HashedOntologyItem c2= createTestLiphItem("c2");
        when(liph.getNamespace()).thenReturn(LinguisticPhenomena.NS);
        when(liph.getClasses()).thenReturn(List.of(c1,c2));
        getLiphHTMLPage().andExpect(xpath("//h3[@id='c1']").string(c1.getLabel()))
                .andExpect(xpath("//h3[@id='c1']/following-sibling::p").string(c1.getComment()))
                .andExpect(xpath("//h3[@id='c2']").string(c2.getLabel()))
                .andExpect(xpath("//h3[@id='c2']/following-sibling::p").string(c2.getComment()));
    }

    private HashedOntologyItem createTestLiphItem(final String id){
        return new HashedOntologyItem(LinguisticPhenomena.NS+id, LinguisticPhenomena.NS) {
            @Override
            public String getLabel() {
                return "label"+id;
            }

            @Override
            public String getComment() {
                return "comment"+id;
            }
        };
    }
    // GS FEATURES
    private ResultActions getGSFeaturesHTMLPage() throws Exception {
        return getHTMLPage("/ns/gs-features");
    }

    @Test
    void shouldShowTitleInGSFeaturesPage() throws Exception {
        final String expectedTitle="expected title";
        when(gsFeatures.getName()).thenReturn(expectedTitle);
        getGSFeaturesHTMLPage().andExpect(xpath("//h1").string(expectedTitle));
    }

    @Test
    void shouldShowPhenomenaInGsPage() throws Exception {
        when(gsFeatures.getName()).thenReturn("a title");

        final TestUtil util=new TestUtil();
        final GSFeaturesCategory c1=util.createCategory(GSFeatures.NS);
        final HashedOntologyItem i11=util.addChild(GSFeatures.NS, c1);
        final GSFeaturesCategory c2=util.createCategory(GSFeatures.NS);
        final HashedOntologyItem i21= util.addChild(GSFeatures.NS, c2);
        final HashedOntologyItem i22= util.addChild(GSFeatures.NS, c2);


        when(gsFeatures.getCategories()).thenReturn(List.of(c1, c2));

        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+c1.getId()+"']/th[1]").string(c1.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+c1.getId()+"']/th[2]").string(c1.getComment()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i11.getId()+"']/td[1]").string(i11.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i11.getId()+"']/td[2]").string(i11.getComment()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+c2.getId()+"']/th[1]").string(c2.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+c2.getId()+"']/th[2]").string(c2.getComment()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i21.getId()+"']/td[1]").string(i21.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i21.getId()+"']/td[2]").string(i21.getComment()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i22.getId()+"']/td[1]").string(i22.getLabel()));
        getGSFeaturesHTMLPage().andExpect(xpath("//tr[@id='"+i22.getId()+"']/td[2]").string(i22.getComment()));
    }

}
