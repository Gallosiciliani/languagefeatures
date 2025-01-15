package it.unict.gallosiciliani.derivations;

import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class DerivationCollectionsMatcher implements ArgumentMatcher<Collection<DerivationPathNode>> {

    private final Collection<DerivationMatcher> matchers;

    public DerivationCollectionsMatcher(final Collection<DerivationPathNode> expected){
        matchers=expected.stream().map(DerivationMatcher::new).toList();
    }

    public DerivationCollectionsMatcher(DerivationPathNode...expectedDerivations){
        this(Arrays.asList(expectedDerivations));
    }

    @Override
    public boolean matches(final Collection<DerivationPathNode> actual) {
        if (actual==null) return matchers.isEmpty();
        final Iterator<DerivationPathNode> actualIt=actual.iterator();
        final Iterator<DerivationMatcher> matcherIt=matchers.iterator();
        while (actualIt.hasNext()){
            if (!matcherIt.hasNext()) return false;
            if (!matcherIt.next().matches(actualIt.next())) return false;
        }
        return !matcherIt.hasNext();
    }
}
