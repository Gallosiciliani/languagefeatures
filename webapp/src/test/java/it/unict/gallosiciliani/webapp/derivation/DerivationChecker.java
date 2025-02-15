package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationPathNode;

import static org.junit.jupiter.api.Assertions.*;

class DerivationChecker {
    private final DerivationPathNode derivation;

    DerivationChecker(final DerivationPathNode derivation){
        this.derivation=derivation;
    }

    DerivationChecker inner(final String expectedString, final String expectedPhenomenon){
        assertEquals(expectedString, derivation.get());
        assertEquals(expectedPhenomenon, derivation.getLinguisticPhenomenon().getIRI());
        assertNotNull(derivation.prev());
        return new DerivationChecker(derivation.prev());
    }

    void last(final String expectedString){
        assertEquals(expectedString, derivation.get());
        assertNull(derivation.getLinguisticPhenomenon());
        assertNull(derivation.prev());
    }
}
