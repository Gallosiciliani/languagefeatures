package it.unict.gallosiciliani.importing.csv;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class CachingLatinFormProviderTest {

    @Test
    void shouldCreateNewFormIfNotPresent() {
        final CachingLatinFormProvider latinFormProvider=new CachingLatinFormProvider();
        final String latinLemma = "habitu";
        final String expectedIRI="http://test.org/etym/habitu";
        final URI expectedSeeAlso = URI.create("http://test.org/lemmas/habitu");
        final Form actual = latinFormProvider.getLatinForm(latinLemma, expectedIRI);
        assertEquals(expectedIRI, actual.getId());
        assertEquals(latinLemma, actual.getWrittenRep());
        assertEquals(expectedSeeAlso, actual.getSeeAlso());
    }

    @Test
    void shouldReuseFormAlreadyStored() {
        final CachingLatinFormProvider latinFormProvider=new CachingLatinFormProvider();
        final String latinLemma = "habitus";
        final String expectedIRI="http://test.org/etym/habitu";
        final Form expected = latinFormProvider.getLatinForm(latinLemma, expectedIRI);
        final Form actual = latinFormProvider.getLatinForm(latinLemma, "http://test.org/etym/anotherIRI");
        assertSame(expected, actual);
    }
}
