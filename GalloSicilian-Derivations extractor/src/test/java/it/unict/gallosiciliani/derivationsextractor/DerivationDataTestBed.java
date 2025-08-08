package it.unict.gallosiciliani.derivationsextractor;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.MultilingualString;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.InMemoryEntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.ReasonerFactoryWithTbox;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DerivationDataTestBed implements AutoCloseable{

    private static final String NS="http://test.org/derivationDataReaderTest#";
    private static final String ENTRY_LANG = "und";
    public final LinguisticPhenomenon p=createLinguisticPhenomenon("p");
    public final LinguisticPhenomenon q=createLinguisticPhenomenon("q");
    public final LinguisticPhenomenaProvider lpProvider=new LinguisticPhenomenaProvider(List.of(p,q));

    public final LexicalEntry entryWithDerivation;
    public final List<LinguisticPhenomenonOccurrence> derivation;
    public final LexicalObject intermediateForm=new LexicalObject();
    public final LexicalEntry entryWithEtymonButNoDerivation;
    public final LexicalEntry entryWithNoEtymology;

    private final LinguisticPhenomena liph;
    private final EntityManagerFactoryHelper factory=new InMemoryEntityManagerFactoryHelper(Map.of(OntoDriverProperties.REASONER_FACTORY_CLASS, ReasonerFactoryWithTbox.class.getName()));
    public final EntityManager entityManager;

    public DerivationDataTestBed() throws IOException {
        intermediateForm.setWrittenRep(new MultilingualString().set(ENTRY_LANG, "intermediate form"));

        entryWithDerivation=createEntry("1");
        addEtymology(entryWithDerivation);
        derivation=addDerivation(entryWithDerivation);

        entryWithEtymonButNoDerivation=createEntry("2");
        addEtymology(entryWithEtymonButNoDerivation);

        entryWithNoEtymology=createEntry("3");

        liph=new LinguisticPhenomena();
        ReasonerFactoryWithTbox.theInstance().setTBox(liph.getModel());
        entityManager = factory.createEntityManager();


        entityManager.getTransaction().begin();
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


    /**
     * Create a {@link LinguisticPhenomenon} for test purposes
     *
     * @param phenomenonId a unique identifier for linguistic phenomenon
     * @return a linguistic phenomenon
     */
    private static LinguisticPhenomenon createLinguisticPhenomenon(String phenomenonId) {
        final LinguisticPhenomenon p=new LinguisticPhenomenon();
        p.setId("phenomenon"+phenomenonId);
        p.setLabel("phenomenon "+phenomenonId);
        p.setComment("Comment for phenomenon "+phenomenonId);
        return p;
    }

    private static LexicalEntry createEntry(final String entryId) {
        final String iri=NS+"entry"+entryId;
        final LexicalEntry entry=new LexicalEntry();
        entry.setId(iri);
        entry.setCanonicalForm(new Form());
        entry.getCanonicalForm().setId(iri+"form");
        entry.getCanonicalForm().setWrittenRep(new MultilingualString().set(ENTRY_LANG, "entry "+entryId));
        return entry;
    }

    /**
     * @param entry the entry to which the etymology will be added
     */
    private static void addEtymology(final LexicalEntry entry){
        final Form etymon=new Form();
        etymon.setId(entry.getId()+"etymon");
        etymon.setWrittenRep(new MultilingualString().set(ENTRY_LANG, "etymon"));

        final Etymology etymology=new Etymology();
        entry.setEtymology(Collections.singleton(etymology));
        etymology.setStartingLink(new EtyLink());
        etymology.getStartingLink().setEtySubSource(Collections.singleton(etymon));
    }

    private List<LinguisticPhenomenonOccurrence> addDerivation(final LexicalEntry entry){
        final Form etymon=entry.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator().next();
        final LinguisticPhenomenonOccurrence o1=new LinguisticPhenomenonOccurrence();
        o1.setId(NS+"o1");
        o1.setSource(etymon);
        o1.setTarget(intermediateForm);
        o1.setOccurrenceOf(p);

        final LinguisticPhenomenonOccurrence o2=new LinguisticPhenomenonOccurrence();
        o2.setId(NS+"o2");
        o2.setSource(intermediateForm);
        o2.setTarget(entry.getCanonicalForm());
        o2.setOccurrenceOf(q);
        return List.of(o2, o1);
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

        entityManager.getTransaction().commit();
        factory.close();
        liph.close();
    }

}
