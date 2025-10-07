package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.*;
import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static it.unict.gallosiciliani.gs.GSFeatures.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link GSFeatures} ontology
 * @author Cristiano Longo
 */
@Slf4j
public class GSFeaturesTest {

    @Test
    @Disabled
    void checkRegexConflicts() throws IOException {
        try(final GSFeatures gs = new GSFeatures()) {
            final RegexLinguisticPhenomenaConflictsDetector d = new RegexLinguisticPhenomenaConflictsDetector();
            final List<RegexFeatureQuerySolution> regexPhenomena=new FiniteStatePhenomenaQuery().exec(gs.getModel());
            regexPhenomena.addAll(new RegexLiph1FeatureQuery().ignoreDeprecated().exec(gs.getModel()));
            regexPhenomena.forEach((s)->{
                final FiniteStateLinguisticPhenomenon p=new FiniteStateLinguisticPhenomenon();
                p.setId(s.getFeatureIRI());
                p.setMatchingPattern(s.getRegex());
                d.accept(p);
            });
            for(final Map.Entry<FiniteStateLinguisticPhenomenon, Set<FiniteStateLinguisticPhenomenon>> conflict: d.getConflicts().entrySet()){
                final FiniteStateLinguisticPhenomenon p=conflict.getKey();
                for(final FiniteStateLinguisticPhenomenon q: conflict.getValue())
                    System.out.println("Detected conflict "+p.getId()+" "+q.getId());
            }
            assertTrue(d.getConflicts().isEmpty());
        }
    }

    private RegexLinguisticPhenomenonCheckerFactory getChecker(final String featureIRI) throws IOException {
        try(final GSFeatures ont = new GSFeatures()) {
            final GSFeaturesCategoryRetriever categoryRetriever=new GSFeaturesCategoryRetriever(ont.getCategories());
            RegexLinguisticPhenomenaReader r=new RegexLinguisticPhenomenaReader();
            r.read(ont.getModel(), new FiniteStatePhenomenaQuery());
            final List<LinguisticPhenomenon> allRegexFeatures=r.getFeatures();
            assertFalse(allRegexFeatures.isEmpty());
            for (final LinguisticPhenomenon f : allRegexFeatures)
                if (featureIRI.equals(f.getId())){
                    final String expectedLabel=f.getId().substring(NS.length());
                    assertEquals(f.getId(), ont.getByLabel(expectedLabel, null).getId());
                    return new RegexLinguisticPhenomenonCheckerFactory(f, categoryRetriever);
                }
            throw new IllegalArgumentException("Unable to get feature " + featureIRI);
        }
    }

    /**
     * leniz.1 -c-  > -g-
     *
     */
    @Test
    void testLeniz1() throws IOException {
        final RegexLinguisticPhenomenonCheckerFactory checker= getChecker(NS+"leniz.1")
                .betweenVowels(true, "c", "g");
        for(final char v: RegexLinguisticPhenomenonChecker.VOWELS.toCharArray())
                checker.derives("123"+v+"ch456", "123"+v+"gh456");
        checker.notApply("accutiddari");
        checker.category(LENIZ_CLASS);
    }

    /**
     * leniz.2 -cr- > -gr-
     */
    @Test
    void testLeniz2() throws IOException{
        getChecker(NS+"leniz.2").category(LENIZ_CLASS).betweenVowels(true, "cr","gr");
    }

    /**
     * -p-  > -v-
     */
    @Test
    void testLeniz3() throws IOException{
        getChecker(NS+"leniz.3").category(LENIZ_CLASS).betweenVowels(true, "p","v");
    }

    /**
     * -pr- > -vr-
     */
    @Test
    void testLeniz4() throws IOException{
        getChecker(NS+"leniz.4").category(LENIZ_CLASS).betweenVowels(true, "pr","vr");
    }

    /**
     * -t- > -d-
     */
    @Test
    void testLeniz5() throws IOException{
        getChecker(NS+"leniz.5").category(LENIZ_CLASS).betweenVowels(true, "t","d");
    }

    /**
     * -ìtu > -ì
     */
    @Test
    void testLeniz6() throws IOException{
        getChecker(NS+"leniz.6").category(LENIZ_CLASS).atTheEnd("ìtu","ì");
    }

    /**
     * -àtu > -à
     */
    @Test
    void testLeniz7() throws IOException{
        getChecker(NS+"leniz.7").category(LENIZ_CLASS).atTheEnd("àtu","à");
    }

    /**
     * -ùtu > -ù
     */
    @Test
    void testLeniz8() throws IOException{
        getChecker(NS+"leniz.8").category(LENIZ_CLASS).atTheEnd("ùtu","ù");
    }

