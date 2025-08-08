package it.unict.gallosiciliani.derivationsextractor;

import it.unict.gallosiciliani.gs.derivationsextractor.DerivationData;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link it.unict.gallosiciliani.gs.derivationsextractor.DerivationData}
 */
public class DerivationDataTest {

    /**
     * Entry IRIs have the form {iriPrefix}{rowId}"
     */
    @Test
    void shouldExtractEntryNumber(){
        final int rowId=123;
        final String iri="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#entry123";
        final LexicalEntry src=new LexicalEntry();
        src.setId(iri);
        final DerivationData d=new DerivationData(src);
        assertEquals(rowId, d.rowNum);

    }

}
