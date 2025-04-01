package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationIOUtil;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.gs.GSFeatures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test for {@link DerivationService}
 * @author Cristiano Longo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DerivationServiceTest {


    @Autowired
    DerivationService derivationService;

    @Test
    void shouldReturnNearestShortestDerivations(){
        checkBocheDerivations(derivationService.derives("abbuccari", "böchè"));
    }

    private void checkBocheDerivations(final NearestShortestDerivation actual){
        final Iterator<DerivationPathNode> actualIt=actual.getDerivation().iterator();
        final DerivationIOUtil printer=new DerivationIOUtil(GSFeatures.LABEL_PROVIDER_ID);
        final DerivationPathNode actualDerivation=actualIt.next();
        System.out.println("Derivation 2: "+printer.print(actualDerivation, Locale.ENGLISH));
        printer.print(actualDerivation, Locale.ENGLISH);
        new DerivationChecker(actualDerivation)
                .inner("böchè", GSFeatures.NS+"vocal.5.a")
                .inner("buchè", GSFeatures.NS+"elim.2")
                .inner("bbuchè", GSFeatures.NS+"degem.8")
                .inner("bbuccari", GSFeatures.NS+"afer.1")
                .last("abbuccari");

        assertFalse(actualIt.hasNext());
    }

    @Test
    void shouldFindLemmaEtymon() throws IOException {
        final NearestShortestDerivation actual = derivationService.findSicilianEtymon("abentö");
        final Iterator<DerivationPathNode> actualIt=actual.getDerivation().iterator();
        new DerivationChecker(actualIt.next())
                .inner("abentö", GSFeatures.NS+"vocal.5.a")
                .inner("abentu", GSFeatures.NS+"degem.6")
                .last("abbentu");
    }

}
