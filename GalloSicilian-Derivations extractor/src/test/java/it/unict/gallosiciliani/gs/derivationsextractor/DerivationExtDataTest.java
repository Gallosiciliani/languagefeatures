package it.unict.gallosiciliani.gs.derivationsextractor;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link DerivationExtData}
 */
public class DerivationExtDataTest {

    private final DerivationDataTestBed testBed=new DerivationDataTestBed();
    private final DerivationRawData rawData=createTestRawData(testBed.entryWithEtymonButNoDerivation);

    private DerivationRawData createTestRawData(final LexicalEntry dummyEntry){
        final DerivationRawData rawData=mock(DerivationRawData.class);
        when(rawData.getEntry()).thenReturn(dummyEntry);
        when(rawData.getDerivationChain()).thenReturn(Collections.emptyList());
        when(rawData.getEligibleLinguisticPhenomena()).thenReturn(new LinguisticPhenomenaProvider(Collections.emptyList()));
        return rawData;
    }

    @Test
    void shouldProvideLemma(){
        final String lemma="lemma";
        rawData.getEntry().getCanonicalForm().setWrittenRep(new MultilingualString().set("lang", lemma));
        final DerivationExtData d=new DerivationExtData(rawData);
        assertEquals(lemma, d.getLemma());
    }

    @Test
    void shouldProvideNoun(){
        rawData.getEntry().getPartOfSpeech().setId(LexInfo.NOUN_INDIVIDUAL);
        final DerivationExtData d=new DerivationExtData(rawData);
        assertTrue(d.isNoun());
    }

    @Test
    void shouldProvideVerb(){
        rawData.getEntry().getPartOfSpeech().setId(LexInfo.VERB_INDIVIDUAL);
        final DerivationExtData d=new DerivationExtData(rawData);
        assertFalse(d.isNoun());
    }

    @Test
    void shouldProvideDerivation(){
        when(rawData.getDerivationChain()).thenReturn(testBed.derivation);
        final DerivationExtData d=new DerivationExtData(rawData);
        final DerivationPathNode actual=d.getDerivation();
        assertNotNull(actual);
        check(testBed.derivation.get(0), actual);
        check(testBed.derivation.get(1), actual.prev());
        assertEquals(testBed.derivation.get(1).getSource().getWrittenRep().get(), actual.prev().prev().get());
        assertNull(actual.prev().prev().prev());
    }

    @Test
    void shouldProvideEmptyDerivation(){
        when(rawData.getEntry()).thenReturn(testBed.entryWithEtymonButNoDerivation);
        when(rawData.getDerivationChain()).thenReturn(Collections.emptyList());
        final DerivationExtData d=new DerivationExtData(rawData);

        final DerivationPathNode actual=d.getDerivation();
        assertNotNull(actual);
        assertEquals(testBed.entryWithEtymonButNoDerivation.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator().next().getWrittenRep().get(),
                actual.get());
        assertNull(actual.prev());
    }

    /**
     * Check if a {@link LinguisticPhenomenonOccurrence} corresponds to the derivation
     * step described by a {@link DerivationPathNode}
     */
    private void check(final LinguisticPhenomenonOccurrence expected, final DerivationPathNode actual){
        assertEquals(expected.getTarget().getWrittenRep().get(), actual.get());
        assertEquals(expected.getOccurrenceOf().getLabel(), actual.getLinguisticPhenomenon().getLabel());
    }

    @Test
    void shouldProvideMissedPhenomena(){
        final ActionablePhenomenon p=new ActionablePhenomenon().set(testBed.p);
        p.setOut(Set.of("y"));
        final LinguisticPhenomenon q=new ActionablePhenomenon().set(testBed.q);
        final ActionablePhenomenon r=new ActionablePhenomenon().set(testBed.r);
        r.setOut(Set.of("w"));
        final ActionablePhenomenon s=new ActionablePhenomenon().set(testBed.s);
        s.setOut(Set.of("w"));

        when(rawData.getEntry()).thenReturn(testBed.entryWithDerivation);
        when(rawData.getDerivationChain()).thenReturn(testBed.derivation);
        when(rawData.getEligibleLinguisticPhenomena()).thenReturn(new LinguisticPhenomenaProvider(List.of(s, r, q, p))); //NOTE that they are not in alphabetic order

        final Iterator<LinguisticPhenomenon> actualIt=new DerivationExtData(rawData).getMissed().iterator();
        assertEquals(r.getId(), actualIt.next().getId());
        assertEquals(s.getId(), actualIt.next().getId());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldProvideGalloitalicityRate(){
        final ActionablePhenomenon p=new ActionablePhenomenon().set(testBed.p);
        p.setOut(Set.of("y"));
        final LinguisticPhenomenon q=new ActionablePhenomenon().set(testBed.q);
        final ActionablePhenomenon r=new ActionablePhenomenon().set(testBed.r);
        r.setOut(Set.of("w"));

        when(rawData.getEntry()).thenReturn(testBed.entryWithDerivation);
        when(rawData.getDerivationChain()).thenReturn(testBed.derivation);
        when(rawData.getEligibleLinguisticPhenomena()).thenReturn(new LinguisticPhenomenaProvider(List.of(p,q,r)));

        final Optional<Float> actual=new DerivationExtData(rawData).getGalloItalicityRate();
        assertFalse(actual.isEmpty());
        assertEquals(0.5f, actual.get());
    }

    @Test
    void shouldProvideRateNotAvailableOnEmptyDerivationChain(){
        final ActionablePhenomenon p=new ActionablePhenomenon().set(testBed.p);
        p.setOut(Set.of("y"));
        final LinguisticPhenomenon q=new ActionablePhenomenon().set(testBed.q);
        final ActionablePhenomenon r=new ActionablePhenomenon().set(testBed.r);
        r.setOut(Set.of("w"));

        when(rawData.getEntry()).thenReturn(testBed.entryWithEtymonButNoDerivation);
        when(rawData.getDerivationChain()).thenReturn(Collections.emptyList());
        when(rawData.getEligibleLinguisticPhenomena()).thenReturn(new LinguisticPhenomenaProvider(List.of(p,q,r)));

        final Optional<Float> actual=new DerivationExtData(rawData).getGalloItalicityRate();
        assertTrue(actual.isEmpty());
    }

}
