package it.unict.gallosiciliani.liphtools.liph;

import it.unict.gallosiciliani.liphtools.util.OntologyTestUtils;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that the ontology {@link LinguisticPhenomenon} has been correctly imported
 */
public class LinguisticPhenomenaTest {
    @Test
    void testContainProperties() throws IOException {
        try(final LinguisticPhenomena liph = new LinguisticPhenomena()) {
            final Model m = liph.getModel();
            final OntologyTestUtils utils = new OntologyTestUtils(m);
                utils.checkObjectPropertyExists(LinguisticPhenomena.LINGUISTIC_PHENOMENON_OBJ_PROPERTY);
                utils.checkAnnotationPropertyExists(LinguisticPhenomena.REGEX_ANN_PROPERTY);
                utils.checkAnnotationPropertyExists(LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY);
        }
    }

    @Test
    void testReturningOntologyAsStr() throws IOException {
        try(final LinguisticPhenomena liph = new LinguisticPhenomena()) {
            assertTrue(liph.getOntologyAsStr().startsWith("@prefix : <" + LinguisticPhenomena.NS + ">"));
        }
    }


}
