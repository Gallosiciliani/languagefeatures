package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.webapp.WebAppProperties;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Test for web pages provided by {@link LexicaHTMLController}
 * @see <a href="https://spring.io/guides/gs/testing-web">Testing the Web Layer</a>
 */
@WebMvcTest(value={LexicaHTMLController.class, EntrySummarizer.class})
public class LexicaHTMLControllerTest {

    private static final String NS = "http://test.org/";

    private final Locale locale = Locale.ITALIAN;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LexicaService lexicaService;

    @MockBean
    WebAppProperties props;

    @Test
    void shouldProvideLinksAllLexicaInKB() throws Exception {
        final List<Lexicon> expected = new ArrayList<>(2);

        final Lexicon expected1 = new Lexicon();
        final String expectedIRI1=NS+"expected1";
        expected1.setId(expectedIRI1);
        expected1.setTitle("Title of the expected lexicon 1");
        expected.add(expected1);

        final Lexicon expected2 = new Lexicon();
        final String expectedIRI2=NS+"expected2";
        expected2.setId(expectedIRI2);
        expected2.setTitle("Title of the expected lexicon 2");
        expected.add(expected2);

        when(lexicaService.findAllLexica()).thenReturn(expected);

        mockMvc.perform(get("/lexica/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(xpath("//a[@href='lexicon?id="+expectedIRI1+"']").string("Title of the expected lexicon 1"))
                .andExpect(xpath("//a[@href='lexicon?id="+expectedIRI2+"']").string("Title of the expected lexicon 2"));
    }

    private void setPages(final LexiconWithThreeEntries l3e){
        final PagingProperties p= Mockito.mock(PagingProperties.class);
        when(p.getPages()).thenReturn(l3e.pageSelectors);
        when(props.getPaging()).thenReturn(p);
        when(lexicaService.getPageLabels()).thenReturn(l3e.pageLabels);
    }

    private final LexiconWithThreeEntries.RetrieveLexiconRequestPerformer GETperformer = new LexiconWithThreeEntries.RetrieveLexiconRequestPerformer() {
        @Override
        public ResultActions perform(final LexiconWithThreeEntries l3e) throws Exception {
            setPages(l3e);
            when(lexicaService.findLexiconByIRI(l3e.lexicon.getId())).thenReturn(l3e.lexicon);
            when(lexicaService.findEntries(l3e.lexicon, EntrySelector.ALL))
                    .thenReturn(l3e.pageABEntries);
            return mockMvc.perform(get("/lexica/lexicon").param("id", l3e.lexicon.getId())
                            .accept(MediaType.TEXT_HTML).locale(locale))
                    .andExpect(status().isOk());
        }
    };

    private final LexiconWithThreeEntries.RetrieveLexiconRequestPerformer POSTperformer = createPostPerformer(EntrySelector.ALL);

    private LexiconWithThreeEntries.RetrieveLexiconRequestPerformer createPostPerformer(final EntrySelector selector){
        return (l3e) -> {
            setPages(l3e);
            when(lexicaService.findLexiconByIRI(l3e.lexicon.getId())).thenReturn(l3e.lexicon);
            when(lexicaService.findEntries(l3e.lexicon, EntrySelector.ALL))
                    .thenReturn(l3e.pageABEntries);

            return mockMvc.perform(post("/lexica/lexicon")
                    .param("id", l3e.lexicon.getId())
                    .param("pos", selector.getPos())
                    .param("featureType", selector.getFeatureType())
                    .param("page", Integer.toString(selector.getPage()))
                    .accept(MediaType.TEXT_HTML).locale(locale)).andExpect(status().isOk());
        };
    }

    @Test
    public void shouldGetSelectAllFirstPageEntries() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        GETperformer.perform(l3e);

        final ArgumentCaptor<Lexicon> lexiconArgumentCaptor = ArgumentCaptor.forClass(Lexicon.class);
        final ArgumentCaptor<EntrySelector> entrySelectorArgumentCaptor = ArgumentCaptor.forClass(EntrySelector.class);
        verify(lexicaService).findEntries(lexiconArgumentCaptor.capture(), entrySelectorArgumentCaptor.capture());
        assertSame(l3e.lexicon, lexiconArgumentCaptor.getValue());
        assertEquals(EntrySelector.ALL, entrySelectorArgumentCaptor.getValue());
    }

    @Test
    public void shouldPostUseSelectForm() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final EntrySelector expectedSelector = new EntrySelector();
        expectedSelector.setPos(LexInfo.NOUN_INDIVIDUAL);
        //expectedSelector.setFeatureType(gskb.getHasNorthernItalyFeatureClass());

        createPostPerformer(expectedSelector).perform(l3e);

        final ArgumentCaptor<Lexicon> lexiconArgumentCaptor = ArgumentCaptor.forClass(Lexicon.class);
        final ArgumentCaptor<EntrySelector> entrySelectorArgumentCaptor = ArgumentCaptor.forClass(EntrySelector.class);
        verify(lexicaService).findEntries(lexiconArgumentCaptor.capture(), entrySelectorArgumentCaptor.capture());
        assertEquals(expectedSelector.getPos(), entrySelectorArgumentCaptor.getValue().getPos());
        //assertEquals(expectedSelector.getFeatureType(), entrySelectorArgumentCaptor.getValue().getFeatureType());
    }