    /**
     * -ìti > -ë̀
     */
    @Test
    void testLeniz9() throws IOException{
        getChecker(NS+"leniz.9").category(LENIZ_CLASS).atTheEnd("ìti","ë̀");
    }

    /**
     * -ṭṛ- > -ir-
     */
    @Test
    void testLeniz10() throws IOException{
        getChecker(NS+"leniz.10").category(LENIZ_CLASS).betweenVowels(true, "ṭṛ", "ir");
    }

    /**
     * -dr- > -ir-
     */
    @Test
    void testLeniz11() throws IOException{
        getChecker(NS+"leniz.11").category(LENIZ_CLASS).betweenVowels(true, "dr", "ir");
    }

    /**
     * -l- > --
     */
    @Test
    void testLeniz12() throws IOException{
        getChecker(NS+"leniz.12").category(LENIZ_CLASS).betweenVowels(true, "l","");
    }

    /**
     * -àni > -àn
     */
    @Test
    void testLeniz13() throws IOException{
        getChecker(NS+"leniz.13").category(LENIZ_CLASS).atTheEnd("àni","àn");
    }

    /**
     * -ànu > -àn
     */
    @Test
    void testLeniz13b() throws IOException{
        getChecker(NS+"leniz.13.b").category(LENIZ_CLASS).atTheEnd("ànu","àn");
    }

    /**
     * -ùni > -ö̀n
     */
    @Test
    void testLeniz14() throws IOException{
        getChecker(NS+"leniz.14").category(LENIZ_CLASS).atTheEnd("ùni","ö̀n");
    }

    /**
     * -òni > -ö̀n
     */
    @Test
    void testLeniz15() throws IOException{
        getChecker(NS+"leniz.15").category(LENIZ_CLASS).atTheEnd("òni","ö̀n");
    }

    /**
     * -ìnu > -ìn
     */
    @Test
    void testLeniz16() throws IOException{
        getChecker(NS+"leniz.16").category(LENIZ_CLASS).atTheEnd("ìnu","ìn");
    }

    /**
     * -ùnu > -ùn
     */
    @Test
    void testLeniz17() throws IOException{
        getChecker(NS+"leniz.17").category(LENIZ_CLASS).atTheEnd("ùnu","ùn");
    }

    /*
     * Leniz 19 -chi- | -chì- | -chj- | -chï- > -ghj- | -ghï-
     */

    /**
     * -chi- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19a() throws IOException{
       getChecker(NS+"leniz.19.a").category(LENIZ_CLASS).betweenVowels(true, "chi", "ghj");
    }

    /**
     * -chi- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19b() throws IOException{
        getChecker(NS+"leniz.19.b").category(LENIZ_CLASS).betweenVowels(true, "chi", "ghï");
    }

    /**
     * -chì- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19c() throws IOException{
        getChecker(NS+"leniz.19.c").category(LENIZ_CLASS).betweenVowels(true, "chì", "ghj");
    }

    /**
     * -chì- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19d() throws IOException{
        getChecker(NS+"leniz.19.d").category(LENIZ_CLASS).betweenVowels(true, "chì", "ghï");
    }

    /**
     * -chj- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19e() throws IOException{
        getChecker(NS+"leniz.19.e").category(LENIZ_CLASS).betweenVowels(true, "chj", "ghj");
    }

    /**
     * -chj- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19f() throws IOException{
        getChecker(NS+"leniz.19.f").category(LENIZ_CLASS).betweenVowels(true, "chj", "ghï");
    }

    /**
     * -chï- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19g() throws IOException{
        getChecker(NS+"leniz.19.g").category(LENIZ_CLASS).betweenVowels(true, "chï","ghj");
    }

    /**
     * -chï- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19h() throws IOException{
        getChecker(NS+"leniz.19.h").category(LENIZ_CLASS).betweenVowels(true, "chï", "ghï");
    }

    /**
     * spr / sbr > sbr
     */
    @Test
    void testLeniz20() throws IOException{
        getChecker(NS+"leniz.20").category(LENIZ_CLASS).replacing("spr", "sbr");
    }

    /**
     * -tt- > -it-
     * was f20
     */
    @Test
    void testDegem1() throws IOException{
        getChecker(NS+"degem.1").category(DEGEM_CLASS).betweenVowels(true, "tt","it");
    }

    /**
     * -tt- > -t-
     * was f21
     */
    @Test
    void testDegem2() throws IOException{
        getChecker(NS+"degem.2").category(DEGEM_CLASS).replacing("tt","t");
    }

    /**
     * dd > d
     */
    @Test
    void testDegem3() throws IOException{
        getChecker(NS+"degem.3").category(DEGEM_CLASS).replacing("dd","d");
    }

