package it.unict.gallosiciliani.gs.derivationsextractor;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.PartOfSpeech;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.InMemoryEntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.ReasonerFactoryWithTbox;

import java.io.IOException;
import java.util.Map;

/**
 * @author Cristiano Longo
 */

public class PersistedDerivationDataTestBed extends DerivationDataTestBed implements AutoCloseable{


    private final LinguisticPhenomena liph;
    private final EntityManagerFactoryHelper factory=new InMemoryEntityManagerFactoryHelper(Map.of(OntoDriverProperties.REASONER_FACTORY_CLASS, ReasonerFactoryWithTbox.class.getName()));
    public final EntityManager entityManager;

    public PersistedDerivationDataTestBed() throws IOException {
        liph=new LinguisticPhenomena();
        ReasonerFactoryWithTbox.theInstance().setTBox(liph.getModel());
        entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.persist(noun);
        entityManager.persist(verb);


        persistEntry(entryWithDerivation, entityManager);
        persistEntry(entryWithEtymonButNoDerivation, entityManager);
        persistEntry(entryWithNoEtymology, entityManager);

        //persist the intermediate form
        entityManager.persist(derivation.iterator().next().getSource());
        //persist all the derivation
        for (final LinguisticPhenomenonOccurrence o : derivation)
            entityManager.persist(o);

        entityManager.getTransaction().commit();

    }

    private static void persistEntry(final LexicalEntry entry, final EntityManager entityManager){
        entry.setPartOfSpeech(null);
        entityManager.persist(entry);
        entityManager.persist(entry.getCanonicalForm());
        if (entry.getEtymology()!=null && !entry.getEtymology().isEmpty())
            entityManager.persist(entry.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator().next());
    }

    private static void removeEntry(final LexicalEntry entry, final EntityManager entityManager){
        if (entry.getEtymology()!=null && !entry.getEtymology().isEmpty()){
            final Form etymon=entityManager.merge(entry.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator().next());
            entityManager.remove(etymon);
        }
        final Form canonicalForm=entityManager.merge(entry.getCanonicalForm());
        entityManager.remove(canonicalForm);
        final LexicalEntry entryStored=entityManager.merge(entry);
        entityManager.remove(entryStored);
    }



    @Override
    public void close(){
        entityManager.getTransaction().begin();
        //delete the derivation

        for(final LinguisticPhenomenonOccurrence o: derivation) {
            final LinguisticPhenomenonOccurrence occurrenceStored=entityManager.merge(o);
            entityManager.remove(occurrenceStored);
        }
        //delete the intermediate form
        final LexicalObject intermediateForm=entityManager.merge(derivation.iterator().next().getSource());
        entityManager.remove(intermediateForm);

        //delete entries
        removeEntry(entryWithNoEtymology, entityManager);
        removeEntry(entryWithEtymonButNoDerivation, entityManager);
        removeEntry(entryWithDerivation, entityManager);

        final PartOfSpeech nounStored=entityManager.merge(noun);
        entityManager.remove(nounStored);
        final PartOfSpeech verbStored=entityManager.merge(verb);
        entityManager.remove(verbStored);
        entityManager.getTransaction().commit();
        factory.close();
        liph.close();
    }

}
