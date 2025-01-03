package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link DerivationBuilder}
 *
 * @author Cristiano Longo
 */
public class DerivationBuilderTest {

    /**
     * Compare path nodes by the characterizing strings
     */
    private static final Comparator<DerivationPathNode> COMPARATOR = Comparator.comparing(DerivationPathNode::get);

    private static class DerivationPathNodesSet extends TreeSet<DerivationPathNode> implements DerivationsToTargetContainer {

        private final String target;
        DerivationPathNodesSet(String target) {
            super(COMPARATOR);
            this.target=target;
        }

        @Override
        public boolean test(final DerivationPathNode n) {
            System.out.println(n);
            add(n);
            return true;
        }

        @Override
        public String getTarget() {
            return target;
        }

        @Override
        public Collection<DerivationPathNode> getDerivation() {
            return this;
        }
    }

    @Test
    void shouldApplySelectShortestDerivations() {
        final String target="xy";
        final String initialLexeme = "ss";
        final String derived1 = "st";
        final String derived2 = "ts";
        //initial lexeme, derived1 and derived2 are all at distance 2 from the target
        //so that all the possible derivations are candidates, but the first one is shortest
        final Set<String> derived = new TreeSet<>();
        derived.add(derived1);
        derived.add(derived2);

        final LinguisticPhenomenon t = mock(LinguisticPhenomenon.class);
        when(t.apply(initialLexeme)).thenReturn(derived);

        final DerivationPathNodesSet actual = new DerivationPathNodesSet(target);
        new DerivationBuilder(Collections.singletonList(t), Collections.singletonList(actual)).apply(initialLexeme);
        final Iterator<DerivationPathNode> actualIt = actual.iterator();

        checkEquals(new DerivationPathNode() {
            @Override
            public String get() {
                return initialLexeme;
            }

            @Override
            public DerivationPathNode prev() {
                return null;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return null;
            }

            @Override
            public int length() {
                return 0;
            }
        }, actualIt.next());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldApplyCreateNewPathsUsingTransformation() {
        final String target="xy";
        final String initialLexeme = "ss";
        final String derived1 = "xs";
        final String derived2 = "sy";
        //initial lexeme is at distance 2, whereas derived1 and derived2 are both at distance 1 from the target
        //so these last ones are selected
        final Set<String> derived = new TreeSet<>();
        derived.add(derived1);
        derived.add(derived2);

        final LinguisticPhenomenon t = mock(LinguisticPhenomenon.class);
        when(t.apply(initialLexeme)).thenReturn(derived);

        final DerivationPathNodesSet actual = new DerivationPathNodesSet(target);
        new DerivationBuilder(Collections.singletonList(t), Collections.singletonList(actual)).apply(initialLexeme);
        final Iterator<DerivationPathNode> actualIt = actual.iterator();

        final DerivationPathNode expectedRoot = new DerivationPathNode() {
            @Override
            public String get() {
                return initialLexeme;
            }

            @Override
            public DerivationPathNode prev() {
                return null;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return null;
            }

            @Override
            public int length() {
                return 0;
            }
        };

        checkEquals(new DerivationPathNode() {
            @Override
            public String get() {
                return derived1;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedRoot;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return t;
            }

            @Override
            public int length() {
                return 1;
            }
        }, actualIt.next());

        checkEquals(new DerivationPathNode() {
            @Override
            public String get() {
                return derived2;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedRoot;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return t;
            }

            @Override
            public int length() {
                return 1;
            }
        }, actualIt.next());
        assertFalse(actualIt.hasNext());
    }

    private void checkEquals(final DerivationPathNode expected, final DerivationPathNode actual) {
        assertEquals(expected.get(), actual.get());
        if (expected.prev() == null) {
            assertNull(expected.prev());
        } else {
            assertEquals(expected.getLinguisticPhenomenon(), actual.getLinguisticPhenomenon());
            checkEquals(expected.prev(), actual.prev());
        }
    }

    @Test
    void shouldIgnoreTransformationsWhichDoesNotChangeTheString() {
        final String initialLexeme = "s";
        final String derived1 = "sT1s1";
        final String derived2 = "sT1s2";
        final LinguisticPhenomenon T1 = mock(LinguisticPhenomenon.class);
        when(T1.apply(initialLexeme)).thenReturn(Set.of(derived1, derived2));
        when(T1.toString()).thenReturn("T1");

        final String derived11 = "sT1s1T2s11";
        @SuppressWarnings("UnnecessaryLocalVariable") final String derived12 = derived1;
        final LinguisticPhenomenon T2 = mock(LinguisticPhenomenon.class);
        when(T2.apply(derived1)).thenReturn(Set.of(derived11, derived12));
        when(T2.toString()).thenReturn("T2");

        @SuppressWarnings("UnnecessaryLocalVariable") final String derived21 = derived2;
        when(T2.apply(derived2)).thenReturn(Set.of(derived21));


        final DerivationPathNodesSet actual = new DerivationPathNodesSet("x");
        new DerivationBuilder(Arrays.asList(T1, T2), Collections.singletonList(actual)).apply(initialLexeme);

        //expected paths sT1s1<-T1--s, sT1s1T2s11<-T2--sT1s1<-T1--s, sT1s2<-T1--s
        final Iterator<DerivationPathNode> actualIt = actual.iterator();

        final DerivationPathNode expectedRoot = new DerivationPathNode() {
            @Override
            public String get() {
                return initialLexeme;
            }

            @Override
            public DerivationPathNode prev() {
                return null;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return null;
            }

            @Override
            public int length() {
                return 0;
            }
        };
        checkEquals(expectedRoot, actualIt.next());

        // sT1s1<-T1--s
        final DerivationPathNode expectedDerived1Node = new DerivationPathNode() {
            @Override
            public String get() {
                return derived1;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedRoot;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return T1;
            }

            @Override
            public int length() {
                return 1;
            }
        };
        checkEquals(expectedDerived1Node, actualIt.next());

        // sT1s1T2s11<-T2--sT1s1<-T1--s
        checkEquals(new DerivationPathNode() {
            @Override
            public String get() {
                return derived11;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedDerived1Node;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return T2;
            }

            @Override
            public int length() {
                return 2;
            }
        }, actualIt.next());

        //sT1s2<-T1--s
        checkEquals(new DerivationPathNode() {
            @Override
            public String get() {
                return derived2;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedRoot;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return T1;
            }

            @Override
            public int length() {
                return 1;
            }
        }, actualIt.next());
        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldGoOnWhenCurrentTransformationDoesNotApply() {
        final String initialLexeme = "s";
        final LinguisticPhenomenon T1 = mock(LinguisticPhenomenon.class);
        when(T1.apply(initialLexeme)).thenReturn(new TreeSet<>()); //return the empty set

        final String derived = "X";
        final LinguisticPhenomenon T2 = mock(LinguisticPhenomenon.class);
        when(T2.apply(initialLexeme)).thenReturn(Set.of(derived));

        final DerivationPathNodesSet actual = new DerivationPathNodesSet("x");
        new DerivationBuilder(Arrays.asList(T1, T2), Collections.singletonList(actual)).apply(initialLexeme);
        final Iterator<DerivationPathNode> actualIt = actual.iterator();

        final DerivationPathNode expectedRoot = new DerivationPathNode() {
            @Override
            public String get() {
                return initialLexeme;
            }

            @Override
            public DerivationPathNode prev() {
                return null;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return null;
            }

            @Override
            public int length() {
                return 0;
            }
        };

        // X<-T1--s
        final DerivationPathNode expectedDerived1Node = new DerivationPathNode() {
            @Override
            public String get() {
                return derived;
            }

            @Override
            public DerivationPathNode prev() {
                return expectedRoot;
            }

            @Override
            public LinguisticPhenomenon getLinguisticPhenomenon() {
                return T2;
            }

            @Override
            public int length() {
                return 1;
            }
        };
        checkEquals(expectedDerived1Node, actualIt.next());
        checkEquals(expectedRoot, actualIt.next());
        assertFalse(actualIt.hasNext());
    }
}
