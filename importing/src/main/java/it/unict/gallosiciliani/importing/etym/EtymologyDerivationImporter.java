package it.unict.gallosiciliani.importing.etym;

import cz.cvut.kbss.jopa.exceptions.NoUniqueResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.MultilingualString;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
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
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Cristiano Longo
 */
@Slf4j
class EtymologyDerivationImporter {
    private final EntityManager entityManager;
    private final IRIProvider iriProvider;
    private final String etymonLanguageTag;

    @Getter
    private LexicalEntry lemmaEntry;
    @Getter
    private EtymologyIRIProvider etymologyIRIProvider;
    @Getter
    private Form etymon;

    EtymologyDerivationImporter(final EntityManager entityManager, final IRIProvider iriProvider,
                                final String etymonLanguageTag){
        this.entityManager=entityManager;
        this.iriProvider=iriProvider;
        this.etymonLanguageTag=etymonLanguageTag;
    }

    /**
     * Create and store the etymon individual
     * @param writtenRep etymon written representation
     * @return a {@link Form} individual
     */
    private Form createEtymon(final String writtenRep){
        final Form etymon=new Form();
        etymon.setId(etymologyIRIProvider.getEtySubSourceIRI());
        etymon.setWrittenRep(new MultilingualString().set(etymonLanguageTag, writtenRep));
        entityManager.persist(etymon);
        return etymon;
    }

    void importDerivation(final DerivationPathNode n){
        final TypedQuery<LexicalEntry> query=entityManager.createNativeQuery("SELECT ?x WHERE {?x <"+ Ontolex.CANONICAL_FORM_OBJ_PROPERTY+"> ?f . "+
                "?f <"+Ontolex.WRITTEN_REP_DATA_PROPERTY+"> ?w . FILTER (STR(?w)=\""+n.get()+"\") }", LexicalEntry.class);
        try {
            lemmaEntry = query.getSingleResult();
        }catch(NoUniqueResultException e){
            log.error("Multiple entries for the single form {}", n.get());
            lemmaEntry=query.getResultList().get(0);
        }
        etymologyIRIProvider=iriProvider.getLexicalEntryIRIs(lemmaEntry).getEtymologyIRIs();
        importDerivation(n, (writtenRep)-> lemmaEntry.getCanonicalForm());
    }

    private LexicalObject importDerivation(final DerivationPathNode n, final Function<String, LexicalObject> targetProvider) {
        if (n.prev()==null){
            etymon=createEtymon(n.get());
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
            intermediateForm.setWrittenRepUndLang(sourceWrittenRep);
            entityManager.persist(intermediateForm);
            return intermediateForm;
        }));

        //due to an openellet error, it is necessary to explicitly put this assertion
        final Set<LexicalObject> derives=new HashSet<>();
        derives.add(o.getTarget());
        o.getSource().setDerives(derives);

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
            etymon=createEtymon(n.get());
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
            etymon=createEtymon(n.get());
            return etymon;
        }
        final LexicalObject intermediateForm=new LexicalObject();
        intermediateForm.setId(iris.getIntermediateFormIRI());
        intermediateForm.setWrittenRep(new MultilingualString().set(n.get()));
        entityManager.persist(intermediateForm);
        importDerivation(intermediateForm, n.prev());
        return intermediateForm;
    }

}
