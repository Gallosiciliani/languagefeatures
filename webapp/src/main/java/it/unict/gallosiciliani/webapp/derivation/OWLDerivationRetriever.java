package it.unict.gallosiciliani.webapp.derivation;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Ontolex;
import it.unict.gallosiciliani.liph.model.LexicalObject;

import cz.cvut.kbss.jopa.model.query.Query;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Retrieve derivations, based on GSFeatures, from the knowledge base
 * @author Cristiano Longo
 */
public class OWLDerivationRetriever {

    private final Collection<LinguisticPhenomenon> recognizedPhenomena;
    private final Set<String> recognizedPhenomenaIRIs=new TreeSet<>();
    private final EntityManager entityManager;

    public OWLDerivationRetriever(final Collection<LinguisticPhenomenon> recognizedPhenomena, final EntityManager entityManager){
        this.recognizedPhenomena=recognizedPhenomena;
        recognizedPhenomena.forEach((p)->recognizedPhenomenaIRIs.add(p.getId()));
        this.entityManager=entityManager;

    }

    /**
     * Extract from knowledge base the derivation from etymon to target
     *
     * @param etymon form representing the etymon
     * @param lemma derived lemma
     * @return the path from etymon to lemma, if exists
     * NOTE: there we are assuming that there is at most one path from etymon to target.
     */
    public DerivationPathNode retrieve(final Form etymon, final Form lemma){
        return retrieveDerivation(etymon, lemma.getId());
    }

    /**
     * Find a derivation starting from etymon and ending with the {@link LexicalObject} with the specified IRI
     *
     * @param etymon         the origin etymon
     * @param destinationIRI IRI of the {@link LexicalObject} representing the derivation end
     * @return the derivation extended with an additional arc
     */
    private DerivationPathNode retrieveDerivation(final Form etymon, final String destinationIRI){
        if (destinationIRI.equals(etymon.getId()))
            return new DerivationPathNodeImpl(etymon.getWrittenRep().get());
        // etymon --...-> x -- p -> derivedForm --...-> lemma
        final Query q=entityManager.createNativeQuery("SELECT ?x ?r ?p WHERE {\n"+
                "\t<"+etymon.getId()+"> <"+ LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> ?x .\n"+
                "\t?x ?p <"+ destinationIRI +"> . \n"+
                "\t?p <"+ RDFS.SUB_PROPERTY_OF+"> <"+LinguisticPhenomena.DERIVES_OBJ_PROPERTY+"> .\n"+
                "\t?x <"+ Ontolex.WRITTEN_REP_DATA_PROPERTY+"> ?r \n"+
                "}");
        final List<Object> results=q.getResultList();
        for(final Object resultRowObject: results){
            final Object[] resultRow=(Object[]) resultRowObject;
            final String xValue=resultRow[0].toString();
            final String rValue=resultRow[1].toString();
            final String pValue=resultRow[2].toString();
            for(final LinguisticPhenomenon recognizedPhenomenon: recognizedPhenomena)
                if (recognizedPhenomenon.getId().equals(pValue)){
                    DerivationPathNode prev=retrieveDerivation(etymon, xValue);
                    return prev==null ? null : new DerivationPathNodeImpl(rValue, recognizedPhenomenon, prev);
                }
        }
        return null;
    }
}
