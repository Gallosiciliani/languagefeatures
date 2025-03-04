package it.unict.gallosiciliani.sicilian;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Test for {@link SicilianToNicosiaESperlingaDerivations}
 */
public class SicilianToNicosiaESperlingaDerivationsTest {

    @Test
    void testAmmolafuorficiECuteddi() throws IOException {
        final SicilianToNicosiaESperlingaDerivations d=new SicilianToNicosiaESperlingaDerivations();
        final SicilianVocabulary v=new SicilianVocabulary(d);
        v.accept("ammolafòffici e-ccuteḍḍ(ṛ)i");
    }
}
