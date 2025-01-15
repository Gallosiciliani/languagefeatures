package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.derivations.strategy.*;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test to verify whether the {@link it.unict.gallosiciliani.derivations.DerivationBuilder} works good
 * when applied to gallo-sicilian features
 * @author Cristiano Longo
 */
public class GSDerivationsTest {

    private static final String NS= GSFeatures.NS;

    @Test
    void testAbento() throws IOException {
        testDerivation("abentö","abbentu", NS+"degem.6",NS+"vocal.5");
    }

    @Test
    @Disabled
    void testBoche() throws IOException{
        testDerivation("böchè","abbuccari", NS+"degem.6", NS+"degem.7", NS+"palat.1");
    }

    @Test
    void testTrepuodeno() throws IOException{
        testDerivation("trepuòdenö","ṭṛipòdinu",NS+"ditt.2", NS+"vocal.7", NS+"deretr.1");
    }

    @Test
    void testTrazziera() throws IOException {
        testDerivation("trazziera", "ṭṛazzera", NS+"ditt.1",NS+"deretr.1");
    }

    /**
     * còpela ‘coppola’ (VS còppula) [vocal.4 + degem.5 + vocal.8]
     * NOTE: vocal.4 is not encoded as it does not produce any change
     */
    @Test
    void testCopela() throws IOException{
        testDerivation("còpela", "còppula", NS+"degem.5", NS+"vocal.8");
    }

    /**
     * tabutö ‘bara, cassa da morto (VS tabbutu) [degem.6 + vocal.5]
     */
    @Test
    void testTabuto() throws IOException{
        testDerivation("tabutö", "tabbutu", NS+"degem.6", NS+"vocal.5");
    }

    /**
     * tapölïè ‘bussare’ (VS tuppulïari) [degem.5 + vocal.5 + palat.5]
     */
    @Test
    void testTapolie() throws IOException{
        testDerivation("tapölïè","tuppulïari", NS+"degem.5", NS+"vocal.5", NS+"palat.5");
    }

    /**
     * mecadörö ‘fazzoletto’ (VS muccaturi ‘fazzoletto da naso’) [leniz.5 + degem.7 + vocal.5 + vocal.8]
     */
    @Test
    void testMecadoro() throws IOException{
        testDerivation("mecadörö", "muccaturi", NS+"leniz.5", NS+"degem.7", NS+"vocal.5", NS+"vocal.8");
    }

    /**
     * böcierö ‘macellaio’ (VS vuccieri) [elim.1 + degem.7 + vocal.9]
     */
    @Test
    void testBociero() throws IOException{
        testDerivation("böcierö" ,"vuccieri", NS+"elim.1", NS+"degem.7",NS+"vocal.9");
    }

    /**
     * strö̀mböla ‘trottola’ (VS ṣṭṛùmmula) [deretr.3 + vocal.5 + dissim.1]
     * @throws IOException
     */
    @Test
    void testStrombola() throws IOException{
        testDerivation("strö̀mböla","ṣṭṛùmmula",NS+"deretr.3", NS+"vocal.5", NS+"dissim.1");
    }

    /**
     * carösö ‘bambino, ragazzino’ (VS carusu) [dissim.1 + leniz.18]
     * @throws IOException
     */
    @Test
    @Disabled
    void testCaroso() throws IOException{
        testDerivation("carösö","carusu", NS+"dissim.1", NS+"leniz.18");
    }

    /**
     * ddanternïè ‘bestemmiare, dare in escandescenze’ (VS lantirnïari ‘arrossarsi di qualche parte del corpo, gironzolare’ [degem.25 + vocal.7 + palat.5]
     * @throws IOException
     */
    @Test
    void testDDanternie() throws IOException{
        testDerivation("ddanternïè","lantirnïari", NS+"degem.25", NS+"vocal.7", NS+"palat.5");
    }

    /**
     * ntapè ‘tappare, sbattere’ (VS ntappari) [degem.5 + palat.5]
     * @throws IOException
     */
    @Test
    void testNtape() throws IOException{
        testDerivation("ntapè","ntappari", NS+"degem.5", NS+"palat.5");
    }

