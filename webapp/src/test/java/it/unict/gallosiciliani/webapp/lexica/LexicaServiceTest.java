package it.unict.gallosiciliani.webapp.lexica;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.webapp.TestUtil;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class LexicaServiceTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    LexicaService lexicaService;
    @Autowired
    WebAppProperties props;

    private final TestUtil util=new TestUtil();

    @Test
    void ensureUsingInMemoryStorage(){
        assertEquals(JenaOntoDriverProperties.IN_MEMORY, props.getJenaStorageType());
    }
    @Test
    void shouldGetLexicaAlphabeticallyOrdered(){
        final Lexicon a=new Lexicon();
        a.setId("http://test.org/1");
        a.setTitle("lexicon1");

        final Lexicon b=new Lexicon();
        b.setId("http://test.org/2");
        b.setTitle("lexicon2");

        final Lexicon c=new Lexicon();
        c.setId("http://test.org/3");
        c.setTitle("lexicon3");


        PersistenceTestUtils.build().persist(c).persist(a).persist(b).execute(entityManager);
        try{
            final Iterator<Lexicon> actualLexicaIt=lexicaService.findAllLexica().iterator();
            util.checkEquals(a, actualLexicaIt.next());
            util.checkEquals(b, actualLexicaIt.next());
            util.checkEquals(c, actualLexicaIt.next());
            assertFalse(actualLexicaIt.hasNext());
        } finally {
            PersistenceTestUtils.build().remove(b).remove(a).remove(c).execute(entityManager);
        }

    }

    @Test
    void shouldGetLexiconByIRI(){
        final LexiconWithThreeEntries t=new LexiconWithThreeEntries();
        t.persist(entityManager);
        try{
            final Lexicon actual = lexicaService.findLexiconByIRI(t.lexicon.getId());
            util.checkEquals(t.lexicon, actual);
        } finally {
            t.cleanup(entityManager);
        }
    }

    @Test
    void shouldReturnPageLabels(){
        final String[] actualLabels=lexicaService.getPageLabels();
        final LexiconPageSelector[] expectedPages=props.getPaging().getPages();
        for(int i=0; i<expectedPages.length; i++)
            assertEquals(expectedPages[i].getLabel(), actualLabels[i]);
    }

    @Test
    void shouldReturnEntriesInPagesAndSortedByLemma(){
        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
        lexiconWithThreeEntries.persist(entityManager);
        try {
            final EntrySelector s=new EntrySelector();
            final Iterator<LexicalEntry> actualPageABIt = lexicaService.findEntries(lexiconWithThreeEntries.lexicon, s)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryA, actualPageABIt.next());
            util.checkEquals(lexiconWithThreeEntries.entryB, actualPageABIt.next());
            assertFalse(actualPageABIt.hasNext());

            s.setPage(1);
            final Iterator<LexicalEntry> actualPageCIt = lexicaService.findEntries(lexiconWithThreeEntries.lexicon, s)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryC, actualPageCIt.next());
            assertFalse(actualPageCIt.hasNext());
        } finally {
            lexiconWithThreeEntries.cleanup(entityManager);
        }
    }

    @Test
    void testSelectByPOS(){
        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
        lexiconWithThreeEntries.entryA.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.noun);
        lexiconWithThreeEntries.entryB.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.verb);
        lexiconWithThreeEntries.entryC.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.noun);

        lexiconWithThreeEntries.persist(entityManager);
        try {
            final EntrySelector selector = new EntrySelector();
            selector.setPos(LexInfo.NOUN_INDIVIDUAL);
            final Iterator<LexicalEntry> actualPageABIt = lexicaService.findEntries(lexiconWithThreeEntries.lexicon, selector)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryA, actualPageABIt.next());
            assertFalse(actualPageABIt.hasNext());

            selector.setPage(1);
            final Iterator<LexicalEntry> actualPageCIt = lexicaService.findEntries(lexiconWithThreeEntries.lexicon, selector)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryC, actualPageCIt.next());
            assertFalse(actualPageCIt.hasNext());
        } finally {
            lexiconWithThreeEntries.cleanup(entityManager);
        }
    }

//    @Test
//    void testSelectByPOSandFeature(){
//        final GSKB gskb = new GSKB(webAppProperties);
//        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
//        final LexInfo lexInfo = new LexInfo();
//        lexiconWithThreeEntries.entryA.setPartOfSpeech(lexInfo.noun);
//        addFeature(lexiconWithThreeEntries.entryA, GSLanguageFeatureCode.N03b);
//        lexiconWithThreeEntries.entryB.setPartOfSpeech(lexInfo.verb);
//        addFeature(lexiconWithThreeEntries.entryB, GSLanguageFeatureCode.N03b, GSLanguageFeatureCode.S01);
//        addFeature(lexiconWithThreeEntries.entryC, GSLanguageFeatureCode.S01);
//        lexiconWithThreeEntries.entryC.setPartOfSpeech(lexInfo.noun);
//
//        lexiconWithThreeEntries.persist(entityManager, txManager);
//        try {
//            final EntrySelector selector = new EntrySelector();
//            selector.setPos(LexInfo.NOUN_INDIVIDUAL);
//            selector.setFeatureType(gskb.getHasNorthernItalyFeatureClass());
//            final Iterator<LexicalEntry> actualIt = lexicaService.findAllEntriesAlphabeticallyOrdered(lexiconWithThreeEntries.lexicon, selector)
//                    .iterator();
//            testBed.checkEquals(lexiconWithThreeEntries.entryA, actualIt.next());
//            assertFalse(actualIt.hasNext());
//        } finally {
//            lexiconWithThreeEntries.cleanup(txManager, entityManager);
//        }
//    }
//
//    @Test
//    void testSelectByFeature(){
//        final GSKB gskb = new GSKB(webAppProperties);
//        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
//        addFeature(lexiconWithThreeEntries.entryA, GSLanguageFeatureCode.N03b);
//        addFeature(lexiconWithThreeEntries.entryB, GSLanguageFeatureCode.N03b, GSLanguageFeatureCode.S01);
//        addFeature(lexiconWithThreeEntries.entryC, GSLanguageFeatureCode.S01);
//        System.out.println(gskb.getCreateHasGSFeatureClassesQuery());
//
//        lexiconWithThreeEntries.persist(entityManager, txManager);
//        try {
//            final EntrySelector selector = new EntrySelector();
//            selector.setFeatureType(gskb.getHasNorthernItalyFeatureClass());
//            final List<LexicalEntry> actual = lexicaService.findAllEntriesAlphabeticallyOrdered(lexiconWithThreeEntries.lexicon, selector);
//            final Iterator<LexicalEntry> actualIt = actual.iterator();
//            testBed.checkEquals(lexiconWithThreeEntries.entryA, actualIt.next());
//            testBed.checkEquals(lexiconWithThreeEntries.entryB, actualIt.next());
//            assertFalse(actualIt.hasNext(), "Found "+actual);
//        } finally {
//            lexiconWithThreeEntries.cleanup(txManager, entityManager);
//        }
//    }

//    private void addFeature(final LexicalEntry entry, final GSLanguageFeatureCode... code){
//        final GSKB gskb = new GSKB(webAppProperties);
//        final Etymology e = entry.getEtymology().iterator().next();
//        for(final GSLanguageFeatureCode c : code)
//            e.getTypes().add(gskb.getHasFeatureClass(c));
//    }

}
