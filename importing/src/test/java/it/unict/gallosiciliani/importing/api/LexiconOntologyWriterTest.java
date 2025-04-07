package it.unict.gallosiciliani.importing.api;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.FileEntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.InMemoryEntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lime;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test for LexiconOntologyWriterTest
 *
 * @author Cristiano Longo
 */
public class LexiconOntologyWriterTest {

    private final EntityManagerFactoryHelper inMemoryEntityManagerFactory =new InMemoryEntityManagerFactoryHelper();
    private final POSIndividualProvider posProvider=new POSIndividualProvider();
    private final Lexicon lexicon=createLexicon();
    private final LexicalEntry entry=createTestEntry();

    /**
     *
     * @return the lexicon which will be in the ontology
     */
    private Lexicon createLexicon() {
        final Lexicon l=new Lexicon();
        l.setId("http://test.org/lexicon");
        return l;
    }

    /**
     *
     * @return a lexical entry with just id set
     */
    private LexicalEntry createTestEntry() {
        final LexicalEntry e=new LexicalEntry();
        e.setId("http://test.org/entry");
        return e;
    }


    private LexiconOntologyWriter getWriter(final EntityManager m){
        //a lexicon has to be already present in the ontology
        m.getTransaction().begin();
        m.persist(lexicon);
        m.getTransaction().commit();
        return new LexiconOntologyWriter(m, posProvider);
    }

    @Test
    void shouldStoreEntryIntoTheLexicon(){
        write(entry, inMemoryEntityManagerFactory);

        final EntityManager m1= inMemoryEntityManagerFactory.createEntityManager();
        final Lexicon actualLexicon=m1.createNativeQuery("SELECT ?iri WHERE {?iri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <"+ Lime.LEXICON_CLASS+"> }", Lexicon.class)
                .getSingleResult();
        assertEquals(lexicon.getId(), actualLexicon.getId());

        final Iterator<LexicalEntry> actualEntries=m1.createNativeQuery("SELECT ?x WHERE {<"+lexicon.getId()+"> <"+Lime.ENTRY_OBJ_PROPERTY+"> ?x}",
                        LexicalEntry.class).getResultList().iterator();
        final LexicalEntry actualEntry=actualEntries.next();
        assertEquals(entry.getId(), actualEntry.getId());
        assertFalse(actualEntries.hasNext());
    }

    /**
     * Persist an entry into the ontology
     * @param e the entry
     * @param emf the {@link EntityManagerFactoryHelper} concerning the ontology
     */
    private void write(final LexicalEntry e, final EntityManagerFactoryHelper emf){
        final EntityManager m0=emf.createEntityManager();
        final LexiconOntologyWriter w=getWriter(m0);
        m0.getTransaction().begin();
        w.accept(e);
        m0.getTransaction().commit();
    }

    @Test
    void shouldStoreEntryPartOfSpeech(){
        entry.setPartOfSpeech(posProvider.getNoun());
        write(entry, inMemoryEntityManagerFactory);

        final EntityManager m1= inMemoryEntityManagerFactory.createEntityManager();
        final LexicalEntry actualEntry=m1.find(LexicalEntry.class, entry.getId());
        assertEquals(posProvider.getNoun().getId(), actualEntry.getPartOfSpeech().getId());
    }

    @Test
    void shouldStoreEntryCanonicalForm(){
        final Form f=new Form();
        f.setId("http://test.org/form");
        f.setWrittenRep("written rep");
        entry.setCanonicalForm(f);
        write(entry, inMemoryEntityManagerFactory);

        final EntityManager m1= inMemoryEntityManagerFactory.createEntityManager();
        final LexicalEntry actualEntry=m1.find(LexicalEntry.class, entry.getId());
        assertEquals(actualEntry.getCanonicalForm().getId(), f.getId());
        assertEquals(actualEntry.getCanonicalForm().getWrittenRep(), f.getWrittenRep());
    }

    @Test
    void testStoringMultipleEntriesOnFile(){
        try(final EntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper("testontology.ttl");
            final EntityManager m0=emf.createEntityManager()) {

            final LexiconOntologyWriter w = getWriter(m0);

            final Form f=new Form();
            f.setId("http://test.org/form");

            entry.setPartOfSpeech(posProvider.getNoun());
            entry.setCanonicalForm(f);

            final LexicalEntry entry1 = new LexicalEntry();
            entry1.setId("http://test.org/entry1");
            entry1.setPartOfSpeech(posProvider.getNoun());
            entry1.setCanonicalForm(f);

            m0.getTransaction().begin();
            w.accept(entry);
            w.accept(entry1);
            m0.getTransaction().commit();

            try(final EntityManagerFactoryHelper emfForCheck=new FileEntityManagerFactoryHelper("testontology.ttl")) {
                final EntityManager em=emfForCheck.createEntityManager();
                final Iterator<LexicalEntry> actualEntriesIt=em.createNativeQuery("SELECT ?e WHERE{"+
                        "<"+lexicon.getId()+"> <"+Lime.ENTRY_OBJ_PROPERTY+"> ?e}"+
                        " ORDER BY ?e", LexicalEntry.class).getResultList().iterator();
                final LexicalEntry actualEntry=actualEntriesIt.next();
                assertEquals(entry.getId(), actualEntry.getId());
                assertEquals(f.getId(), actualEntry.getCanonicalForm().getId());
                final LexicalEntry actualEntry1=actualEntriesIt.next();
                assertEquals(entry1.getId(), actualEntry1.getId());
                assertEquals(f.getId(), actualEntry1.getCanonicalForm().getId());
                assertFalse(actualEntriesIt.hasNext());
            }

        } finally {
            new File("testontology.ttl").delete();
        }
    }

}