    /**
     * trabughjessë ‘spaventarsi, sconvolgersi’ (VS ṭṛabbugghjàrisi) [deretr.1 + vocal.6 +  degem.6 + degem.12 + palat.6 ]
     * @throws IOException
     * NOTE: vocal.6 does not update the string
     */
    @Test
    @Disabled
    void testTrabughjesse() throws IOException{
        testDerivation("trabughjessë","ṭṛabbugghjàrisi", NS+"deretr.1", NS+"vocal.6", NS+"degem.6", NS+"degem.12", NS+"palat.6");
    }

    /**
     * nachessë ‘indugiare, dondolarsi’ (VS annacàrisi) [degem.24 + afer.1 + palat.2]
     * @throws IOException
     */
    @Test
    void testNachesse() throws IOException{
        testDerivation("nachessë", "annacàrisi", NS+"degem.24", NS+"afer.1", NS+"palat.2");
    }

    /**
     * böfeta ‘tavolo della cucina’ (VS bbuffetta) [elim.2 + vocal.5 + vocal.1 + degem.13 + degem.2]
     * @throws IOException
     *
     * NOTE: vocal.1 and degem.13 do not update the string
     */
    @Test
    void testBofeta() throws IOException{
        assertEquals(1, LevenshteinDistance.getDefaultInstance().apply("böfeta","böffeta"));
        testDerivation("böfeta","bbuffetta", NS+"elim.2", NS+"vocal.5", NS+"degem.2");
    }

    /**
     * böfönïè ‘sbeffeggiare’ (VS abbuffunïari) [degem.6 + vocal.5 + afer.1 + degem.13 + palat.5]
     * @throws IOException
     * NOTE: degem.13 does not update the string
     */
    @Test
    @Disabled
    void testBofonie() throws IOException{
        testDerivation("böfönïè","abbuffunïari", NS+"degem.6", NS+"vocal.5", NS+"afer.1", NS+"palat.5");
    }

    /**
     * zzeiròbesö ‘propoli’ (VS ciròbbisi) [assib.8 + vocal.4 + degem.6 + leniz.18]
     * @throws IOException
     * NOTE: leniz.18 and vocal.4 do not update the string
     */
    @Test
    void testZzeirobeso() throws IOException{
        testDerivation("zzeiròbesö","ciròbbisi", NS+"assib.8", NS+"degem.6");
    }

    /**
     * minchjön ‘minchione’ (VS minchjuni) [leniz.14]
     * @throws IOException
     */
    @Disabled
    @Test
    void testMinchjon() throws IOException{
        testDerivation("minchjön","minchjuni", NS+"leniz.14");
    }

    /**
     * mièdegö ‘medico’ (VS mèdicu) [V.1 (ma non diagnostico) + leniz.1 + vocal.7 + vocal.5]
     * @throws IOException
     */
    @Test
    void testMiedego() throws IOException{
        testDerivation("mièdegö", "mèdicu", NS+"leniz.1", NS+"vocal.7", NS+"vocal.5");
    }

    /**
     * cöchjè ‘racimolare’ (VS accucchjari) [degem.7 + afer.1 + degem.11 + palat.5]
     * @throws IOException
     */
    @Test
    @Disabled
    void testCochje() throws IOException{
        testDerivation("cöchjè", "accucchjari", NS+"degem.7", NS+"afer.1", NS+"degem.11", NS+"palat.5");
    }

    /**
     * ciarambedda ‘ciaramella, zampogna’ (VS ciarameḍḍa) [dissim.1 + degem.4 + vocal.1]
     * @throws IOException
     * NOTE: vocal.1 does not update the string
     */
    @Test
    @Disabled
    void testCiarambedda() throws IOException{
        testDerivation("ciarambedda", "ciarameḍḍa", NS+"dissim.1", NS+"degem.4");
    }

    /**
     * ciaramìgöla ‘coccio di terracotta, di tegola’ (VS ciaramìcula) [leniz.1 + vocal.5]
     * @throws IOException
     */
    @Test
    void testCiaramigola() throws IOException{
        testDerivation("ciaramìgöla", "ciaramìcula", NS+"leniz.1", NS+"vocal.5");
    }

    /**
     * zzipala ‘siepe’ (VS sipala) [degemI.8]
     * NOTE: degemI.8 does not exists, may be assib.10
     * @throws IOException
     */
    @Test
    @Disabled
    void testZzipala() throws IOException{
        testDerivation("zzipala", "sipala", "degemI.8");
    }

