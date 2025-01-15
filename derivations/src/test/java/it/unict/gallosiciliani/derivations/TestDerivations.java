package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.derivations.strategy.DerivationStrategy;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.Getter;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

/**
 * Some derivations for test purposes
 */
class TestDerivations {

    static class FinalStrategy implements DerivationStrategy {
        @Getter
        private final DerivationPathNode derivation;
        FinalStrategy(final DerivationPathNode derivation){
            this.derivation=derivation;
        }

        public Collection<DerivationStrategy> branch(final Collection<DerivationPathNode> branches){
            return Collections.emptyList();
        }

        /**
         * Consume this derivation (or one of its ancestors) when the derivation stops
         */
        public void end(){

        }
    }

    final LinguisticPhenomenon p= Mockito.mock(LinguisticPhenomenon.class);
    final LinguisticPhenomenon q=Mockito.mock(LinguisticPhenomenon.class);
    final DerivationPathNode s=new DerivationPathNodeImpl("s");
    final DerivationPathNode spx=new DerivationPathNodeImpl("x", s, p);
    final DerivationPathNode spy=new DerivationPathNodeImpl("y", s, p);
    final Collection<DerivationPathNode> sSubsequentsThroughP= List.of(spx, spy);
    final DerivationPathNode sqz=new DerivationPathNodeImpl("z", s, q);
    final Collection<DerivationPathNode> sSubsequentsThroughQ= List.of(sqz);
    final DerivationPathNode spxqu=new DerivationPathNodeImpl("u", spx, q);
    final Collection<DerivationPathNode> sPxSubsequentsThroughQ= List.of(spxqu);

    TestDerivations(){
        when(p.apply("s")).thenReturn(Set.of("x", "y"));
        when(q.apply("s")).thenReturn(Set.of("z"));
        when(q.apply("x")).thenReturn(Set.of("u"));
        when(p.apply("y")).thenReturn(Collections.emptySet());
    }

    static DerivationStrategy getStrategy(final DerivationPathNode derivation, final Collection<DerivationPathNode> expectedDerivations, final Collection<DerivationStrategy> result, final boolean addItselfToResults){
        final DerivationStrategy s= Mockito.mock(DerivationStrategy.class);
        when(s.getDerivation()).thenReturn(derivation);
        final Collection<DerivationStrategy> resultUpdated=new ArrayList<>(result);
        if (addItselfToResults)
            resultUpdated.add(s);
        when(s.branch(argThat(new DerivationCollectionsMatcher(expectedDerivations)))).thenReturn(resultUpdated);
        return s;
    }

    static DerivationStrategy getStrategy(final DerivationPathNode derivation){
        final DerivationStrategy s= Mockito.mock(DerivationStrategy.class);
        when(s.getDerivation()).thenReturn(derivation);
        return s;
    }

    static DerivationStrategy getStrategy(final DerivationPathNode derivation, final Collection<DerivationPathNode> expectedDerivations, final Collection<DerivationStrategy> result){
        return getStrategy(derivation, expectedDerivations, result, false);
    }
}
