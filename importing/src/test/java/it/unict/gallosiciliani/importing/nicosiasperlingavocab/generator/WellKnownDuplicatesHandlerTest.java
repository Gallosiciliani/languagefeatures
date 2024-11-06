package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link WellKnownDuplicatesHandler}
 *
 * @author Cristiano Longo
 */
public class WellKnownDuplicatesHandlerTest {
    private static final String[] expectedDuplicates={};
    private final WellKnownDuplicatesHandler h=new WellKnownDuplicatesHandler();

    @Test
    void shouldHandleNotDuplicate(){
        assertTrue(h.handle("not duplicate lemma"));
    }

    @Test
    void shouldHandleDuplicateFoundForTheFirstTime(){
        assertTrue(h.handle("cömenazziön"));
    }

    @Test
    void shouldNotHandleDuplicateFoundForTheNextTimes(){
        assertTrue(h.handle("cömenazziön"));
        assertFalse(h.handle("cömenazziön"));
        assertFalse(h.handle("cömenazziön"));
    }


}
