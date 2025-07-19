package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.vocabulary.RDF;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lime;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.resultset.ResultSetReader;
import org.apache.jena.riot.resultset.ResultSetReaderRegistry;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.apache.jena.sparql.resultset.SPARQLResult;
import org.apache.jena.vocabulary.DCTerms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link SPARQLService}
 * @author Cristiano Longo
 */
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SPARQLServiceTest {
    @Autowired
    SPARQLService sparqlService;

    @Autowired
    EntityManager entityManager;

    private final Lexicon l1=createTestLexicon();
    private final Lexicon l2=createTestLexicon();
    private final Lexicon l3=createTestLexicon();
    private int n=1;

    private Lexicon createTestLexicon(){
        final Lexicon l=new Lexicon();
        l.setId("http://test.org/lexicon"+n);
        l.setTitle("Lexicon "+n);
        n++;
        return l;
    }

    /**
     * Select query with a single variable and result in CSV
     */
    @Test
    void shouldConvertResultSetWithSingleVariableToCSV() throws SPARQLQueryException, IOException {
        shouldConvertResultSetWithSingleVariable(ResultsFormat.FMT_RS_CSV);
    }

    /**
     * Select query with a single variable and result in XML
     */
    @Test
    void shouldConvertResultSetWithSingleVariableToXML() throws SPARQLQueryException, IOException {
        shouldConvertResultSetWithSingleVariable(ResultsFormat.FMT_RS_XML);
    }

    /**
     * Select query with a single variable and result in JSON
     */
    @Test
    void shouldConvertResultSetWithSingleVariableToJSON() throws SPARQLQueryException, IOException {
        shouldConvertResultSetWithSingleVariable(ResultsFormat.FMT_RS_JSON);
    }

    /**
     * Select query with a single variable
     */
    private void shouldConvertResultSetWithSingleVariable(final ResultsFormat resultFormat) throws SPARQLQueryException, IOException {
        PersistenceTestUtils.build().persist(l1).persist(l2).persist(l3).execute(entityManager);
        try {
            final String actual=sparqlService.query("SELECT ?x where {" +
                    "?x a <" + Lime.LEXICON_CLASS + "> ." +
                    "} ORDER BY ?x", resultFormat);
            try(final InputStream is= IOUtils.toInputStream(actual, StandardCharsets.UTF_8)){
                final ResultSet actualRs=ResultSetFactory.load(is, resultFormat);
                final QuerySolution s1=actualRs.next();
                assertEquals(s1.get("x").toString(), l1.getId());
                final QuerySolution s2=actualRs.next();
                assertEquals(s2.get("x").toString(), l2.getId());
                final QuerySolution s3=actualRs.next();
                assertEquals(s3.get("x").toString(), l3.getId());
                assertFalse(actualRs.hasNext());
            }
        }finally {
            PersistenceTestUtils.build().remove(l3).remove(l2).remove(l1).execute(entityManager);
        }
    }

    /**
     * Assuming that in the knowledge base there are the three default lexica.
     */
    @Test
    void shouldConvertResultSetWithMultipleVariablesToCSV() throws SPARQLQueryException {

        final String expected ="x,title\r\n"+
                l1.getId()+","+l1.getTitle()+"\r\n"+
                l2.getId()+","+l2.getTitle()+"\r\n"+
                l3.getId()+","+l3.getTitle()+"\r\n";

        PersistenceTestUtils.build().persist(l1).persist(l2).persist(l3).execute(entityManager);
        try {
            final String actual = sparqlService.query("SELECT ?x ?title where {" +
                    "?x a <" + Lime.LEXICON_CLASS + "> ." +
                    "?x <" + DCTerms.NS + "title> ?title " +
                    "} ORDER BY ?x", ResultsFormat.FMT_RS_CSV);
            assertEquals(expected, actual);
        } finally {
            PersistenceTestUtils.build().remove(l3).remove(l2).remove(l1).execute(entityManager);
        }
    }

    @Test
    public void shouldThrowExceptionOnWrongQuery() {
        final String query = "Not a sparql query";
        final SPARQLQueryException e = assertThrows(SPARQLQueryException.class, ()->sparqlService.query(query, ResultsFormat.FMT_RS_CSV));
        assertEquals(query, e.getQuery());
        assertNotNull(e.getCause());
    }

    @Test
    public void askQueryCSV() throws SPARQLQueryException, IOException {
        PersistenceTestUtils.build().persist(l1).execute(entityManager);
        final ResultSetReader readerCSV=ResultSetReaderRegistry.getFactory(Lang.CSV).create(Lang.CSV);
        try {
            final String actual = sparqlService.query("ASK {?x <"+ RDF.TYPE+"> <"+Lime.LEXICON_CLASS+">}",
                    ResultsFormat.FMT_RS_CSV);
            try(final InputStream is=IOUtils.toInputStream(actual, StandardCharsets.UTF_8)) {
                final SPARQLResult result=readerCSV.readAny(is, null);
                assertTrue(result.isBoolean());
                assertTrue(result.getBooleanResult());
            }
        } finally {
            PersistenceTestUtils.build().remove(l1).execute(entityManager);
        }
    }

    @Test
    public void describeQuery() throws SPARQLQueryException {
        PersistenceTestUtils.build().persist(l1).execute(entityManager);
        try{
            final String actual=sparqlService.query("DESCRIBE ?x WHERE {?x <"+DCTerms.NS+"title> \""+l1.getTitle()+"\"}", ResultsFormat.FMT_RDF_TTL);
            final Model actualModel= ModelFactory.createDefaultModel();
            RDFParser.fromString(actual, Lang.TTL).parse(actualModel);
            assertNotNull(actualModel.getResource(l1.getId()));
        } finally {
            PersistenceTestUtils.build().remove(l1).execute(entityManager);
        }


    }
}
