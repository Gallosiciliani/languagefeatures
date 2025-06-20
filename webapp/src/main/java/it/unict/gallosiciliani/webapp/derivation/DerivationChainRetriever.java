package it.unict.gallosiciliani.webapp.derivation;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Getter;

import java.util.*;

/**
 * Utility object to retrieve derivation chains from the knowledge base
 * @author Cristiano Longo
 */
public class DerivationChainRetriever {

    private final Form etymon;
    /**
     * -- GETTER --
     *  Get the linguistic phenomena occurrences in the chain from etymon to lemma, but in reversed
     *  order
     *
     */
    @Getter
    private final List<LinguisticPhenomenonOccurrence> occurrencesSorted;

    public DerivationChainRetriever(final Form lemma, final Form etymon, final EntityManager entityManager){
        this.etymon=etymon;
        final TypedQuery<LinguisticPhenomenonOccurrence> query=entityManager.createNativeQuery("SELECT ?o WHERE {\n"+
                "\t?etymon <"+ LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> ?x .\n"+
                "\t?o <"+ LinguisticPhenomena.SOURCE_OBJ_PROPERTY+"> ?x ;\n"+
                "\t\t <"+ LinguisticPhenomena.TARGET_OBJ_PROPERTY+"> ?y .\n"+
                "\t?y <"+ LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> ?lemma "
                +"}", LinguisticPhenomenonOccurrence.class);
        query.setParameter("etymon", etymon);
        query.setParameter("lemma", lemma);

        final Map<LexicalObject, LinguisticPhenomenonOccurrence> targetToOccurrence=new TreeMap<>(Thing.COMPARATOR_BY_IRI);
        query.getResultStream().forEach((o)->{
            if (targetToOccurrence.put(o.getTarget(), o)!=null)
                throw new UnsupportedOperationException("Multiple linguistic phenomena occurrences with the same target in the derivation from "+etymon+" to "+lemma);
        });
        occurrencesSorted=targetToOccurrence.isEmpty()? Collections.emptyList() : getOccurrencesSorted(lemma, targetToOccurrence);
    }

    /**
     * Get the chain of linguistic phenomena occurrences in the chain from etymon to lemma, but in reversed
     * order
     * @return a list of {@link LinguisticPhenomenonOccurrence}
     */
    private List<LinguisticPhenomenonOccurrence> getOccurrencesSorted(final LexicalObject target, final Map<LexicalObject, LinguisticPhenomenonOccurrence> targetToOccurrence){
        final LinguisticPhenomenonOccurrence o=targetToOccurrence.get(target);
        final List<LinguisticPhenomenonOccurrence> res=o.getSource().getId().equals(etymon.getId()) ? new LinkedList<>() : getOccurrencesSorted(o.getSource(), targetToOccurrence);
        res.add(0, o);
        return res;
    }

}