    /**
     * -ḍḍ- > -dd-
     */
    @Test
    void testDegem4() throws IOException {
        getChecker(NS+"degem.4").category(DEGEM_CLASS).replacing("ḍḍ","dd");
    }

    /**
     * -pp- > -p-
     */
    @Test
    void testDegem5() throws IOException{
        getChecker(NS+"degem.5").category(DEGEM_CLASS).replacing("pp","p");
    }

    /**
     * bb > b
     */
    @Test
    void testDegem6() throws IOException{
        getChecker(NS+"degem.6").category(DEGEM_CLASS)
                .inside(false, "bb","b")
                .atTheEnd(false, "bb","b")
                .notApply("bb456");
    }

    /**
     * cc > c
     * was f25
     */
    @Test
    void testDegem7() throws IOException{
        getChecker(NS+"degem.7").category(DEGEM_CLASS)
                .atTheBeginning(false, "cc","c")
                .inside(false, "cc","c")
                .notApply("123ccari")
                .derives("zzìcca", "zzìca")
                .derives("123ccar", "123car")
                .derives("123ccarx", "123carx");
    }

    /**
     * -ccàri > -chè
     */
    @Test
    void testDegem8() throws IOException {
        getChecker(NS+"degem.8").category(DEGEM_CLASS).atTheEnd("ccàri","chè");
    }

    /**
     * gg > g but not in -ggari
     * was f27
     */
    @Test
    void testDegem9() throws IOException{
        getChecker(NS+"degem.9").category(DEGEM_CLASS)
                .atTheBeginning(false, "gg", "g")
                .inside(false, "gg","g")
                .notApply("123ggari")
                .derives("123gga", "123ga")
                .derives("123ggar", "123gar")
                .derives("123ggarx", "123garx");
    }

    /**
     *  -ggàri > -ghè
     */
    @Test
    void testDegem10() throws IOException {
        getChecker(NS+"degem.10").category(DEGEM_CLASS).atTheEnd("ggàri", "ghè");
    }


    /**
     * ff > f
     */
    @Test
    void testDegem11() throws IOException{
        getChecker(NS+"degem.11").category(DEGEM_CLASS).replacing("ff","f");
    }

    /**
     * vv > v
     * was degem.14
     */
    @Test
    void testDegem12() throws IOException{
        getChecker(NS+"degem.12").category(DEGEM_CLASS).replacing("vv","v");
    }

    /**
     * ss- > s-
     * was f33
     */
    @Test
    void testDegem13() throws IOException{
        getChecker(NS+"degem.13").category(DEGEM_CLASS).atTheBeginning("ss","s");
    }

    /**
     * żż > ẕẕ
     * was f34
     */
    @Test
    void testDegem15() throws IOException{
        getChecker(NS+"degem.15").category(DEGEM_CLASS).replacing("żż","ẕẕ");
    }


    /**
     * mm > m
     * was f39
     */
    @Test
    void testDegem16() throws IOException{
        getChecker(NS+"degem.16").category(DEGEM_CLASS).replacing("mm","m");
    }

    /**
     * nn > n
     */
    @Test
    void testDegem17() throws IOException{
        getChecker(NS+"degem.17").category(DEGEM_CLASS).replacing("nn","n");
    }

    /**
     * l- > dd-
     */
    @Test
    void testDegem18() throws IOException{
        getChecker(NS+"degem.18").category(DEGEM_CLASS).atTheBeginning("l","dd");
    }

    /**
     * -ll- > -
     */
    @Test
    void testDegem19() throws IOException{
        getChecker(NS+"degem.19").category(DEGEM_CLASS).betweenVowels(true, "ll", "");
    }

    /**
     * -ll- > -l-
     */
    @Test
    void testDegem19b() throws IOException{
        getChecker(NS+"degem.19.b").category(DEGEM_CLASS).betweenVowels(true, "ll", "l");
    }

    /**
     * -ḍḍ- > -
     */
    @Test
    void testDegem20() throws IOException{
        getChecker(NS+"degem.20").category(DEGEM_CLASS).betweenVowels(true, "ḍḍ","");
    }

    /**
     * ḍḍr > dr
     */
    @Test
    void testDegem22() throws IOException{
        getChecker(NS+"degem.22").category(DEGEM_CLASS).replacing("ḍḍr","dr");
    }

    /**
     * ṭṭṛ > tr
     */
    @Test
    void testDegem23() throws IOException{
        getChecker(NS+"degem.23").category(DEGEM_CLASS).replacing("ṭṭṛ","tr");
    }

    //ASSIBILAZIONE

    /*
     * assib.1 si >  sge / sgë / sgi
     */

    /**
     * si > sge
     * @throws IOException missing phenomenon
     */
    @Test
    void testAssib1a() throws IOException{
        getChecker(NS+"assib.1.a").category(ASSIB_CLASS).replacing("si","sge");
    }

