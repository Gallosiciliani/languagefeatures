package it.unict.gallosiciliani.importing.etym;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.importing.iri.EtymologyIRIProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.LexicalEntryIRIProvider;
import it.unict.gallosiciliani.importing.iri.PhenomenonOccurrenceIRIProvider;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.InMemoryEntityManagerFactoryHelper;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Test for {@link EtymologyImporter}
 *
 * @author Cristiano Longo
 */
public class EtymologyImporterTest {

    interface FormProvider extends Function<String, Form> {
    }

    private static final String NS="http://test.org/";

    private final Form etymonForm=createForm("etymon");
    private final LinguisticPhenomenon p=createPhenomenon("p");
    private final LinguisticPhenomenon q=createPhenomenon("q");
    private final Form lemmaForm=createForm("lemmaForm");
    private final LexicalEntry lemmaEntry=new LexicalEntry();
    private final Function<String, Form> etymonProvider=mock(FormProvider.class);
    private final IRIProvider iriProvider=mock(IRIProvider.class);
    private final PhenomenonOccurrenceIRIProvider occurrenceIRIProviderP=mock(PhenomenonOccurrenceIRIProvider.class);
    private final PhenomenonOccurrenceIRIProvider occurrenceIRIProviderQ=mock(PhenomenonOccurrenceIRIProvider.class);


    EtymologyImporterTest(){
        lemmaEntry.setId("http://test.org/lemmaEntry");
        lemmaEntry.setCanonicalForm(lemmaForm);
        when(etymonProvider.apply(etymonForm.getWrittenRep())).thenReturn(etymonForm);
        final EtymologyIRIProvider etymIriProvider=mock(EtymologyIRIProvider.class);
        final LexicalEntryIRIProvider entryIRIProvider=mock(LexicalEntryIRIProvider.class);
        when(etymIriProvider.getEtymolgyIRI()).thenReturn(NS+"etymology");
        when(etymIriProvider.getEtyLinkIRI()).thenReturn(NS+"etyLink");
        when(entryIRIProvider.getEtymologyIRIs()).thenReturn(etymIriProvider);
        when(iriProvider.getLexicalEntryIRIs(argThat((e)-> lemmaEntry.getId().equals(e.getId()))))
                .thenReturn(entryIRIProvider);
        when(occurrenceIRIProviderP.getOccurrenceIRI()).thenReturn(NS+"occurrenceOfP");
        when(occurrenceIRIProviderP.getIntermediateFormIRI()).thenReturn(NS+"intermediateForm");
        when(occurrenceIRIProviderQ.getOccurrenceIRI()).thenReturn(NS+"occurrenceOfQ");
        when(etymIriProvider.getLinguisticPhenomenaOccurrencesIRIs()).thenReturn(occurrenceIRIProviderP, occurrenceIRIProviderQ);

    }
    private Form createForm(final String id){
        final Form f=new Form();
        f.setId(NS+id);
        f.setWrittenRep("written rep for "+id);
        return f;

    }

    private LinguisticPhenomenon createPhenomenon(final String id){
        final LinguisticPhenomenon p=new LinguisticPhenomenon();
        p.setId(NS+id);
        p.setLabel(id);
        return p;
    }

