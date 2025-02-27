package it.unict.gallosiciliani.importing.pdf.writer;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.model.persistence.FileEntityManagerFactoryHelper;
import it.unict.gallosiciliani.importing.pdf.writing.NicosiaSperlingaEntityManagerFactory;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.lime.Lime;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link NicosiaSperlingaEntityManagerFactory}
 *
 * @author Cristiano Longo
 */
public class NicosiaSperlingaEntityManagerFactoryTest {

    @Test
    void shouldCreateFileContainingTheLexicon() throws IOException {
        final String testFilePath="testNicosiaSperlingaOntologyWriter.ttl";
        final File f=new File(testFilePath);
        assertFalse(f.exists());
        //create the file with the ontology cotaining just the lexicon
        try(final NicosiaSperlingaEntityManagerFactory ignored =new NicosiaSperlingaEntityManagerFactory(testFilePath)){
            assertTrue(f.exists());
            //check that the lexicon exists
            try(final FileEntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper(testFilePath)){
                final EntityManager em=emf.createEntityManager();
                final String queryStr="SELECT ?iri WHERE {?iri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <"+Lime.LEXICON_CLASS+"> }";
                final TypedQuery<Lexicon> q=em.createNativeQuery(queryStr,
                        Lexicon.class);
                //check that the lexicon exists and it is unique
                final Lexicon actualLexicon=q.getSingleResult();
                assertEquals(NicosiaSperlingaEntityManagerFactory.NS+"lexicon", actualLexicon.getId());
                em.close();
            }
        }finally{
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }
    }

    @Test
    void testMultipleCommits(){
        try(final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu", Map.of(
                JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.model",
                JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName(),
                JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName(),
                JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.FILE,
                JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "testontology.ttl"));
            final EntityManager em=emf.createEntityManager()) {
            final LexicalEntry e1 = new LexicalEntry();
            e1.setId("http://test.org/e1");

            em.getTransaction().begin();
            em.persist(e1);
            em.getTransaction().commit();

            final LexicalEntry e2 = new LexicalEntry();
            e2.setId("http://test.org/e2");

            em.getTransaction().begin();
            em.persist(e2);
            em.getTransaction().commit();
        } finally{
            new File("testontology.ttl").delete();
        }
    }
}
