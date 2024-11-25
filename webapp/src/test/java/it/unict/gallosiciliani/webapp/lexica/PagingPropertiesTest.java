package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.webapp.WebAppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for @{@link PagingProperties}
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PagingPropertiesTest {

    @Autowired
    WebAppProperties webAppProperties;

    @Test
    void shouldDetectProperties(){
        final LexiconPageSelector[] actualSelectors=webAppProperties.getPaging().getPages();
        assertEquals(2, actualSelectors.length);
        assertEquals("A-B", actualSelectors[0].getLabel());
        assertEquals("^[a-b].*", actualSelectors[0].getSelector());
        assertEquals("C-Z", actualSelectors[1].getLabel());

    }
}
