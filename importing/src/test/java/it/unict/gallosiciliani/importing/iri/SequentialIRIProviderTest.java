package it.unict.gallosiciliani.importing.iri;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SequentialIRIProvider}
 */
public class SequentialIRIProviderTest {

    private static final String NS="http://test.org/lexicon#";

    private final Set<String> produced=new TreeSet<>();

    @Test
    void shouldProduceDifferentIRIS(){
        //just produce a lot of IRIs anche check that all of them are right
        final IRIProvider p=new SequentialIRIProvider(NS);
        final LexicalEntryIRIProvider e1=p.getLexicalEntryIRIs();
        check(e1);

        final LexicalEntryIRIProvider e2=p.getLexicalEntryIRIs();
        check(e2);
    }

    private void check(final LexicalEntryIRIProvider entryIRIProvider){
        checkProduced(entryIRIProvider.getLexicalEntryIRI());
        checkProduced(entryIRIProvider.getCanonicalFormIRI());

        final EtymologyIRIProvider e1ety1=entryIRIProvider.getEtymologyIRIs();
        checkProduced(e1ety1.getEtymolgyIRI());
        checkProduced(e1ety1.getEtyLinkIRI());
        checkProduced(e1ety1.getEtySubSourceIRI());
        checkProduced(e1ety1.getEtySubSourceIRI());

        final PhenomenonOccurrenceIRIProvider e1ety1o1=e1ety1.getLinguisticPhenomenaOccurrencesIRIs();
        checkProduced(e1ety1o1.getOccurrenceIRI());
        checkProduced(e1ety1o1.getIntermediateFormIRI());

        final PhenomenonOccurrenceIRIProvider e1ety1o2=e1ety1.getLinguisticPhenomenaOccurrencesIRIs();
        checkProduced(e1ety1o2.getOccurrenceIRI());
        checkProduced(e1ety1o2.getIntermediateFormIRI());

        final EtymologyIRIProvider e1ety2=entryIRIProvider.getEtymologyIRIs();
        checkProduced(e1ety2.getEtymolgyIRI());
        checkProduced(e1ety2.getEtyLinkIRI());
        checkProduced(e1ety2.getEtySubSourceIRI());

        final PhenomenonOccurrenceIRIProvider e1ety2o1=e1ety2.getLinguisticPhenomenaOccurrencesIRIs();
        checkProduced(e1ety2o1.getOccurrenceIRI());
        checkProduced(e1ety2o1.getIntermediateFormIRI());
    }

    private void checkProduced(final String iri){
        assertNotNull(iri);
        try {
            URI.create(iri);
        } catch(final IllegalArgumentException e){
            fail("Illegal IRI "+iri);
        }
        assertTrue(iri.startsWith(NS));
        assertTrue(produced.add(iri),"Duplicate "+iri);
        System.out.println(iri);
    }

    @Test
    void shouldProduceIRIsForAnExistingEntry(){
        final LexicalEntry entry=mock(LexicalEntry.class);
        when(entry.getId()).thenReturn(NS+"entry");
        final IRIProvider p=new SequentialIRIProvider(NS);
        final LexicalEntryIRIProvider e=p.getLexicalEntryIRIs(entry);
        assertEquals(entry.getId(), e.getLexicalEntryIRI());
        check(e);
    }
}
