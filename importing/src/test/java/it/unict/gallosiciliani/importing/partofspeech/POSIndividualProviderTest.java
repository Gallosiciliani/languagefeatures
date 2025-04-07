package it.unict.gallosiciliani.importing.partofspeech;

import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link POSIndividualProvider}
 * @author Cristiano Longo
 */
public class POSIndividualProviderTest {

    @Test
    void shouldUseLexInfoForPartOfSpeech(){
        final POSIndividualProvider p = new POSIndividualProvider();
        assertEquals(LexInfo.NOUN_INDIVIDUAL, p.getNoun().getId());
        assertEquals(LexInfo.VERB_INDIVIDUAL, p.getVerb().getId());
    }

}