    @Test
    public void shouldShowLexiconTitleGET() throws Exception {
        shouldShowLexiconTitle(GETperformer);
    }

    @Test
    public void shouldShowLexiconTitlePOST() throws Exception {
        shouldShowLexiconTitle(POSTperformer);
    }

    private void shouldShowLexiconTitle(final LexiconWithThreeEntries.RetrieveLexiconRequestPerformer retriever) throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        retriever.perform(l3e).andExpect(xpath("//h1").string(l3e.lexicon.getTitle()));
    }


    @Test
    public void shouldShowLexiconEntryLemmasAndPOSWithGet() throws Exception {
        shouldShowLexiconEntryLemmasAndPOS(GETperformer);
    }

    @Test
    public void shouldShowLexiconEntryLemmasAndPOSWithPost() throws Exception {
        shouldShowLexiconEntryLemmasAndPOS(POSTperformer);
    }

    private void shouldShowLexiconEntryLemmasAndPOS(final LexiconWithThreeEntries.RetrieveLexiconRequestPerformer retriever) throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final String nounLabel = messageSource.getMessage("galloitailici.kb.lexica.pos.noun", new Object[0], locale);
        final String verbLabel = messageSource.getMessage("galloitailici.kb.lexica.pos.verb", new Object[0], locale);
        retriever.perform(l3e)
                .andExpect(xpath("//form/following-sibling::p")
                        .string(messageSource.getMessage("galloitalici.kb.lexica.selection.numentries", new Object[]{2}, locale)))
                .andExpect(xpath("//tbody[1]/tr[1]/th")
                        .string(l3e.entryA.getCanonicalForm().getWrittenRep()+" ("+nounLabel+")"))
                .andExpect(xpath("//tbody[2]/tr[1]/th")
                        .string(l3e.entryB.getCanonicalForm().getWrittenRep()+" ("+verbLabel+")"));
    }

    @Test
    public void shouldPrintNotAvailableForLemmaWithNoEtymon() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final Etymology etyA = l3e.entryA.getEtymology().iterator().next();
        etyA.setName(null);
        final EtyLink linkA = etyA.getStartingLink();
        linkA.setEtySource(null);
        linkA.setEtySubTarget(null);
        final String etymologyLabel = messageSource.getMessage("galloitalici.kb.lexica.etymology", new Object[0], locale);
        GETperformer.perform(l3e)
                .andExpect(xpath("//tbody[1]/tr/th[text()='"+etymologyLabel+"']/following-sibling::td")
                        .string(messageSource.getMessage("galloitalici.kb.lexica.etymology.na", new Object[0], locale)));
    }

    @Test
    public void shouldShowSimpleNotLinkedLatinEtymonAsIs() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final Form etymonForm = setSimpleLatinEtymon(l3e.entryA);
        final String expectedEtymon = etymonForm.getName();
        final String etymologyLabel = messageSource.getMessage("galloitalici.kb.lexica.etymology", new Object[0], locale);
        GETperformer.perform(l3e)
                .andExpect(xpath("//tbody[1]/tr/th[text()='"+etymologyLabel+"']/following-sibling::td/ul/li[1]")
                        .string(expectedEtymon));
    }


    /**
     * Add to the specific entry a latin etymology consisting of a single word
     *
     * @param entry where set the etymon
     * @return the {@link Form} created to represent the latin etymon
     */
    private Form setSimpleLatinEtymon(final LexicalEntry entry){
        final String expectedEtymon = "expectedLatinEtymon";
        final String expectedEtymonNormalized = "expectedlatinetymon";
        final Etymology etyA = entry.getEtymology().iterator().next();
        etyA.setName(expectedEtymon);
        final EtyLink linkA = etyA.getStartingLink();
        linkA.setEtySource(null);
        final Form etymonForm = new Form();
        linkA.getEtySubSource().add(etymonForm);
        etymonForm.setId("http://www.example.org/latinForm");
        etymonForm.setName(expectedEtymon);
        etymonForm.setWrittenRep(expectedEtymonNormalized);
        return etymonForm;
    }

    @Test
    public void shouldProvideLinkForSimpleLinkedLatinEtymon() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final Form etymonForm = setSimpleLatinEtymon(l3e.entryA);
        final String expectedEtymon = etymonForm.getName();
        final URI expectedEtymonURI = URI.create("http://anexternalresource.org");
        etymonForm.setSeeAlso(expectedEtymonURI);
        final String etymologyLabel = messageSource.getMessage("galloitalici.kb.lexica.etymology", new Object[0], locale);

        GETperformer.perform(l3e)
                .andExpect(xpath("//tbody[1]/tr/"+
                        "th[text()='"+etymologyLabel+"']/following-sibling::td/ul/li[1]/"+
                        "a[@href='"+expectedEtymonURI+"']")
                        .string(expectedEtymon));
    }

    @Test
    public void shouldShowAllLatinEtymonComponentsHabitinu() throws Exception {
        final String fullEtymon = "HABĬTU+-ĪNU";
        final String[] components = {"HABĬTU","-ĪNU"};
        final String[] componentsNormalized = {"habitu","inu"};
        shouldShowAllLatinEtymonComponents(fullEtymon, components, componentsNormalized);
    }

    @Test
    public void shouldShowAllLatinEtymonComponentsBaculoEttaOne() throws Exception {
        final String fullEtymon = "BACŬLŬ+-ETTA+-ONE";
        final String[] components = {"BACŬLŬ","-ETTA","-ONE"};
        final String[] componentsNormalized = {"baculu","etta","one"};
        shouldShowAllLatinEtymonComponents(fullEtymon, components, componentsNormalized);
    }

    /**
     *
     * @param fullEtymon etymon as string
     * @param components etymon components
     * @param componentsNormalized same etymon components, but normalized (removed accent, lowercased, ...)
     * @throws Exception exception
     */
    private void shouldShowAllLatinEtymonComponents(final String fullEtymon, final String[] components, final String[] componentsNormalized) throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final Form[] forms = l3e.setEtymonSubcomponents(LexiconWithThreeEntries.Entry.A, fullEtymon, components, componentsNormalized);
        checkExpectedEtymonSubcomponent(GETperformer.perform(l3e), "1", 0, forms);
    }

    private ResultActions checkExpectedEtymonSubcomponent(final ResultActions r, final String entryNum, final int componentNum, final Form[] expectedComponents) throws Exception {
        final String etymologyLabel = messageSource.getMessage("galloitalici.kb.lexica.etymology", new Object[0], locale);
        if (expectedComponents.length==componentNum)
            return r;
        return checkExpectedEtymonSubcomponent(r.andExpect(xpath("//tbody["+entryNum+"]/tr/th[text()='"+etymologyLabel+"']/following-sibling::td/ul/li["+(componentNum+1)+"]")
                .string(expectedComponents[componentNum].getName())), entryNum, componentNum+1, expectedComponents);
    }

