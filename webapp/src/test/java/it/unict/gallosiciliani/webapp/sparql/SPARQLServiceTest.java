package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.lime.Lime;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import org.apache.jena.vocabulary.DCTerms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link SPARQLService}
 * @author Cristiano Longo
 */
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SPARQLServiceTest {
    @Autowired
    private SPARQLService sparqlService;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private EntityManager entityManager;

    /**
     * Assuming that in the knowledge base there are the three default lexica.
     */
    @Test
    void shouldConvertResultSetWithSingleVariableToCSV() throws IOException, SPARQLQueryException {
        final Lexicon l1=new Lexicon();
        l1.setId("http://test.org/lexicon1");
        final Lexicon l2=new Lexicon();
        l2.setId("http://test.org/lexicon2");
        final Lexicon l3=new Lexicon();
        l3.setId("http://test.org/lexicon3");

        final String expected ="x\r\n"+
                l1.getId()+"\r\n"+
                l2.getId()+"\r\n"+
                l3.getId()+"\r\n";

        PersistenceTestUtils.build().persist(l1).persist(l2).persist(l3).execute(txManager, entityManager);

        try {
            final String actual = sparqlService.performSelectQuery("SELECT ?x where {" +
                    "?x a <" + Lime.LEXICON_CLASS + "> ." +
                    "} ORDER BY ?x");
            assertEquals(expected, actual);
        }finally {
            PersistenceTestUtils.build().remove(l3).remove(l2).remove(l1).execute(txManager, entityManager);
        }
    }

    /**
     * Assuming that in the knowledge base there are the three default lexica.
     */
    @Test
    void shouldConvertResultSetWithMultipleVariablesToCSV() throws IOException, SPARQLQueryException {
        final Lexicon l1=new Lexicon();
        l1.setId("http://test.org/lexicon1");
        l1.setTitle("Lexicon 1");
        final Lexicon l2=new Lexicon();
        l2.setId("http://test.org/lexicon2");
        l2.setTitle("Lexicon 2");
        final Lexicon l3=new Lexicon();
        l3.setId("http://test.org/lexicon3");
        l3.setTitle("Lexicon 3");

        final String expected ="x,title\r\n"+
                l1.getId()+","+l1.getTitle()+"\r\n"+
                l2.getId()+","+l2.getTitle()+"\r\n"+
                l3.getId()+","+l3.getTitle()+"\r\n";

        PersistenceTestUtils.build().persist(l1).persist(l2).persist(l3).execute(txManager, entityManager);
        try {
            final String actual = sparqlService.performSelectQuery("SELECT ?x ?title where {" +
                    "?x a <" + Lime.LEXICON_CLASS + "> ." +
                    "?x <" + DCTerms.NS + "title> ?title " +
                    "} ORDER BY ?x");
            assertEquals(expected, actual);
        } finally {
            PersistenceTestUtils.build().remove(l3).remove(l2).remove(l1).execute(txManager, entityManager);
        }
    }

    @Test
    public void shouldThrowExceptionOnWrongQuery() {
        final String query = "Not a sparql query";
        final SPARQLQueryException e = assertThrows(SPARQLQueryException.class, ()->sparqlService.performSelectQuery(query));
        assertEquals(query, e.getQuery());
        assertNotNull(e.getCause());
    }
}