package it.unict.gallosiciliani.liph;

import it.unict.gallosiciliani.util.OntologyCheckUtils;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that the ontology {@link LinguisticPhenomenon} has been correctly imported
 */
public class LinguisticPhenomenaTest {
    @Test
    void testContainProperties() throws Exception {
        try(final LinguisticPhenomena liph = new LinguisticPhenomena()) {
            final Model m = liph.getModel();
            final OntologyCheckUtils utils = new OntologyCheckUtils(m)
                    .objectPropertyExists(LinguisticPhenomena.LINGUISTIC_PHENOMENON_OBJ_PROPERTY)
                    .annotationPropertyExists(LinguisticPhenomena.REGEX_ANN_PROPERTY)
                    .annotationPropertyExists(LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY);
            assertTrue(utils.check(), utils.getFailureMessage());
        }
    }

    @Test
    void testReturningOntologyAsStr() throws IOException {
        try(final LinguisticPhenomena liph = new LinguisticPhenomena()) {
            assertTrue(liph.getOntologyAsStr().startsWith("@prefix : <" + LinguisticPhenomena.NS + ">"));
        }
    }


}
