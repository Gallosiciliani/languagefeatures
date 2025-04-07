package it.unict.gallosiciliani.webapp.derivation;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.webapp.TestUtil;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link OWLDerivationRetriever}
 * @author Cristiano Longo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OWLDerivationRetrieverTest {

    @Autowired
    EntityManager entityManager;

    private final TestUtil util=new TestUtil();

    private OWLDerivationRetriever getRetriever(final Collection<LinguisticPhenomenon> recognizedPhenomena){
        return new OWLDerivationRetriever(recognizedPhenomena, entityManager);
    }

    /**
     * The functionality has not been developed yet
     */
    @Test
    @Disabled
    void shouldRetrieveDerivation(){
        // derivation x<-p-y<-q-z
        final Form x=util.createForm();
        final LinguisticPhenomenon p=util.createPhenomenon();
        final LexicalObject y=util.createLexicalObject();
        final LinguisticPhenomenon q=util.createPhenomenon();
        final Form z=util.createForm();

        final PersistenceTestUtils persistence= PersistenceTestUtils.build().
                persist(x).
                persist(p).
                persist(y).
                persist(x, p, y).
                persist(q).
                persist(z).
                persist(y, q, z);

        persistence.execute(entityManager);

        final DerivationPathNode actual0=getRetriever(List.of(p,q)).retrieve(x,z);
        assertEquals(x.getWrittenRep(), actual0.get());
        assertSame(p, actual0.getLinguisticPhenomenon());
        final DerivationPathNode actual1=actual0.prev();
        assertEquals(y.getWrittenRep(), actual1.get());
        assertSame(q, actual1.getLinguisticPhenomenon());
        final DerivationPathNode actual2=actual1.prev();
        assertEquals(z.getWrittenRep(), actual2.get());
        assertNull(actual2.getLinguisticPhenomenon());
        assertNull(actual2.prev());
    }

}