    @Test
    void shouldImportDerivationAsEtymology(){
        final String intermediateFormWrittenRep = "written rep for intermediate form";
        final DerivationPathNode derivation=new DerivationPathNodeImpl(lemmaForm.getWrittenRep(), p,
                new DerivationPathNodeImpl(intermediateFormWrittenRep, q ,
                        new DerivationPathNodeImpl(etymonForm.getWrittenRep())));
        try(final EntityManagerFactoryHelper emf=new InMemoryEntityManagerFactoryHelper();
            final EntityManager em=emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(p);
            em.persist(q);
            em.persist(lemmaForm);
            em.persist(lemmaEntry);
            em.persist(etymonForm);
            em.getTransaction().commit();

            em.getTransaction().begin();
            final EtymologyImporter importer=new EtymologyImporter(em, etymonProvider, iriProvider);
            importer.accept(derivation);
            em.getTransaction().commit();

            final LexicalEntry actualEntry=em.find(LexicalEntry.class, lemmaEntry.getId());
            final Set<Etymology> actualEtymologies=actualEntry.getEtymology();
            assertEquals(1, actualEtymologies.size());
            final Etymology actualEtymology=actualEtymologies.iterator().next();
            assertEquals(NS+"etymology", actualEtymology.getId());
            final EtyLink actualLink=actualEtymology.getStartingLink();
            assertEquals(NS+"etyLink", actualLink.getId());
            assertEquals(lemmaEntry.getId(), actualLink.getEtySource().getId());
            assertEquals(1, actualLink.getEtySubSource().size());
            final Form etySubSource=actualLink.getEtySubSource().iterator().next();
            assertEquals(lemmaForm.getId(), etySubSource.getId());

            final Iterator<LinguisticPhenomenonOccurrence> actualOccurrencesIt= em.createNativeQuery("SELECT ?x WHERE {?x a <"+
                    LinguisticPhenomena.LINGUISTIC_PHENOMENON_OCCURRENCE_CLASS+">} ORDER BY ?x", LinguisticPhenomenonOccurrence.class)
                    .getResultList().iterator();
            final LinguisticPhenomenonOccurrence actualOP=actualOccurrencesIt.next();
            assertEquals(occurrenceIRIProviderP.getOccurrenceIRI(), actualOP.getId());
            assertEquals(lemmaForm.getId(), actualOP.getTarget().getId());
            assertEquals(p.getId(), actualOP.getOccurrenceOf().getId());
            assertEquals(occurrenceIRIProviderP.getIntermediateFormIRI(), actualOP.getSource().getId());
            assertEquals(intermediateFormWrittenRep, actualOP.getSource().getWrittenRep());

            final LinguisticPhenomenonOccurrence actualOQ=actualOccurrencesIt.next();
            assertEquals(occurrenceIRIProviderQ.getOccurrenceIRI(), actualOQ.getId());
            assertEquals(occurrenceIRIProviderP.getIntermediateFormIRI(), actualOQ.getTarget().getId());
            assertEquals(q.getId(), actualOQ.getOccurrenceOf().getId());
            assertEquals(etymonForm.getId(), actualOQ.getSource().getId());
            final Form actualEtymonForm=actualLink.getEtySubTarget();
            assertEquals(etymonForm.getId(), actualEtymonForm.getId());
            assertEquals(etymonForm.getWrittenRep(), actualEtymonForm.getWrittenRep());

            assertFalse(actualOccurrencesIt.hasNext());
        }
    }

    @Test
    void shouldImportEmptyDerivation(){
        final DerivationPathNode emptyDerivation=new DerivationPathNodeImpl(lemmaForm.getWrittenRep());
        when(etymonProvider.apply(lemmaForm.getWrittenRep())).thenReturn(etymonForm);

        try(final EntityManagerFactoryHelper emf=new InMemoryEntityManagerFactoryHelper();
            final EntityManager em=emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(lemmaForm);
            em.persist(lemmaEntry);
            em.persist(etymonForm);
            em.getTransaction().commit();

            em.getTransaction().begin();
            final EtymologyImporter importer=new EtymologyImporter(em, etymonProvider, iriProvider);
            importer.accept(emptyDerivation);
            em.getTransaction().commit();

            final LexicalEntry actualEntry=em.find(LexicalEntry.class, lemmaEntry.getId());
            final Set<Etymology> actualEtymologies=actualEntry.getEtymology();
            assertEquals(1, actualEtymologies.size());
            final Etymology actualEtymology=actualEtymologies.iterator().next();
            assertEquals(NS+"etymology", actualEtymology.getId());
            final EtyLink actualLink=actualEtymology.getStartingLink();
            assertEquals(NS+"etyLink", actualLink.getId());
            assertEquals(lemmaEntry.getId(), actualLink.getEtySource().getId());
            assertEquals(1, actualLink.getEtySubSource().size());
            final Form etySubSource=actualLink.getEtySubSource().iterator().next();
            assertEquals(lemmaForm.getId(), etySubSource.getId());

            final Iterator<LinguisticPhenomenonOccurrence> actualOccurrencesIt= em.createNativeQuery("SELECT ?x WHERE {?x a <"+
                            LinguisticPhenomena.LINGUISTIC_PHENOMENON_OCCURRENCE_CLASS+">} ORDER BY ?x", LinguisticPhenomenonOccurrence.class)
                    .getResultList().iterator();
            assertFalse(actualOccurrencesIt.hasNext());

            final Form actualEtymonForm=actualLink.getEtySubTarget();
            assertEquals(etymonForm.getId(), actualEtymonForm.getId());
            assertEquals(etymonForm.getWrittenRep(), actualEtymonForm.getWrittenRep());

            assertFalse(actualOccurrencesIt.hasNext());
        }
    }

}
