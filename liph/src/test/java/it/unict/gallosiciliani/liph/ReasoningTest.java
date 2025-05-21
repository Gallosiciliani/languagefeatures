package it.unict.gallosiciliani.liph;


import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ReasoningTest{

    final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu", Map.of(
            JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.liph.model",
            JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName(),
            JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName(),
            JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "not-relevant-for-in-memory-storage",
            JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.IN_MEMORY,
            OntoDriverProperties.REASONER_FACTORY_CLASS, TestReasonerFactory.class.getName()));

    final EntityManager entityManager=emf.createEntityManager();

    final Form x=new Form();
    final LexicalObject y=new LexicalObject();
    final Form z=new Form();
    final LinguisticPhenomenonOccurrence x2y=new LinguisticPhenomenonOccurrence();
    final LinguisticPhenomenonOccurrence y2z=new LinguisticPhenomenonOccurrence();

    ReasoningTest(){
        x.setId("http://test.org/x");
        y.setId("http://test.org/y");
        z.setId("http://test.org/z");
        x2y.setId("http://test.org/x2y");
        x2y.setSource(x);
        x2y.setTarget(y);
        y2z.setId("http://test.org/y2z");
        y2z.setSource(y);
        y2z.setTarget(z);
    }

    /**
     * Check whether the source is bound to the target through the derives property
     * @param source derives subject
     * @param target derives object
     */
    private void assertDerives(final LexicalObject source, final LexicalObject target){
        final TypedQuery<Boolean> askDerivesQuery=entityManager.createNativeQuery("ASK { ?source <"+LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> ?target}",
                Boolean.class);
        askDerivesQuery.setParameter("source", source);
        askDerivesQuery.setParameter("target", target);
        final Iterator<Boolean> askResultIt=askDerivesQuery.getResultList().iterator();
        assertEquals(Boolean.TRUE, askResultIt.next());
        assertFalse(askResultIt.hasNext());
    }

    @Test
    void testDerivesReflexivity(){
        x.setDerives(Set.of(y));
        entityManager.getTransaction().begin();
        entityManager.persist(y);
        entityManager.persist(x);
        entityManager.getTransaction().commit();
        assertDerives(x,x);
    }

    @Test
    void testDerivesTransitivity(){
        x.setDerives(Set.of(y));
        y.setDerives(Set.of(z));
        entityManager.getTransaction().begin();
        entityManager.persist(z);
        entityManager.persist(y);
        entityManager.persist(x);
        entityManager.getTransaction().commit();

        assertDerives(x, z);
    }

    @Test
    void testPhenomenonOccurrenceRoleChain(){
        entityManager.getTransaction().begin();
        entityManager.persist(x);
        entityManager.persist(y);
        entityManager.persist(x2y);
        entityManager.getTransaction().commit();

        assertDerives(x,y);
    }

    @Disabled // see github.com/Gallosiciliani/openllet-jena5/issues/2
    @Test
    void testPhenomenonOccurrenceRoleChainAndTransitivity(){
//        x.setDerives(Set.of(y));
        entityManager.getTransaction().begin();
        entityManager.persist(z);
        entityManager.persist(y);
        entityManager.persist(x);
        entityManager.persist(x2y);
        entityManager.persist(y2z);
        entityManager.getTransaction().commit();

        assertDerives(x,y);
        assertDerives(y,z);
        assertDerives(x,z);
    }
}