    /**
     * si > sgë
     * @throws IOException missing phenomenon
     */
    @Test
    void testAssib1b() throws IOException{
        getChecker(NS+"assib.1.b").category(ASSIB_CLASS).replacing("si","sgë");
    }

    /**
     * si > sgi
     * @throws IOException missing phenomenon
     */
    @Test
    void testAssib1c() throws IOException{
        getChecker(NS+"assib.1.c").category(ASSIB_CLASS).replacing("si","sgi");
    }

    /**
     * sì > sgì
     */
    @Test
    void testAssib2() throws IOException{
        getChecker(NS+"assib.2").category(ASSIB_CLASS).replacing("sì","sgì");
    }

    /**
     * sï > sgï
     */
    @Test
    void testAssib3() throws IOException{
        getChecker(NS+"assib.3").category(ASSIB_CLASS)
                .replacing("sï","sgï");
    }

    /**
     * ce > sge
     */
    @Test
    void testAssib4() throws IOException{
        getChecker(NS+"assib.4").category(ASSIB_CLASS)
                .replacing("ce","sge");
    }

    /**
     * cè > sgè
     */
    @Test
    void testAssib5() throws IOException{
        getChecker(NS+"assib.5").category(ASSIB_CLASS)
                .replacing("cè","sgè");
    }

    /*
     * Assib.6 ci > sge | sgë | sgi
     */
    /**
     * ci > sge
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib6a() throws IOException{
        getChecker(NS+"assib.6.a").category(ASSIB_CLASS)
                .replacing("ci","sge");
    }

    /**
     * ci > sgë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib6b() throws IOException{
        getChecker(NS+"assib.6.b").category(ASSIB_CLASS).atTheEnd("ci","sgë");
    }

    /**
     * ci > sgë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib6c() throws IOException{
        getChecker(NS+"assib.6.c").category(ASSIB_CLASS)
                .replacing("ci","sgi");
    }

    // TODO assib.6.d -ci- > -sgi/sge overlaps with assib.6.b and assib.6.c
    /**
     * cì > sgi
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib7() throws IOException{
        getChecker(NS+"assib.7").category(ASSIB_CLASS)
                .replacing("cì","sgì");
    }

    /**
     * cï >  sgï
     */
    @Test
    void testAssib8() throws IOException{
        getChecker(NS+"assib.7").category(ASSIB_CLASS)
                .replacing("cì","sgì");
    }

    /**
     * nce / ncè / nci / ncì / ncï > nze / nzè / nzi / nzì / nzï
     */
    @Test
    void testAssib9() throws IOException{
        getChecker(NS+"assib.9").category(ASSIB_CLASS)
                .replacing("nce", "nze")
                        .replacing("ncè","nzè")
                                .replacing("nci","nzi")
                                        .replacing("ncì","nzì")
                                                .replacing("ncï","nzï")
                .notApply("nca");
    }

    /**
     * nge / ngè / ngi / ngì / ngï >  nẕe / nẕè / nẕi / nẕì / nẕï
     */
    @Test
    void testAssib10() throws IOException{
        getChecker(NS+"assib.10").category(ASSIB_CLASS)
                .replacing("nge","nẕe")
                .replacing("ngè","nẕè")
                .replacing("ngi","nẕi")
                .replacing("ngì", "nẕì")
                .replacing("ngï","nẕï")
                .notApply("nga");
    }

    /**
     * -rce- / -rcè / -rci-/ -rcì- / -rcï- > -rze- / -rzè / -rzi- / -rzì- / -rzï-
     */
    @Test
    void testAssib11() throws IOException{
        getChecker(NS+"assib.11").category(ASSIB_CLASS)
                .notEndingprecededByVowel("rce","rze")
                .endingPrecededByVowel("rcè", "rzè")
                .notEndingprecededByVowel("rci","rzi")
                .notEndingprecededByVowel("rcì","rzì")
                .notEndingprecededByVowel("rcï", "rzï");
    }

    /**
     * ce- / cè- / ci- / cì- / cï- > zze- / zzè- /zzi- / zzì- / zzï-
     */
    @Test
    void testAssib12() throws IOException{
        getChecker(NS+"assib.12").category(ASSIB_CLASS)
                .atTheBeginning("ce","zze")
                .atTheBeginning("cè", "zzè")
                .atTheBeginning("ci", "zzi")
                .atTheBeginning("cì", "zzì")
                .atTheBeginning("cï", "zzï");
    }

