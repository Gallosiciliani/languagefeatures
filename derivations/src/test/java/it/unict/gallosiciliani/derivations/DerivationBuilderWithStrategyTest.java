package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.derivations.strategy.DerivationStrategy;
import it.unict.gallosiciliani.derivations.strategy.DerivationStrategyFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Test for {@link DerivationBuilderWithStrategy}
 * @author Cristiano Longo
 */
public class DerivationBuilderWithStrategyTest {

    private final TestDerivations d=new TestDerivations();

    @Test
    void shouldEndAllFinishedBranches(){
        final DerivationStrategy strategyForS=Mockito.mock(DerivationStrategy.class);
        when(strategyForS.getDerivation()).thenReturn(d.s);
        final DerivationStrategy strategyForSpx=Mockito.mock(DerivationStrategy.class);
        when(strategyForSpx.getDerivation()).thenReturn(d.spx);
        final DerivationStrategy strategyForSpy=Mockito.mock(DerivationStrategy.class);
        when(strategyForSpy.getDerivation()).thenReturn(d.spy);
        final DerivationStrategy strategyForSqz=Mockito.mock(DerivationStrategy.class);
        when(strategyForSqz.getDerivation()).thenReturn(d.sqz);

        final DerivationStrategy strategyForSpxqu=Mockito.mock(DerivationStrategy.class);
        when(strategyForSpxqu.getDerivation()).thenReturn(d.spxqu);

        when(strategyForS.branch(argThat(new DerivationCollectionsMatcher(d.spx,d.spy)))).thenReturn(List.of(strategyForSpx, strategyForSpy, strategyForS));
        when(strategyForS.branch(argThat(new DerivationCollectionsMatcher(d.sqz)))).thenReturn(Collections.singletonList(strategyForSqz));

        when(strategyForSpx.branch(argThat(new DerivationCollectionsMatcher(d.spxqu)))).thenReturn(Collections.singleton(strategyForSpxqu));
        when(strategyForSpy.branch(eq(Collections.emptyList()))).thenReturn(Collections.emptyList());

        final DerivationStrategyFactory f=Mockito.mock(DerivationStrategyFactory.class);
        when(f.build(argThat(new DerivationMatcher(d.s)))).thenReturn(strategyForS);
        final DerivationBuilderWithStrategy b=new DerivationBuilderWithStrategy(List.of(d.p,d.q), f);
        b.apply(d.s.get());

        verify(strategyForS, never()).end();
        verify(strategyForSpx, never()).end();
        verify(strategyForSpxqu).end();
        verify(strategyForSpy).branch(eq(Collections.emptyList()));
        //verify(strategyForSpy).end();
        verify(strategyForSqz).end();
    }
}