    /**
     * stragölada ‘quantità di paglia contenuta in una treggia’ (VS ṣṭṛagulata) [deretr.3 + vocal.5 + leniz.5]
     * @throws IOException
     */
    @Test
    void testStragolada() throws IOException{
        testDerivation("stragölada", "ṣṭṛagulata", NS+"deretr.3", NS+"vocal.5", NS+"leniz.5");
    }

    /**
     * mucè ‘nascondere’ (VS ammucciari) [degem.23 + afer.1 + degem.7 + palat.7]
     * @throws IOException
     */
    @Test
    void testMuce() throws IOException{
        testDerivation("mucè","ammucciari",NS+"degem.23", NS+"afer.1", NS+"degem.7", NS+"palat.7");
    }

    /**
     * mbabanù ‘rintronato, rimbecillito’ (VS abbabbanutu) [afer.1 + dissim.1 + degem.6 + leniz.8]
     * @throws IOException
     */
    @Test
    @Disabled
    void testMbabanu() throws IOException{
        testDerivation("mbabanù", "abbabbanutu", NS+"afer.1", NS+"dissim.1", NS+"degem.6", NS+"leniz.8");
    }

    /**
     * cörtighjïè ‘spettegolare’ (VS curtigghjari) [vocal.5 + degem.12 + palat.5]
     * @throws IOException
     */
    @Test
    void testCortighie() throws IOException{
        testDerivation("cörtighjïè", "curtigghjari", NS+"vocal.5", NS+"degem.12", NS+"palat.5");
    }

    /**
     * cöbösö ‘soffocante, opprimente’ (VS accubbusu) [degem.7 + afer.1 + degem.6 + vocal.5 + leniz.18]
     * @throws IOException
     * NOTE: leniz.18 does not update the string
     */
    @Test
    @Disabled
    void testCoboso() throws IOException{
        testDerivation("cöbösö", "accubbusu", NS+"degem.7", NS+"afer.1", NS+"degem.6", NS+"vocal.5");
    }

    /**
     *
     * @throws IOException
     */
    @Test
    @Disabled
    void test() throws IOException{
        testDerivation(null, null, null);
    }



    private void testDerivation(final String lemma,
                                final String candidateEtymon,
                                final String...expectedFeatures) throws IOException {
        try(final GSFeatures gs=GSFeatures.loadLocal()) {
            final List<? extends LinguisticPhenomenon> gsFeatures = RegexLinguisticPhenomenaReader.read(gs.getModel(),
                    new RegexFeatureQuery().ignoreDeprecated()).getFeatures();
            final DerivationPrinter printer=new DerivationPrinter(GSFeatures.LABEL_PROVIDER_ID);
            final NearestShortestDerivation actualDerivations = new NearestShortestDerivation(lemma);
            gsFeatures.forEach((f)->System.out.println(f.getIRI().replace(NS,"")));
            new DerivationBuilder(gsFeatures, new DerivationStrategyFactory() {
                @Override
                public DerivationStrategy build(DerivationPathNode initialDerivation) {
                    return new TargetedDerivationStrategy(initialDerivation, actualDerivations, NotFartherStrategySelector.FACTORY);
//                    return new TargetedDerivationStrategy(initialDerivation, actualDerivations, NearestStrategySelector.FACTORY);
                }
            }).apply(candidateEtymon);

            for (final DerivationPathNode actual : actualDerivations.getDerivation()) {
                final Set<String> actualFeatureSet=extractPhenomena(actual);
                System.out.println(printer.print(actual, Locale.ENGLISH));
                if (actualFeatureSet.containsAll(Arrays.asList(expectedFeatures)))
                    return;
            }
            fail("Expected derivation not found: expected="+Arrays.toString(expectedFeatures));
        }
    }

    private Set<String> extractPhenomena(final DerivationPathNode n){
        final Set<String> res=new TreeSet<>();
        if (n.prev()!=null) {
            res.add(n.getLinguisticPhenomenon().getIRI());
            res.addAll(extractPhenomena(n.prev()));
        }
        return res;
    }
//    abbaḍḍuttulïari
//63: elapsed time 763275, total time 1828405. Filtering time 19


}