    /**
     * ge- / gè- / gi- / gì- / gï- > zze- / zze- / ẕẕi- / ẕẕì- / zzï-
     */
    @Test
    void testAssib13() throws IOException {
        getChecker(NS + "assib.13").category(ASSIB_CLASS)
                .atTheBeginning("ge", "ẕẕe")
                .atTheBeginning("gè", "ẕẕè")
                .atTheBeginning("gi", "ẕẕi")
                .atTheBeginning("gì", "ẕẕì")
                .atTheBeginning("gï", "ẕẕï")
                .notApply("ga");
    }

    /**
     * se- / sè- / si- / sì- > zze- /zzè- / zzi- / zzì-
     */
    @Test
    void testAssib14() throws IOException{
        getChecker(NS+"assib.14").category(ASSIB_CLASS)
                .atTheBeginning("se", "zze")
                .atTheBeginning("sè", "zzè")
                .atTheBeginning("si", "zzi")
                .atTheBeginning("sì", "zzì")
                .notApply("sa");
    }

    /**
     * -ii- > ẕẕ
     */
    @Test
    void testAssib15() throws IOException{
        getChecker(NS+"assib.15").category(ASSIB_CLASS).inside("ii","ẕẕ");
    }

    /**
     * -ìiri> ẕẕö
     */
    @Test
    void testAssib16() throws IOException{
        getChecker(NS+"assib.16").category(ASSIB_CLASS).atTheEnd("ìiri","ẕẕö");
    }

    /**
     * ggi > i
     */
    @Test
    void testAssib17() throws IOException{
        getChecker(NS+"assib.17").category(ASSIB_CLASS).replacing("ggi", "i");
    }

    // Dissimilazione dei nessi MB e ND

    /**
     * mm > mb
     */
    @Test
    void testDissim1() throws IOException{
        getChecker(NS+"dissim.1").category(DISSIM_CLASS)
                .replacing("mm","mb");
    }

    /**
     * nn > nd
     */
    @Test
    void testDissim2() throws IOException{
        getChecker(NS+"dissim.2").category(DISSIM_CLASS)
                .replacing("nn", "nd")
                .derives("munnàri", "mundàri");
    }

    // Dittongazione non metafonetica

    /**
     * è- > iè-
     */
    @Test
    void testDitt1a() throws IOException{
        getChecker(NS+"ditt.1.a").category(DITT_CLASS)
                .atTheBeginning("è", "iè");
    }

    /**
     * -è- > iè
     */
    @Test
    void testDitt1b() throws IOException{
        getChecker(NS+"ditt.1.b").category(DITT_CLASS)
                .notApply("è456")
                .notApply("iè456")
                .notApply("123iè456")
                .notApply("123iè").
                inside(false, "è", "iè")
                .atTheEnd(false, "è", "iè");
    }

    /**
     * ò- > uò-
     */
    @Test
    void testDitt2a() throws IOException{
        getChecker(NS+"ditt.2.a").category(DITT_CLASS)
                .atTheBeginning("ò", "uò");
    }

    /**
     * -ò- > uò
     */
    @Test
    void testDitt2b() throws IOException{
        getChecker(NS+"ditt.2.b").category(DITT_CLASS)
                .notApply("ò456")
                .notApply("uò456")
                .notApply("123uò456")
                .notApply("123uò")
                .inside(false, "ò", "uò")
                .atTheEnd(false, "ò", "uò");
    }

    // Mantenimento del sistema vocalico settentrionale

    /**
     * -i > ë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal2a() throws IOException{
        getChecker(NS+"vocal.2.a").category(VOCAL_CLASS).atTheEnd("i", "ë");
    }

    /**
     * u > ö
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal5a() throws IOException{
        getChecker(NS+"vocal.5.a").category(VOCAL_CLASS).replacing("u", "ö");
    }

    /**
     * ù > ö̀
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal5c() throws IOException{
        getChecker(NS+"vocal.5.c").category(VOCAL_CLASS).replacing("ù", "ö̀");
    }

    /**
     * -i > e here - is a placeholder for any char.
     */
    @Test
    void testVocal7a() throws IOException {
        getChecker(NS+"vocal.7.a").category(VOCAL_CLASS).atTheEnd("i", "e");
    }

    /**
     * i- | -i- > e except where i is followed by a vowel. Here - stands for any char.
     */
    @Test
    void testVocal7b() throws IOException{

        final RegexLinguisticPhenomenonChecker checker=getChecker(NS+"vocal.7.b").category(VOCAL_CLASS)
                .atTheBeginning(false, "i","e").inside(false, "i","e").build();
        for(final char vowel: RegexLinguisticPhenomenonChecker.VOWELS.toCharArray()){
            if (vowel!='i')
                checker.notApply("i"+vowel+"456").notApply("123i"+vowel+"456").notApply("123i"+vowel);
        }
    }


