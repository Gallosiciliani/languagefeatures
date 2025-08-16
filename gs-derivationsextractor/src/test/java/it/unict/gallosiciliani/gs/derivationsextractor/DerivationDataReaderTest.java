package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link it.unict.gallosiciliani.gs.derivationsextractor.DerivationDataReader}
 *
 * @author Cristiano Longo
 */
public class DerivationDataReaderTest{

    @Test
    void shouldReadEntriesWithEtymology() throws IOException {
        try(PersistedDerivationDataTestBed testBed=new PersistedDerivationDataTestBed()) {
            final DerivationDataReader r = new DerivationDataReader(testBed.entityManager, testBed.lpProvider);
            final DerivationRawData actual1 = r.next();
            assertEquals(testBed.entryWithDerivation.getId(), actual1.getEntry().getId());
            final DerivationRawData actual2 = r.next();
            assertEquals(testBed.entryWithEtymonButNoDerivation.getId(), actual2.getEntry().getId());
            assertFalse(r.hasNext());
        }
    }

    @Test
    void shouldReadDerivationChain() throws IOException {
        try(PersistedDerivationDataTestBed testBed=new PersistedDerivationDataTestBed()) {
            final DerivationDataReader r = new DerivationDataReader(testBed.entityManager, testBed.lpProvider);
            final DerivationRawData actualEntry = r.next();

            final Iterator<LinguisticPhenomenonOccurrence> actualIt = actualEntry.getDerivationChain().iterator();
            final LinguisticPhenomenonOccurrence o2 = actualIt.next();
            assertEquals(testBed.q.getId(), o2.getOccurrenceOf().getId());
            assertEquals(testBed.entryWithDerivation.getCanonicalForm().getWrittenRep().get(), o2.getTarget().getWrittenRep().get());
            assertEquals(testBed.intermediateForm.getWrittenRep().get(), o2.getSource().getWrittenRep().get());


            final LinguisticPhenomenonOccurrence o1 = actualIt.next();
            assertEquals(testBed.p.getId(), o1.getOccurrenceOf().getId());
            assertEquals(testBed.intermediateForm.getWrittenRep().get(), o1.getTarget().getWrittenRep().get());
            assertEquals(testBed.entryWithDerivation.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator()
                    .next().getWrittenRep().get(), o1.getSource().getWrittenRep().get());

            assertFalse(actualIt.hasNext());
        }
    }

    @Test
    void shouldReadEntryWithEtymologyButNoDerivation() throws IOException {
        try(PersistedDerivationDataTestBed testBed=new PersistedDerivationDataTestBed()) {
            final DerivationDataReader r = new DerivationDataReader(testBed.entityManager, testBed.lpProvider);
            r.next();
            final DerivationRawData actual = r.next();
            assertEquals(testBed.entryWithEtymonButNoDerivation.getId(), actual.getEntry().getId());
            assertTrue(actual.getDerivationChain().isEmpty());
        }
    }

}
