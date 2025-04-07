package it.unict.gallosiciliani.sicilian;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test for {@link NicosiaESperlinga}
 * @author Cristiano Longo
 */
public class NicosiaESperlingaTest {

    @Test
    void shouldReturnAllForms() throws IOException {
        try(final NicosiaESperlinga n=new NicosiaESperlinga()) {
            assertFalse(n.getModel().isEmpty());
            final Iterator<Form> actualIt = n.getAllForms().iterator();
            final Form actual0 = actualIt.next();
            assertEquals(NicosiaESperlinga.NS + "entry0-canonicalForm", actual0.getId());
            assertEquals("entry0", actual0.getWrittenRep());
            final Form actual1 = actualIt.next();
            assertEquals(NicosiaESperlinga.NS + "entry1-canonicalForm", actual1.getId());
            assertEquals("entry1", actual1.getWrittenRep());
            assertFalse(actualIt.hasNext());
        }
    }
}