    /**
     * u > e if u is not preceded by c or g
     */
    @Test
    void testVocal8a() throws IOException{
        getChecker(NS+"vocal.8.a").category(VOCAL_CLASS)
                .notApply("cu456")
                .notApply("123cu456")
                .notApply("123cu")
                .notApply("gu456")
                .notApply("123gu456")
                .notApply("gu456")
                .replacing("u","e");
    }

    /**
     * cu > che
     */
    @Test
    void testVocal8b() throws IOException{
        getChecker(NS+"vocal.8.b").category(VOCAL_CLASS)
                .replacing("cu","che");
    }

    /**
     * gu > ghe
     */
    @Test
    void testVocal8c() throws IOException{
        getChecker(NS+"vocal.8.c").category(VOCAL_CLASS)
                .replacing("gu","ghe");
    }

    /**
     * -èri > -èrö
     */
    @Test
    void testVocal9a() throws IOException{
        getChecker(NS+"vocal.9.a").category(VOCAL_CLASS).atTheEnd("èri", "èrö");
    }

    /**
     * àli > àö
     * @throws IOException if vocal.9.b does not exist
     */
    @Test
    void testVocal9b() throws IOException{
        getChecker(NS+"vocal.9.b").category(VOCAL_CLASS).atTheEnd("àli", "àö");
    }

    /**
     * ì > ë̀
     * @throws IOException if vocal.10 does not exist
     */
    @Test
    void testVocal10() throws IOException{
        getChecker(NS+"vocal.10").category(VOCAL_CLASS).replacing("ì", "ë̀");
    }

    /**
     * -a- > -e-
     * @throws IOException if vocal.12 does not exist
     *
     * TODO it is unclear if - stays for consonants or any letter
     */
    @Test
    void testVocal12() throws IOException{
        getChecker(NS+"vocal.12").category(VOCAL_CLASS).inside("a","e");
    }

    /**
     * -i > -ö
     * @throws IOException if vocal.13 does not exist
     */
    @Test
    void testVocal13() throws IOException{
        getChecker(NS+"vocal.13").category(VOCAL_CLASS).atTheEnd("i", "ö");
    }

    //TODo vocal.11 u > ö | u > e if u is not preceded by c or g already expressed in vocal.5.a vocal.8.a

    // Aferesi di a-

    /**
     * a- > -
     */
    @Test
    void testAfer1() throws IOException{
        getChecker(NS+"afer.1").category(AFER_CLASS)
                .atTheBeginning("a","");
    }

    /**
     * all- > l-
     */
    @Test
    void testAfer1b() throws IOException{
        getChecker(NS+"afer.1.b").category(AFER_CLASS)
                .atTheBeginning("all","l");
    }

    // Palatalizzazione di /a/
    /**
     * -càri > chè
     */
    @Test
    void testPalat1() throws IOException{
        getChecker(NS+"palat.1").category(PALAT_CLASS).atTheEnd("càri", "chè");
    }

    /**
     * -càrisi > chèssë
     */
    @Test
    void testPalat2() throws IOException{
        getChecker(NS+"palat.2").category(PALAT_CLASS).atTheEnd("càrisi",  "chèssë");
    }

    /**
     * -gàri > ghè
     */
    @Test
    void testPalat3() throws IOException{
        getChecker(NS+"palat.3").category(PALAT_CLASS).atTheEnd("gàri", "ghè");
    }

    /**
     * -gàrisi > ghèssë
     */
    @Test
    void testPalat4() throws IOException{
        getChecker(NS+"palat.4").category(PALAT_CLASS).atTheEnd("gàrisi", "ghèssë");
    }

    /**
     * -àri > è if not preceded by c, g, i
     */
    @Test
    void testPalat5() throws IOException{
        getChecker(NS+"palat.5").category(PALAT_CLASS).atTheEnd("àri", "è")
                .notApply("123càri")
                .notApply("123cari")
                .notApply("123gàri")
                .notApply("123gari")
                .notApply("123iari")
                .notApply("123iàri");

    }

    /**
     * -àrisi > èssë if not preceded by c, g, i
     */
    @Test
    void testPalat6() throws IOException{
        getChecker(NS+"palat.6").category(PALAT_CLASS)
                .atTheEnd("àrisi", "èssë")
                .notApply("123càrisi")
                .notApply("123gàrisi")
                .notApply("123iàrisi");
    }

    /**
     * -ciàri > cè
     */
    @Test
    void testPalat7() throws IOException{
        getChecker(NS+"palat.7").category(PALAT_CLASS).atTheEnd("ciàri", "cè");
    }

    /**
     * -iàrisi > èssë
     */
    @Test
    void testPalat8() throws IOException{
        getChecker(NS+"palat.8").category(PALAT_CLASS).atTheEnd("iàrisi", "èssë");
    }

