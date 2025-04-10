package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.when;

/**
 * Some derivations for test purposes
 */
public class TestDerivations implements LinguisticPhenomenonLabelProvider, LinguisticPhenomenonByLabelRetriever {

    public final LinguisticPhenomenon p= Mockito.mock(LinguisticPhenomenon.class);
    public final LinguisticPhenomenon q=Mockito.mock(LinguisticPhenomenon.class);
    public final DerivationPathNode s=new DerivationPathNodeImpl("s");
    public final DerivationPathNode spx=new DerivationPathNodeImpl("x", s, p);
    public final DerivationPathNode spy=new DerivationPathNodeImpl("y", s, p);
    public final Collection<DerivationPathNode> sSubsequentsThroughP= List.of(spx, spy);
    public final DerivationPathNode sqz=new DerivationPathNodeImpl("z", s, q);
    public final Collection<DerivationPathNode> sSubsequentsThroughQ= List.of(sqz);
    public final DerivationPathNode spxqu=new DerivationPathNodeImpl("u", spx, q);
    public final Collection<DerivationPathNode> sPxSubsequentsThroughQ= List.of(spxqu);

    public TestDerivations(){
        when(p.apply("s")).thenReturn(Set.of("x", "y"));
        when(q.apply("s")).thenReturn(Set.of("z"));
        when(q.apply("x")).thenReturn(Set.of("u"));
        when(p.apply("y")).thenReturn(Collections.emptySet());
    }

    @Override
    public String getLabel(LinguisticPhenomenon linguisticPhenomenon, Locale locale) {
        if (linguisticPhenomenon==p) return "p";
        if (linguisticPhenomenon==q) return "q";
        throw new IllegalArgumentException("Unexpected phenomenon "+linguisticPhenomenon);
    }

    @Override
    public LinguisticPhenomenon getByLabel(final String label, final Locale locale) {
        if ("p".equals(label)) return p;
        if ("q".equals(label)) return q;
        throw new IllegalArgumentException();
    }
}
