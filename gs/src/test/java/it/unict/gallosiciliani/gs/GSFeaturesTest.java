package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.*;
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
    void shouldProvideLabelForPhenomena() throws IOException {
        final Locale locale = Locale.ENGLISH;
        try(final GSFeatures gs = new GSFeatures()) {
            for(final LinguisticPhenomenon p : gs.getRegexLinguisticPhenomena())
                assertNotNull(LABEL_PROVIDER_ID.getLabel(p, locale), "label for " + p + " not found");
        }
    }

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

    /**
     * Get a helper to test the feature with the IRI.
     * @param featureIRI final feature code
     * @return helper to test the specified feature
     */
    private RegexLinguisticPhenomenonChecker getChecker(final String featureIRI, final String replacement) throws IOException {
        return getChecker(featureIRI).build(replacement);
    }

    private RegexLinguisticPhenomenonCheckerFactory getChecker(final String featureIRI) throws IOException {
        return new RegexLinguisticPhenomenonCheckerFactory(getFeature(featureIRI));
    }

    /**
     * Get the feature with the specified IRI
     * @param iri phenomenon IRI
     * @return the phenomenon with the specified IRI
     * @throws IllegalArgumentException if no such phenomenon exists among GS features
     */
    private LinguisticPhenomenon getFeature(final String iri) throws IOException {
        try(final GSFeatures ont = new GSFeatures()) {
            RegexLinguisticPhenomenaReader r=new RegexLinguisticPhenomenaReader();
            r.read(ont.getModel(), new FiniteStatePhenomenaQuery());
            final List<LinguisticPhenomenon> allRegexFeatures=r.getFeatures();
//            final List<LinguisticPhenomenon> allRegexFeatures = ont.getRegexLinguisticPhenomena();
            assertFalse(allRegexFeatures.isEmpty());
            for (final LinguisticPhenomenon f : allRegexFeatures)
                if (iri.equals(f.getId()))
                    return f;
            throw new IllegalArgumentException("Unable to get feature " + iri);
        }

    }

    /**
     * leniz.1 -c-  > -g-
     *
     */
    @Test
    void testLeniz1() throws IOException {
        final RegexLinguisticPhenomenonChecker checker= getChecker(NS+"leniz.1","g")
                .betweenVowels(true, "c");
        for(final char v: RegexLinguisticPhenomenonChecker.VOWELS.toCharArray())
                checker.derives("123"+v+"ch456", "123"+v+"gh456");
        checker.notApply("accutiddari");
    }

    /**
     * leniz.2 -cr- > -gr-
     */
    @Test
    void testLeniz2() throws IOException{
        getChecker(NS+"leniz.2","gr").betweenVowels(true, "cr");

    }

    /**
     * -p-  > -v-
     */
    @Test
    void testLeniz3() throws IOException{
        getChecker(NS+"leniz.3","v").betweenVowels(true, "p");
    }

    /**
     * -pr- > -vr-
     */
    @Test
    void testLeniz4() throws IOException{
        getChecker(NS+"leniz.4","vr").betweenVowels(true, "pr");
    }

    /**
     * -t- > -d-
     */
    @Test
    void testLeniz5() throws IOException{
        getChecker(NS+"leniz.5","d").betweenVowels(true, "t");
    }

    /**
     * -ìtu > -ì
     */
    @Test
    void testLeniz6() throws IOException{
        getChecker(NS+"leniz.6","ì").atTheEnd(true, "ìtu");
    }

    /**
     * -àtu > -à
     */
    @Test
    void testLeniz7() throws IOException{
        getChecker(NS+"leniz.7","à").atTheEnd(true, "àtu");
    }

    /**
     * -ùtu > -ù
     */
    @Test
    void testLeniz8() throws IOException{
        getChecker(NS+"leniz.8","ù").atTheEnd(true, "ùtu");
    }

    /**
     * -ìti > -ë̀
     */
    @Test
    void testLeniz9() throws IOException{
        getChecker(NS+"leniz.9","ë̀").atTheEnd(true, "ìti");
    }

    /**
     * -ṭṛ- > -ir-
     */
    @Test
    void testLeniz10() throws IOException{
        getChecker(NS+"leniz.10", "ir").betweenVowels(true, "ṭṛ");
    }

    /**
     * -dr- > -ir-
     */
    @Test
    void testLeniz11() throws IOException{
        getChecker(NS+"leniz.11", "ir").betweenVowels(true, "dr");
    }

    /**
     * -l- > --
     */
    @Test
    void testLeniz12() throws IOException{
        getChecker(NS+"leniz.12","").betweenVowels(true, "l");
    }

    /**
     * -àni > -àn
     */
    @Test
    void testLeniz13() throws IOException{
        getChecker(NS+"leniz.13","àn").atTheEnd(true, "àni");
    }

    /**
     * -ùni > -ö̀n
     */
    @Test
    void testLeniz14() throws IOException{
        getChecker(NS+"leniz.14","ö̀n").atTheEnd(true, "ùni");
    }

    /**
     * -ìnu > -ìn
     */
    @Test
    void testLeniz16() throws IOException{
        getChecker(NS+"leniz.16","ìn").atTheEnd(true, "ìnu");
    }

    /**
     * -ùnu > -ùn
     */
    @Test
    void testLeniz17() throws IOException{
        getChecker(NS+"leniz.17","ùn").atTheEnd(true, "ùnu");
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
       getChecker(NS+"leniz.19.a", "ghj").betweenVowels(true, "chi");
    }

    /**
     * -chi- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19b() throws IOException{
        getChecker(NS+"leniz.19.b", "ghï").betweenVowels(true, "chi");
    }

    /**
     * -chì- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19c() throws IOException{
        getChecker(NS+"leniz.19.c", "ghj").betweenVowels(true, "chì");
    }

    /**
     * -chì- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19d() throws IOException{
        getChecker(NS+"leniz.19.d", "ghï").betweenVowels(true, "chì");
    }

    /**
     * -chj- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19e() throws IOException{
        getChecker(NS+"leniz.19.e", "ghj").betweenVowels(true, "chj");
    }

    /**
     * -chj- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19f() throws IOException{
        getChecker(NS+"leniz.19.f", "ghï").betweenVowels(true, "chj");
    }

    /**
     * -chï- > -ghj-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19g() throws IOException{
        getChecker(NS+"leniz.19.g", "ghj").betweenVowels(true, "chï");
    }

    /**
     * -chï- > -ghï-
     * @throws IOException if not exists
     */
    @Test
    void testLeniz19h() throws IOException{
        getChecker(NS+"leniz.19.h", "ghï").betweenVowels(true, "chï");
    }

    /**
     * spr / sbr > sbr
     */
    @Test
    void testLeniz20() throws IOException{
        getChecker(NS+"leniz.20","sbr").replacing("spr");
    }

    /**
     * -tt- > -it-
     * was f20
     */
    @Test
    void testDegem1() throws IOException{
        getChecker(NS+"degem.1","it").betweenVowels(true, "tt");
    }

    /**
     * -tt- > -t-
     * was f21
     */
    @Test
    void testDegem2() throws IOException{
        getChecker(NS+"degem.2","t").replacing("tt");
    }

    /**
     * dd > d
     */
    @Test
    void testDegem3() throws IOException{
        getChecker(NS+"degem.3","d").replacing("dd");
    }

    /**
     * -ḍḍ- > -dd-
     */
    @Test
    void testDegem4() throws IOException {
        getChecker(NS+"degem.4","dd").replacing("ḍḍ");
    }

    /**
     * -pp- > -p-
     */
    @Test
    void testDegem5() throws IOException{
        getChecker(NS+"degem.5","p").replacing("pp");
    }

    /**
     * bb > b
     */
    @Test
    void testDegem6() throws IOException{
        getChecker(NS+"degem.6","b")
                .inside(false, "bb")
                .atTheEnd(false, "bb")
                .notApply("bb456");
    }

    /**
     * cc > c
     * was f25
     */
    @Test
    void testDegem7() throws IOException{
        getChecker(NS+"degem.7","c").atTheBeginning(false, "cc")
                .inside(false, "cc").notApply("123ccari")
                .derives("zzìcca", "zzìca")
                .derives("123ccar", "123car")
                .derives("123ccarx", "123carx");
    }

    /**
     * -ccàri > -chè
     */
    @Test
    void testDegem8() throws IOException {
        getChecker(NS+"degem.8").atTheEnd("ccàri","chè");
    }

    /**
     * gg > g but not in -ggari
     * was f27
     */
    @Test
    void testDegem9() throws IOException{
        getChecker(NS+"degem.9","g").atTheBeginning(false, "gg").inside(false, "gg")
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
        getChecker(NS+"degem.10").atTheEnd("ggàri", "ghè");
    }


    /**
     * ff > f
     */
    @Test
    void testDegem11() throws IOException{
        getChecker(NS+"degem.11","f").replacing("ff");
    }

    /**
     * vv > v
     * was degem.14
     */
    @Test
    void testDegem12() throws IOException{
        getChecker(NS+"degem.12","v").replacing("vv");
    }

    /**
     * ss- > s-
     * was f33
     */
    @Test
    void testDegem13() throws IOException{
        getChecker(NS+"degem.13","s").atTheBeginning(true, "ss");
    }

    /**
     * żż > ẕẕ
     * was f34
     */
    @Test
    void testDegem15() throws IOException{
        getChecker(NS+"degem.15","ẕẕ").replacing("żż");
    }


    /**
     * mm > m
     * was f39
     */
    @Test
    void testDegem16() throws IOException{
        getChecker(NS+"degem.16","m").replacing("mm");
    }

    /**
     * nn > n
     */
    @Test
    void testDegem17() throws IOException{
        getChecker(NS+"degem.17","n").replacing("nn");
    }

    /**
     * l- > dd-
     */
    @Test
    void testDegem18() throws IOException{
        getChecker(NS+"degem.18","dd").atTheBeginning(true, "l");
    }

    /**
     * -ll- > -
     */
    @Test
    void testDegem19() throws IOException{
        getChecker(NS+"degem.19","").betweenVowels(true, "ll");
    }

    /**
     * -ḍḍ- > -
     */
    @Test
    void testDegem20() throws IOException{
        getChecker(NS+"degem.20","").betweenVowels(true, "ḍḍ");
    }

    /**
     * ḍḍr > dr
     */
    @Test
    void testDegem22() throws IOException{
        getChecker(NS+"degem.22","dr").replacing("ḍḍr");
    }

    /**
     * ṭṭṛ > tr
     */
    @Test
    void testDegem23() throws IOException{
        getChecker(NS+"degem.23","tr").replacing("ṭṭṛ");
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
        getChecker(NS+"assib.1.a","sge").replacing("si");
    }

    /**
     * si > sgë
     * @throws IOException missing phenomenon
     */
    @Test
    void testAssib1b() throws IOException{
        getChecker(NS+"assib.1.b","sgë").replacing("si");
    }

    /**
     * si > sgi
     * @throws IOException missing phenomenon
     */
    @Test
    void testAssib1c() throws IOException{
        getChecker(NS+"assib.1.c","sgi").replacing("si");
    }

    /**
     * sì > sgì
     */
    @Test
    void testAssib2() throws IOException{
        getChecker(NS+"assib.2","sgì").replacing("sì");
    }

    /**
     * sï > sgï
     */
    @Test
    void testAssib3() throws IOException{
        getChecker(NS+"assib.3","sgï")
                .replacing("sï");
    }

    /**
     * ce > sge
     */
    @Test
    void testAssib4() throws IOException{
        getChecker(NS+"assib.4","sge")
                .replacing("ce");
    }

    /**
     * cè > sgè
     */
    @Test
    void testAssib5() throws IOException{
        getChecker(NS+"assib.5","sgè")
                .replacing("cè");
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
        getChecker(NS+"assib.6.a","sge")
                .replacing("ci");
    }

    /**
     * ci > sgë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib6b() throws IOException{
        getChecker(NS+"assib.6.b","sgë").atTheEnd(true,"ci");
    }

    /**
     * ci > sgë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib6c() throws IOException{
        getChecker(NS+"assib.6.c", "sgi")
                .replacing("ci");
    }

    /**
     * cì > sgi
     * @throws IOException on missing phenomenon
     */
    @Test
    void testAssib7() throws IOException{
        getChecker(NS+"assib.7","sgì")
                .replacing("cì");
    }

    /**
     * cï >  sgï
     */
    @Test
    void testAssib8() throws IOException{
        getChecker(NS+"assib.7","sgì")
                .replacing("cì");
    }

    /**
     * nce / ncè / nci / ncì / ncï > nze / nzè / nzi / nzì / nzï
     */
    @Test
    void testAssib9() throws IOException{
        getChecker(NS+"assib.9")
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
        getChecker(NS+"assib.10")
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
        getChecker(NS+"assib.11")
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
        getChecker(NS+"assib.12")
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
        getChecker(NS + "assib.13")
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
        getChecker(NS+"assib.14")
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
        getChecker(NS+"assib.15").inside("ii","ẕẕ");
    }

    /**
     * -ìiri> ẕẕö
     */
    @Test
    void testAssib16() throws IOException{
        getChecker(NS+"assib.16").atTheEnd("ìiri","ẕẕö");
    }

    /**
     * ggi > i
     */
    @Test
    void testAssib17() throws IOException{
        getChecker(NS+"assib.17").replacing("ggi", "i");
    }

    // Dissimilazione dei nessi MB e ND

    /**
     * mm > mb
     */
    @Test
    void testDissim1() throws IOException{
        getChecker(NS+"dissim.1","mb")
                .replacing("mm");
    }

    /**
     * nn > nd
     */
    @Test
    void testDissim2() throws IOException{
        getChecker(NS+"dissim.2").replacing("nn", "nd");
    }

    // Dittongazione non metafonetica

    /**
     * è- > iè-
     */
    @Test
    void testDitt1a() throws IOException{
        getChecker(NS+"ditt.1.a")
                .atTheBeginning("è", "iè");
    }

    /**
     * -è- > iè
     */
    @Test
    void testDitt1b() throws IOException{
        final RegexLinguisticPhenomenonCheckerFactory c=getChecker(NS+"ditt.1.b")
                .notApply("è456")
                .notApply("iè456")
                .notApply("123iè456")
                .notApply("123iè");
        c.build("iè").inside(false, "è").atTheEnd(false, "è");
    }

    /**
     * ò- > uò-
     */
    @Test
    void testDitt2a() throws IOException{
        getChecker(NS+"ditt.2.a")
                .atTheBeginning("ò", "uò");
    }

    /**
     * -ò- > uò
     */
    @Test
    void testDitt2b() throws IOException{
        final RegexLinguisticPhenomenonCheckerFactory c=getChecker(NS+"ditt.2.b")
                .notApply("ò456")
                .notApply("uò456")
                .notApply("123uò456")
                .notApply("123uò");
        c.build("uò").inside(false, "ò").atTheEnd(false, "ò");
    }

    // Mantenimento del sistema vocalico settentrionale

    /**
     * -i > ë
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal2a() throws IOException{
        getChecker(NS+"vocal.2.a", "ë").atTheEnd(true, "i");
    }

    /**
     * ì > ë̀
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal2c() throws IOException{
        getChecker(NS+"vocal.2.c",  "ë̀").replacing("ì");
    }

    /**
     * u > ö
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal5a() throws IOException{
        getChecker(NS+"vocal.5.a", "ö").replacing("u");
    }

    /**
     * ù > ö̀
     * @throws IOException on missing phenomenon
     */
    @Test
    void testVocal5c() throws IOException{
        getChecker(NS+"vocal.5.c", "ö̀").replacing("ù");
    }

    /**
     * -i > e here - is a placeholder for any char.
     */
    @Test
    void testVocal7a() throws IOException {
        getChecker(NS+"vocal.7.a").atTheEnd("i", "e");
    }

    /**
     * i- | -i- > e except where i is followed by a vowel. Here - stands for any char.
     */
    @Test
    void testVocal7b() throws IOException{

        final RegexLinguisticPhenomenonChecker checker=getChecker(NS+"vocal.7.b","e")
                .atTheBeginning(false, "i").inside(false, "i");

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
        getChecker(NS+"vocal.8.a","e")
                .notApply("cu456")
                .notApply("123cu456")
                .notApply("123cu")
                .notApply("gu456")
                .notApply("123gu456")
                .notApply("gu456")
                .replacing("u");
    }

    /**
     * cu > che
     */
    @Test
    void testVocal8b() throws IOException{
        getChecker(NS+"vocal.8.b","che")
                .replacing("cu");
    }

    /**
     * gu > ghe
     */
    @Test
    void testVocal8c() throws IOException{
        getChecker(NS+"vocal.8.c","ghe")
                .replacing("gu");
    }

    /**
     * -èri > -èrö
     */
    @Test
    void testVocal9a() throws IOException{
        getChecker(NS+"vocal.9.a").atTheEnd("èri", "èrö");
    }

    /**
     * àli > àö
     * @throws IOException if vocal.9.b does not exist
     */
    @Test
    void testVocal9b() throws IOException{
        getChecker(NS+"vocal.9.b").atTheEnd("àli", "àö");
    }

    /**
     * ì > ë̀
     * @throws IOException if vocal.10 does not exist
     */
    @Test
    void testVocal10() throws IOException{
        getChecker(NS+"vocal.10").replacing("ì", "ë̀");
    }
    // Aferesi di a-

    /**
     * a- > -
     */
    @Test
    void testAfer1() throws IOException{
        getChecker(NS+"afer.1","")
                .atTheBeginning(true, "a");
    }

    // Palatalizzazione di /a/
    /**
     * -càri > chè
     */
    @Test
    void testPalat1() throws IOException{
        getChecker(NS+"palat.1").atTheEnd("càri", "chè");
    }

    /**
     * -càrisi > chèssë
     */
    @Test
    void testPalat2() throws IOException{
        getChecker(NS+"palat.2").atTheEnd("càrisi",  "chèssë");
    }

    /**
     * -gàri > ghè
     */
    @Test
    void testPalat3() throws IOException{
        getChecker(NS+"palat.3").atTheEnd("gàri", "ghè");
    }

    /**
     * -gàrisi > ghèssë
     */
    @Test
    void testPalat4() throws IOException{
        getChecker(NS+"palat.4").atTheEnd("gàrisi", "ghèssë");
    }

    /**
     * -àri > è if not preceded by c, g, i
     */
    @Test
    void testPalat5() throws IOException{
        getChecker(NS+"palat.5").atTheEnd("àri", "è")
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
        getChecker(NS+"palat.6")
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
        getChecker(NS+"palat.7").atTheEnd("ciàri", "cè");
    }

    /**
     * -iàrisi > èssë
     */
    @Test
    void testPalat8() throws IOException{
        getChecker(NS+"palat.8").atTheEnd("iàrisi", "èssë");
    }

    /**
     * -giàri > gè
     */
    @Test
    void testPalat9() throws IOException{
        getChecker(NS+"palat.9").atTheEnd("giàri", "gè");
    }

    /**
     *  -ïàri > ïè
     */
    @Test
    void testPalat10() throws IOException{
        getChecker(NS+"palat.10").atTheEnd( "ïàri", "ïè");
    }

    /**
     * -ïàrisi > ïèssë
     */
    @Test
    void testPalat11() throws IOException{
        getChecker(NS+"palat.11").atTheEnd("ïàrisi","ïèssë");
    }

    /**
     * -àrini > ènë
     */
    @Test
    void testPalat12() throws IOException{
        getChecker(NS+"palat.12").atTheEnd("àrini","ènë");
    }

    /**
     * -arisìnni > èssenë
     */
    @Test
    void testPalat13() throws IOException{
        getChecker(NS+"palat.13").atTheEnd("arisìnni","èssenë");
    }

    //  Eliminazione di varianti allofoniche siciliane in posizione debole

    /**
     * v- > b-
     */
    @Test
    void testElim1() throws IOException{
        getChecker(NS+"elim.1", "b").atTheBeginning(true, "v");
    }

    /**
     * bb- > b-
     */
    @Test
    void testElim2() throws IOException{
        getChecker(NS+"elim.2", "b").atTheBeginning(true, "bb");
    }

    /**
     * r- > br-
     */
    @Test
    void testElim3() throws IOException{
        getChecker(NS+"elim.3", "br").atTheBeginning(true, "r");
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
        getChecker(NS+"elim.4.a", "g").atTheBeginning(true, "i");
    }

    /**
     * i- > gi-
     * @throws IOException on missing phenomenon
     */
    @Test
    void testElim4b() throws IOException{
        getChecker(NS+"elim.4.b", "gi").atTheBeginning(true, "i");
    }

    /**
     * r- > gr-
     */
    @Test
    void testElim5() throws IOException{
        getChecker(NS+"elim.5", "gr").atTheBeginning(true, "r");
    }

    // Deretroflessione

    /**
     * ṭṛ > tr
     */
    @Test
    void testDeretr1() throws IOException{
        getChecker(NS+"deretr.1", "tr").replacing("ṭṛ");
    }

    /**
     * ṭṛ > tr
     */
    @Test
    void testDeretr1Trazzera() throws IOException{
        final LinguisticPhenomenon deretr1=getFeature(NS+"deretr.1");
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
        getChecker(NS+"deretr.2", "ttr").replacing("ṭṭṛ");
    }

    /**
     * ṣṭṛ > str
     * was f38
     */
    @Test
    void testDeretr3() throws IOException{
        getChecker(NS+"deretr.3", "str").replacing("ṣṭṛ");
    }

    // Desinenze infiniti (=INF)
    /**
     * -ìri > -ë̀
     */
    @Test
    void testInf1() throws IOException{
        getChecker(NS+"inf.1").atTheEnd("ìri", "ë̀");
    }

    /**
     * -ìri > ì
     */
    @Test
    void testInf2() throws IOException{
        getChecker(NS+"inf.2").atTheEnd("ìri","ì");
    }

    /**
     * -iri > ö only if iri is not preceeded by one of the followings: c,g,j,h
     */
    @Test
    void testInf3a() throws IOException{
        getChecker(NS+"inf.3.a").atTheEnd("iri","ö")
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
        getChecker(NS+"inf.3.b.1")
                .atTheEnd("ciri", "ciö");
    }

    /**
     *  -giri > giö
     */
    @Test
    void testInf3b2() throws IOException{
        getChecker(NS+"inf.3.b.2")
                .atTheEnd("giri","giö");
    }

    /**
     *  -chjiri > chjiö
     */
    @Test
    void testInf3b3() throws IOException{
        getChecker(NS+"inf.3.b.3")
                .atTheEnd("chjiri", "chjiö");
    }

    /**
     *  -ghjiri > ghjiö
     */
    @Test
    void testInf3b4() throws IOException{
        getChecker(NS+"inf.3.b.4")
                .atTheEnd("ghjiri", "ghjiö");
    }

    /**
     * -jiri > jö
     */
    @Test
    void testInf3c() throws IOException{
        getChecker(NS+"inf.3.c")
                .atTheEnd("jiri","jö");
    }

    /**
     * -hiri > hjö
     */
    @Test
    void testInf3d() throws IOException{
        getChecker(NS+"inf.3.d")
                .atTheEnd("hiri","hjö");
    }

    /**
     * -ìri > ìsciö
     */
    @Test
    void testInf4() throws IOException{
        getChecker(NS+"inf.4").atTheEnd("ìri","ìsciö");
    }

    /**
     * -ìrisi > -essë provided that ìrisi is not preceded by SC
     */
    @Test
    void testInf5() throws IOException{
        getChecker(NS+"inf.5")
                .atTheEnd("ìrisi", "essë")
                .notApply("123scìrisi");
    }

    /**
     * -scìrisi > essë
     */
    @Test
    void testInf6() throws IOException{
        getChecker(NS+"inf.6").atTheEnd("scìrisi","essë");
    }

    /**
     * -ìricci > ë̀ghjë
     */
    @Test
    void testInf7() throws IOException{
        getChecker(NS+"inf.7").atTheEnd("ìricci","ë̀ghjë");
    }
}