    /**
     * -giàri > gè
     */
    @Test
    void testPalat9() throws IOException{
        getChecker(NS+"palat.9").category(PALAT_CLASS).atTheEnd("giàri", "gè");
    }

    /**
     *  -ïàri > ïè
     */
    @Test
    void testPalat10() throws IOException{
        getChecker(NS+"palat.10").category(PALAT_CLASS).atTheEnd( "ïàri", "ïè");
    }

    /**
     * -ïàrisi > ïèssë
     */
    @Test
    void testPalat11() throws IOException{
        getChecker(NS+"palat.11").category(PALAT_CLASS).atTheEnd("ïàrisi","ïèssë");
    }

    /**
     * -àrini > ènë
     */
    @Test
    void testPalat12() throws IOException{
        getChecker(NS+"palat.12").category(PALAT_CLASS).atTheEnd("àrini","ènë");
    }

    /**
     * -arisìnni > èssenë
     */
    @Test
    void testPalat13() throws IOException{
        getChecker(NS+"palat.13").category(PALAT_CLASS).atTheEnd("arisìnni","èssenë");
    }

    //  Eliminazione di varianti allofoniche siciliane in posizione debole

    /**
     * v- > b-
     */
    @Test
    void testElim1() throws IOException{
        getChecker(NS+"elim.1").category(ELIM_CLASS).atTheBeginning("v", "b");
    }

    /**
     * bb- > b-
     */
    @Test
    void testElim2() throws IOException{
        getChecker(NS+"elim.2").category(ELIM_CLASS).atTheBeginning(true, "bb", "b");
    }

    /**
     * r- > br-
     */
    @Test
    void testElim3() throws IOException{
        getChecker(NS+"elim.3").category(ELIM_CLASS).atTheBeginning(true, "r", "br");
    }

    /*
     * elim.4 i- > g-|gi-
     */

    /**
     * i- > g-
     * @throws IOException on missing phenomenon
     */
    @Test
    void testElim4a() throws IOException{
        getChecker(NS+"elim.4.a").category(ELIM_CLASS).atTheBeginning(true, "i", "g");
    }

    /**
     * i- > gi-
     * @throws IOException on missing phenomenon
     */
    @Test
    void testElim4b() throws IOException{
        getChecker(NS+"elim.4.b").category(ELIM_CLASS).atTheBeginning(true, "i","gi");
    }

    /**
     * r- > gr-
     */
    @Test
    void testElim5() throws IOException{
        getChecker(NS+"elim.5").category(ELIM_CLASS).atTheBeginning(true, "r", "gr");
    }

    // Deretroflessione

    /**
     * ṭṛ > tr
     */
    @Test
    void testDeretr1() throws IOException{
        getChecker(NS+"deretr.1").category(DERETR_CLASS).replacing("ṭṛ", "tr");
    }

    /**
     * ṭṛ > tr
     */
    @Test
    void testDeretr1Trazzera() throws IOException{
        final LinguisticPhenomenon deretr1=getChecker(NS+"deretr.1").getPhenomenon();
        final Set<String> actual0=deretr1.apply("ṭṛazziera");
        assertEquals(1, actual0.size());
        assertEquals("trazziera", actual0.iterator().next());
    }

    /**
     * ṭṭṛ > ttr
     * was f82
     */
    @Test
    void testDeretr2() throws IOException{
        getChecker(NS+"deretr.2").category(DERETR_CLASS).replacing("ṭṭṛ", "ttr");
    }

    /**
     * ṣṭṛ > str
     * was f38
     */
    @Test
    void testDeretr3() throws IOException{
        getChecker(NS+"deretr.3").category(DERETR_CLASS).replacing("ṣṭṛ", "str");
    }

    // Desinenze infiniti (=INF)
    /**
     * -ìri > -ë̀
     */
    @Test
    void testInf1() throws IOException{
        getChecker(NS+"inf.1").category(PALAT_CLASS).atTheEnd("ìri", "ë̀");
    }

    /**
     * -ìri > ì
     */
    @Test
    void testInf2() throws IOException{
        getChecker(NS+"inf.2").category(PALAT_CLASS).atTheEnd("ìri","ì");
    }

    /**
     * -iri > ö only if iri is not preceeded by one of the followings: c,g,j,h
     */
    @Test
    void testInf3a() throws IOException{
        getChecker(NS+"inf.3.a").category(PALAT_CLASS).atTheEnd("iri","ö")
                .notApply("123ciri")
                .notApply("123giri")
                .notApply("123jiri")
                .notApply("123hiri");
    }

    /**
     *  -ciri > ciö
     */
    @Test
    void testInf3b1() throws IOException{
        getChecker(NS+"inf.3.b.1").category(PALAT_CLASS)
                .atTheEnd("ciri", "ciö");
    }

