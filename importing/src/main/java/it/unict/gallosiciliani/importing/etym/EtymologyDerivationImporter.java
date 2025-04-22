package it.unict.gallosiciliani.importing.etym;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.importing.iri.EtymologyIRIProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.PhenomenonOccurrenceIRIProvider;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Ontolex;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author Cristiano Longo
 */
class EtymologyDerivationImporter {
    private final EntityManager entityManager;
    private final Function<String, Form> etymonFormProvider;
    private final IRIProvider iriProvider;

    @Getter
    private LexicalEntry lemmaEntry;
    @Getter
    private EtymologyIRIProvider etymologyIRIProvider;
    @Getter
    private Form etymon;

    EtymologyDerivationImporter(final EntityManager entityManager, final IRIProvider iriProvider,
                                final Function<String, Form> etymonFormProvider){
        this.entityManager=entityManager;
        this.iriProvider=iriProvider;
        this.etymonFormProvider=etymonFormProvider;
    }

    void importDerivation(final DerivationPathNode n){
        lemmaEntry=entityManager.createNativeQuery("SELECT ?x WHERE {?x <"+ Ontolex.CANONICAL_FORM_OBJ_PROPERTY+"> ?f . "+
                "?f <"+Ontolex.WRITTEN_REP_DATA_PROPERTY+"> \""+n.get()+"\"}", LexicalEntry.class).getSingleResult();
        etymologyIRIProvider=iriProvider.getLexicalEntryIRIs(lemmaEntry).getEtymologyIRIs();
        importDerivation(n, (writtenRep)-> lemmaEntry.getCanonicalForm());
    }

    private LexicalObject importDerivation(final DerivationPathNode n, final Function<String, LexicalObject> targetProvider) {
        if (n.prev()==null){
            etymon=etymonFormProvider.apply(n.get());
            return etymon;
        }
        final PhenomenonOccurrenceIRIProvider iris=etymologyIRIProvider.getLinguisticPhenomenaOccurrencesIRIs();

        final LinguisticPhenomenonOccurrence o=new LinguisticPhenomenonOccurrence();
        o.setId(iris.getOccurrenceIRI());
        o.setOccurrenceOf(n.getLinguisticPhenomenon());
        o.setTarget(targetProvider.apply(n.get()));
        o.setSource(importDerivation(n.prev(), (sourceWrittenRep)->{
            final LexicalObject intermediateForm=new LexicalObject();
            intermediateForm.setId(iris.getIntermediateFormIRI());
            intermediateForm.setWrittenRep(sourceWrittenRep);
            entityManager.persist(intermediateForm);
            return intermediateForm;
        }));
        entityManager.persist(o);
        return o.getTarget();
    }

    /**
     *
     * @param target the occurrence target
     * @param n derivation, the terminal node should be labeled with the written representation of the target
     */
    private void importDerivation(final LexicalObject target, final DerivationPathNode n) {
        if (n.prev()==null){
            etymon=etymonFormProvider.apply(n.get());
            return;
        }
        final PhenomenonOccurrenceIRIProvider iris=etymologyIRIProvider.getLinguisticPhenomenaOccurrencesIRIs();

        final LinguisticPhenomenonOccurrence o=new LinguisticPhenomenonOccurrence();
        o.setId(iris.getOccurrenceIRI());
        o.setOccurrenceOf(n.getLinguisticPhenomenon());
        o.setTarget(target);
        o.setSource(importDerivation(n.prev(), iris));
        entityManager.persist(o);

    }


    /**
     * Import a derivation by creating a {@link LinguisticPhenomenonOccurrence} chain
     * @param n derivation
     * @param iris IRI provider
     * @return target endpoint of the derivation chain
     */
    private LexicalObject importDerivation(final DerivationPathNode n, final PhenomenonOccurrenceIRIProvider iris) {
        if (n.prev()==null){
            etymon=etymonFormProvider.apply(n.get());
            return etymon;
        }
        final LexicalObject intermediateForm=new LexicalObject();
        intermediateForm.setId(iris.getIntermediateFormIRI());
        intermediateForm.setWrittenRep(n.get());
        entityManager.persist(intermediateForm);
        importDerivation(intermediateForm, n.prev());
        return intermediateForm;
    }

}
