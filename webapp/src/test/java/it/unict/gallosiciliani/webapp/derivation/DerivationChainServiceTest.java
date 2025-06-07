package it.unict.gallosiciliani.webapp.derivation;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.webapp.TestUtil;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

/**
 * Test for {@link DerivationService}, but specific for derivation chains
 * @author Cristiano Longo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DerivationChainServiceTest {


    @Autowired
    DerivationService derivationService;

    @Autowired
    EntityManager entityManager;

    @MockBean
    LinguisticPhenomenaProvider lpProvider;

    @Test
    void shouldRetrieveTheDerivationChain(){
        final TestUtil util=new TestUtil();
        final Form x=util.createForm();
        final LinguisticPhenomenon p=util.createPhenomenon();
        final LexicalObject y=util.createLexicalObject();
        final LinguisticPhenomenonOccurrence op=util.createPhenomenonOccurrence(p, x, y, true);
        final LinguisticPhenomenon q=util.createPhenomenon();
        final Form z=util.createForm();
        final LinguisticPhenomenonOccurrence oq=util.createPhenomenonOccurrence(q, y, z, true);
        when(lpProvider.getById(p.getId())).thenReturn(p);
        when(lpProvider.getById(q.getId())).thenReturn(q);

        final PersistenceTestUtils persistence= PersistenceTestUtils.build().
                persist(x).
                persist(p).
                persist(y).
                persist(op).
                persist(q).
                persist(z).
                persist(oq);
        persistence.execute(entityManager);

        assertDerives(x,x);
        assertDerives(x,y);
        assertDerives(y,z);
        assertDerives(z,z);

        final Iterator<LinguisticPhenomenonOccurrence> actualIt=derivationService.getDerivationChain(z, x).iterator();
        util.checkEquals(oq, actualIt.next());
        util.checkEquals(op, actualIt.next());
    }

    /**
     * Check whether the source is bound to the target through the derives property
     * @param source derives subject
     * @param target derives object
     */
    private void assertDerives(final LexicalObject source, final LexicalObject target){
        final TypedQuery<Boolean> askDerivesQuery=entityManager.createNativeQuery("ASK { ?source <"+ LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> ?target}",
                Boolean.class);
        askDerivesQuery.setParameter("source", source);
        askDerivesQuery.setParameter("target", target);
        final Iterator<Boolean> askResultIt=askDerivesQuery.getResultList().iterator();
        assertEquals(Boolean.TRUE, askResultIt.next());
        assertFalse(askResultIt.hasNext());
    }

}
