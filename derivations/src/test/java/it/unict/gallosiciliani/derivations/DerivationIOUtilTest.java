package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link DerivationIOUtil}
 */
public class DerivationIOUtilTest {

    private final TestDerivations testbed=new TestDerivations();
    private final DerivationIOUtil io=new DerivationIOUtil(testbed);

    @Test
    void shouldPrintDerivation(){
        assertEquals("u<-q--x<-p--s", io.print(testbed.spxqu, Locale.ENGLISH));
    }

    @Test
    void shouldParseDerivation(){
        final Collection<LinguisticPhenomenon> availablePhenomena= List.of(testbed.p, testbed.q);
        final DerivationPathNode actual=io.getParser(availablePhenomena).parse("u<-q--x<-p--s", Locale.ENGLISH);
        assertEquals("u", actual.get());
        assertSame(testbed.q, actual.getLinguisticPhenomenon());
        final DerivationPathNode actualX=actual.prev();
        assertEquals("x", actualX.get());
        assertSame(testbed.p, actualX.getLinguisticPhenomenon());
        final DerivationPathNode actualS=actualX.prev();
        assertEquals("s", actualS.get());
        assertNull(actualS.getLinguisticPhenomenon());
        assertNull(actualS.prev());
    }
}