//    @Test
//    public void shouldShowLexiconEntryPhoneticFeatures() throws Exception {
//        final GSKB gskb = new GSKB(props);
//        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
//
//        final GSLanguageFeatureCode entryAFeature1code = GSLanguageFeatureCode.N03b;
//        final Etymology etymologyA = l3e.entryA.getEtymology().iterator().next();
//        etymologyA.getTypes().add(gskb.getHasFeatureClass(entryAFeature1code));
//
//        final String expectedEntryAFeature1Label = "label for N03b";
//        when(languageFeatureLabelProvider.getLabel(entryAFeature1code, locale)).thenReturn(expectedEntryAFeature1Label);
//
//        final String featuresLabel = messageSource.getMessage("galloitalici.kb.lexica.features", new Object[0], locale);
//
//        GETperformer.perform(l3e)
//                .andExpect(xpath("//tbody[@id='"+l3e.getId(LexiconWithThreeEntries.Entry.A)+"']/tr/th[text()='"+featuresLabel+"']/following-sibling::td/ul/li[1]")
//                        .string(expectedEntryAFeature1Label))
//                .andExpect(xpath("//tbody[@id='"+l3e.getId(LexiconWithThreeEntries.Entry.A)+"']/tr/th[text()='"+featuresLabel+"']/following-sibling::td/ul/li[2]")
//                        .doesNotExist());
//    }

//    @Test
//    public void shouldGetLexiconShowFormWithSelectAllValues() throws Exception {
//        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
//        GETperformer.perform(l3e).andExpect(xpath("//select[@name='pos']/option[@selected]/@value").string(EntrySelector.ANY_SELECTOR))
//            .andExpect(xpath("//select[@name='featureType']/option[@selected]/@value").string(EntrySelector.ANY_SELECTOR));
//    }

    @Test
    public void shouldPostPreservesSelectionValuesInForm() throws Exception {
        final LexiconWithThreeEntries l3e = new LexiconWithThreeEntries();
        final EntrySelector expectedSelector = new EntrySelector();
        expectedSelector.setPos(LexInfo.NOUN_INDIVIDUAL);
//        expectedSelector.setFeatureType(gskb.getHasNorthernItalyFeatureClass());

        createPostPerformer(expectedSelector).perform(l3e)
            .andExpect(xpath("//select[@name='pos']/option[@selected]/@value").string(expectedSelector.getPos()));
//                .andExpect(xpath("//select[@name='featureType']/option[@selected]/@value").string(expectedSelector.getFeatureType()));
    }

}
