package it.unict.gallosiciliani.webapp.lexica;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.webapp.TestUtil;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class LexicaServiceTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LexicaService lexicaService;
    @Autowired
    protected PlatformTransactionManager txManager;

    private final TestUtil util=new TestUtil();

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


        PersistenceTestUtils.build().persist(c).persist(a).persist(b).execute(txManager, entityManager);
        try{
            final Iterator<Lexicon> actualLexicaIt=lexicaService.findAllLexica().iterator();
            util.checkEquals(a, actualLexicaIt.next());
            util.checkEquals(b, actualLexicaIt.next());
            util.checkEquals(c, actualLexicaIt.next());
            assertFalse(actualLexicaIt.hasNext());
        } finally {
            PersistenceTestUtils.build().remove(b).remove(a).remove(c).execute(txManager, entityManager);
        }

    }

    @Test
    void shouldGetLexiconByIRI(){
        final LexiconWithThreeEntries t=new LexiconWithThreeEntries();
        t.persist(entityManager, txManager);
        try{
            final Lexicon actual = lexicaService.findLexiconByIRI(t.lexicon.getId());
            util.checkEquals(t.lexicon, actual);
        } finally {
            t.cleanup(txManager, entityManager);
        }
    }

    @Test
    void shouldReturnEntriesSortedByLemma(){
        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
        lexiconWithThreeEntries.persist(entityManager, txManager);
        try {
            final Iterator<LexicalEntry> actualIt = lexicaService.findAllEntriesAlphabeticallyOrdered(lexiconWithThreeEntries.lexicon, EntrySelector.ALL)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryA, actualIt.next());
            util.checkEquals(lexiconWithThreeEntries.entryB, actualIt.next());
            util.checkEquals(lexiconWithThreeEntries.entryC, actualIt.next());
            assertFalse(actualIt.hasNext());
        } finally {
            lexiconWithThreeEntries.cleanup(txManager, entityManager);
        }
    }

    @Test
    void testSelectByPOS(){
        final LexiconWithThreeEntries lexiconWithThreeEntries = new LexiconWithThreeEntries();
        lexiconWithThreeEntries.entryA.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.noun);
        lexiconWithThreeEntries.entryB.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.verb);
        lexiconWithThreeEntries.entryC.setPartOfSpeech(lexiconWithThreeEntries.lexInfo.noun);

        lexiconWithThreeEntries.persist(entityManager, txManager);
        try {
            final EntrySelector selector = new EntrySelector();
            selector.setPos(LexInfo.NOUN_INDIVIDUAL);
            final Iterator<LexicalEntry> actualIt = lexicaService.findAllEntriesAlphabeticallyOrdered(lexiconWithThreeEntries.lexicon, selector)
                    .iterator();
            util.checkEquals(lexiconWithThreeEntries.entryA, actualIt.next());
            util.checkEquals(lexiconWithThreeEntries.entryC, actualIt.next());
            assertFalse(actualIt.hasNext());
        } finally {
            lexiconWithThreeEntries.cleanup(txManager, entityManager);
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