    /**
     *  -giri > giö
     */
    @Test
    void testInf3b2() throws IOException{
        getChecker(NS+"inf.3.b.2").category(PALAT_CLASS)
                .atTheEnd("giri","giö");
    }

    /**
     *  -chjiri > chjiö
     */
    @Test
    void testInf3b3() throws IOException{
        getChecker(NS+"inf.3.b.3").category(PALAT_CLASS)
                .atTheEnd("chjiri", "chjiö");
    }

    /**
     *  -ghjiri > ghjiö
     */
    @Test
    void testInf3b4() throws IOException{
        getChecker(NS+"inf.3.b.4").category(PALAT_CLASS)
                .atTheEnd("ghjiri", "ghjiö");
    }

    /**
     * -jiri > jö
     */
    @Test
    void testInf3c() throws IOException{
        getChecker(NS+"inf.3.c").category(PALAT_CLASS)
                .atTheEnd("jiri","jö");
    }

    /**
     * -hiri > hjö
     */
    @Test
    void testInf3d() throws IOException{
        getChecker(NS+"inf.3.d").category(PALAT_CLASS)
                .atTheEnd("hiri","hjö");
    }

    /**
     * -ìri > ìsciö
     */
    @Test
    void testInf4() throws IOException{
        getChecker(NS+"inf.4").category(PALAT_CLASS).atTheEnd("ìri","ìsciö");
    }

    /**
     * -ìrisi > -essë provided that ìrisi is not preceded by SC
     */
    @Test
    void testInf5() throws IOException{
        getChecker(NS+"inf.5").category(PALAT_CLASS)
                .atTheEnd("ìrisi", "essë")
                .notApply("123scìrisi");
    }

    /**
     * -scìrisi > essë
     */
    @Test
    void testInf6() throws IOException{
        getChecker(NS+"inf.6").category(PALAT_CLASS).atTheEnd("scìrisi","essë");
    }

    /**
     * -ìricci > ë̀ghjë
     */
    @Test
    void testInf7() throws IOException{
        getChecker(NS+"inf.7").category(PALAT_CLASS).atTheEnd("ìricci","ë̀ghjë");
    }

    @Test
    void shouldGetTheOntologyName() throws IOException {
        try(final GSFeatures gs = new GSFeatures()) {
            assertNotNull(gs.getName());
        }
    }

    @Test
    void shouldReturnAllCategories() throws IOException {
        final String[] allExpected={AFER_CLASS, ASSIB_CLASS, DEGEM_CLASS, DERETR_CLASS, DISSIM_CLASS, DITT_CLASS,
                ELIM_CLASS, LENIZ_CLASS, PALAT_CLASS, VOCAL_CLASS,};

        try(final GSFeatures gs = new GSFeatures()) {
            final List<GSFeaturesCategory> allActual=gs.getCategories();
            final Iterator<GSFeaturesCategory> actualIt=allActual.iterator();
            for (String s : allExpected) {
                assertTrue(actualIt.hasNext());
                final GSFeaturesCategory actual = actualIt.next();
                assertEquals(s, actual.getIri());
                assertEquals(s, NS + actual.getId());
                assertNotNull(actual.getLabel());
                assertNotNull(actual.getComment());
            }
            assertFalse(actualIt.hasNext());
        }

    }

    @Test
    void shouldReturnApheresisCategoryChildren() throws IOException {
        try(final GSFeatures gs = new GSFeatures()) {
            final GSFeaturesCategory actualApheresis=gs.getCategories().get(0);
            assertEquals(AFER_CLASS, actualApheresis.getIri());
            final Set<HashedOntologyItem> actualApheresisChildren=actualApheresis.getMembers();
            assertEquals(1, actualApheresisChildren.size());
            final HashedOntologyItem afer1=actualApheresisChildren.iterator().next();
            assertEquals(NS+"afer.1", afer1.getIri());
            assertEquals("afer.1", afer1.getLabel());
            assertEquals("a- > -", afer1.getComment());
        }

    }

    @Test
    void shouldReturnInfFeaturesUnderPalat() throws IOException {
        try(final GSFeatures gs = new GSFeatures()) {
            final GSFeaturesCategory actualPalat=getPalat(gs);
            int n=0;
            for(final HashedOntologyItem f: actualPalat.getMembers()){
                if (f.getId().startsWith("inf.")) {
                    n++;
                    System.out.println(f.getId());
                }
            }
            assertNotEquals(0, n, "No inf feature found under Palat");
        }
    }

    GSFeaturesCategory getPalat(final GSFeatures gs){
        for(final GSFeaturesCategory c: gs.getCategories())
            if (GSFeatures.PALAT_CLASS.equals(c.getIri())) return c;
        fail("No Palat Features Category");
        return null;
    }

}
