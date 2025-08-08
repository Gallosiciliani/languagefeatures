package it.unict.gallosiciliani.gs.derivationsextractor;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.LemonEty;
import it.unict.gallosiciliani.liph.util.DerivationChainRetriever;

import java.util.Iterator;
import java.util.List;

/**
 * Produce {@link DerivationRawData} from suitable entries
 * in the knowledge base.
 *
 * @author Cristiano Longo
 */
public class DerivationDataReader implements Iterator<DerivationRawData> {

    private final Iterator<LexicalEntry> entriesIt;
    private final EntityManager entityManager;
    private final LinguisticPhenomenaProvider lpProvider;

    public DerivationDataReader(final EntityManager entityManager, final LinguisticPhenomenaProvider lpProvider){
        this.entityManager=entityManager;
        this.lpProvider=lpProvider;
        final TypedQuery<LexicalEntry> q= entityManager.createNativeQuery("SELECT DISTINCT ?x WHERE {" +
                "\t?x <"+ LemonEty.ETYMOLOGY_OBJ_PROPERTY+"> ?etymology . \n"+
                "} ORDER BY ?x", LexicalEntry.class);
        entriesIt=q.getResultStream().iterator();
    }

    @Override
    public boolean hasNext() {
        return entriesIt.hasNext();
    }

    @Override
    public DerivationRawData next() {
        final LexicalEntry e=entriesIt.next();
        final List<LinguisticPhenomenonOccurrence> derivationChain= DerivationChainRetriever.retrieve(e, entityManager, lpProvider);

        return new DerivationRawData() {
            @Override
            public LexicalEntry getEntry() {
                return e;
            }

            @Override
            public List<LinguisticPhenomenonOccurrence> getDerivationChain() {
                return derivationChain;
            }
        };
    }
}
