package it.unict.gallosiciliani.liph.util;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Test for {@link LinguisticPhenomenonByLabelRetrieverImpl}
 */
public class LinguisticPhenomenonByLabelRetrieverImplTest {
    final LinguisticPhenomenon p1=mock(LinguisticPhenomenon.class);
    final LinguisticPhenomenon p2=mock(LinguisticPhenomenon.class);

    @Test
    void shouldProvidePhenomena(){
        final LinguisticPhenomenonByLabelRetrieverImpl r=new LinguisticPhenomenonByLabelRetrieverImpl();
        final String p1Label="p1";
        when(p1.getLabel()).thenReturn(p1Label);
        r.accept(p1);
        final String p2Label="p2";
        when(p2.getLabel()).thenReturn(p2Label);
        r.accept(p2);

        assertSame(p1, r.getByLabel(p1Label, null));
        assertSame(p2, r.getByLabel(p2Label, null));
        assertNull(r.getByLabel("No such label", null));
    }

    @Test
    void shouldThrowExceptionOnDuplicatePhenomenon(){
        final String theLabel="label";
        final LinguisticPhenomenonByLabelRetrieverImpl r=new LinguisticPhenomenonByLabelRetrieverImpl();
        final LinguisticPhenomenon p1=mock(LinguisticPhenomenon.class);
        when(p1.getLabel()).thenReturn(theLabel);
        r.accept(p1);
        final LinguisticPhenomenon p2=mock(LinguisticPhenomenon.class);
        when(p2.getLabel()).thenReturn(theLabel);
        assertThrows(IllegalArgumentException.class, ()-> r.accept(p2));
    }

    @Test
    void shouldBuildARetirevr(){
        final String p1Label="p1";
        when(p1.getLabel()).thenReturn(p1Label);
        final String p2Label="p2";
        when(p2.getLabel()).thenReturn(p2Label);
        final LinguisticPhenomenonByLabelRetriever r=LinguisticPhenomenonByLabelRetrieverImpl.build(List.of(p1, p2));

        assertSame(p1, r.getByLabel(p1Label, null));
        assertSame(p2, r.getByLabel(p2Label, null));
        assertNull(r.getByLabel("No such label", null));
    }

}
