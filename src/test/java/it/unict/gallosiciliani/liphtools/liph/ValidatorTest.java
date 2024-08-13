package it.unict.gallosiciliani.liphtools.liph;

import it.unict.gallosiciliani.liphtools.util.OntologyLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link Validator}
 *
 * @author Cristiano Longo
 */
public class ValidatorTest {
    private static final String TEST_SINGLE_DERIVATION_NS = "https://gallosiciliani.unict.it/ns/test/derivation#";

    private final LinguisticPhenomenon phenomenon = mock(LinguisticPhenomenon.class);

    private final LiphDerivation testDerivation = new LiphDerivation() {
        @Override
        public String getSourceIndividual() {
            return TEST_SINGLE_DERIVATION_NS +"a";
        }

        @Override
        public String getSourceWrittenRep() {
            return "ax";
        }

        @Override
        public LinguisticPhenomenon getPhenomenon() {
            return phenomenon;
        }

        @Override
        public String getTargetIndividual() {
            return TEST_SINGLE_DERIVATION_NS +"b";
        }

        @Override
        public String getTargetWrittenRep() {
            return "bx";
        }
    };

    @BeforeEach
    void init(){
        when(phenomenon.getIRI()).thenReturn(TEST_SINGLE_DERIVATION_NS +"phenomenon");
    }
    @Test
    void testValidDerivation() throws IOException {
        when(phenomenon.apply("ax")).thenReturn(Set.of("bx", "cx"));
        try(final OntologyLoader l = new OntologyLoader("derivation.ttl")){
            final Validator actual = new Validator(l.getModel());
            actual.accept(phenomenon);
            assertEquals(1, actual.getProcessedDerivations());
            assert actual.getInvalidDerivations().isEmpty();
        }
    }

    @Test
    void testUnexpectedDerivation() throws IOException {
        when(phenomenon.apply("ax")).thenReturn(Set.of("cx"));
        testDerivation();
    }

    private void testDerivation() throws IOException {
        try(final OntologyLoader l = new OntologyLoader("derivation.ttl")){
            final Validator actual = new Validator(l.getModel());
            actual.accept(phenomenon);
            assertEquals(1, actual.getProcessedDerivations());
            final Iterator<LiphDerivation> actualIt = actual.getInvalidDerivations().iterator();
            checkEquals(testDerivation, actualIt.next());
            assertFalse(actualIt.hasNext());
        }
    }
    private void checkEquals(final LiphDerivation expected, final LiphDerivation actual){
        assertEquals(expected.getSourceIndividual(), actual.getSourceIndividual());
        assertEquals(expected.getSourceWrittenRep(), actual.getSourceWrittenRep());
        assertEquals(expected.getPhenomenon().getIRI(), actual.getPhenomenon().getIRI());
        assertEquals(expected.getTargetIndividual(), actual.getTargetIndividual());
        assertEquals(expected.getTargetWrittenRep(), actual.getTargetWrittenRep());
    }

    @Test
    void testExpectedEmptyDerivation() throws IOException {
        when(phenomenon.apply("ax")).thenReturn(Collections.emptySet());
        testDerivation();
    }

    @Test
    void shouldRetriveDerivationsFromModel() throws IOException {
        try (final OntologyLoader data = new OntologyLoader("derivations.ttl");
             final OntologyLoader defs = new OntologyLoader("defs.ttl")) {
            final Validator actual = new Validator(data.getModel());
            actual.validate(defs.getModel());
            assertEquals(2, actual.getProcessedDerivations());
            final Iterator<LiphDerivation> actualIt = actual.getInvalidDerivations().iterator();

            final String dataNS = "https://gallosiciliani.unict.it/ns/test/derivations#";
            final String defsNS = "https://gallosiciliani.unict.it/ns/test/defs#";

            final LinguisticPhenomenon atoc = mock(LinguisticPhenomenon.class);
            when(atoc.getIRI()).thenReturn(defsNS+"atoc");
            checkEquals(new LiphDerivation() {
                @Override
                public String getSourceIndividual() {
                    return dataNS+"a";
                }

                @Override
                public String getSourceWrittenRep() {
                    return "ax";
                }

                @Override
                public LinguisticPhenomenon getPhenomenon() {
                    return atoc;
                }

                @Override
                public String getTargetIndividual() {
                    return dataNS+"b";
                }

                @Override
                public String getTargetWrittenRep() {
                    return "bx";
                }
            }, actualIt.next());
            assertFalse(actualIt.hasNext());
        }
    }
}
