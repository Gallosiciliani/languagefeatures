package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.gs.GSFeatures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;

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
        final NearestShortestDerivation actual=derivationService.derives("abbuccari", "böchè");
        final Iterator<DerivationPathNode> actuaIt=actual.getDerivation().iterator();
        new DerivationChecker(actuaIt.next())
                .inner("böchè", GSFeatures.NS+"vocal.5")
                .inner("buchè", GSFeatures.NS+"elim.2")
                .inner("bbuchè", GSFeatures.NS+"degem.8")
                .inner("bbuccari",GSFeatures.NS+"afer.1")
                .last("abbuccari");

        //two derivations found
        new DerivationChecker(actuaIt.next())
                .inner("böchè", GSFeatures.NS+"vocal.5")
                .inner("buchè", GSFeatures.NS+"degem.8")
                .inner("buccari", GSFeatures.NS+"degem.6")
                .inner("bbuccari", GSFeatures.NS+"afer.1")
                .last("abbuccari");

        assertFalse(actuaIt.hasNext());
    }

}
