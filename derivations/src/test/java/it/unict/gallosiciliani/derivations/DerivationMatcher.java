package it.unict.gallosiciliani.derivations;

import org.mockito.ArgumentMatcher;

public class DerivationMatcher implements ArgumentMatcher<DerivationPathNode> {
    private final DerivationPathNode expected;
    private final DerivationMatcher prev;

    public DerivationMatcher(final DerivationPathNode expected){
        this.expected=expected;
        prev=expected.prev()==null ? null : new DerivationMatcher(expected.prev());
    }

    @Override
    public boolean matches(final DerivationPathNode actual) {
        return expected.get().equals(actual.get())
                && expected.getLinguisticPhenomenon()==actual.getLinguisticPhenomenon() &&
                actual.prev()==null ? expected.prev()==null : prev.matches(actual.prev());
    }
}
