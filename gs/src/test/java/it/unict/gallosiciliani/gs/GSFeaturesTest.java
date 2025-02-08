package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaConflictsDetector;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
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
        try(final GSFeatures gs = GSFeatures.loadLocal()) {
            for(final LinguisticPhenomenon p : gs.getRegexLinguisticPhenomena())
                assertNotNull(LABEL_PROVIDER_ID.getLabel(p, locale), "label for " + p + " not found");
        }
    }

    @Test
    void checkDuplicateRegex() throws IOException {
        try(final GSFeatures gs = GSFeatures.loadLocal()) {
            final RegexLinguisticPhenomenaReader reader = RegexLinguisticPhenomenaReader.read(gs.getModel());
            assertTrue(reader.getExceptions().isEmpty());
        }
    }

    @Test
    @Disabled
    void checkRegexConflicts() throws IOException {
        try(final GSFeatures gs = GSFeatures.loadLocal()) {
            final RegexLinguisticPhenomenaConflictsDetector d = new RegexLinguisticPhenomenaConflictsDetector();
            gs.getRegexLinguisticPhenomena().forEach(d);
            for(final Map.Entry<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> conflict: d.getConflicts().entrySet()){
                final RegexLinguisticPhenomenon p=conflict.getKey();
                for(final RegexLinguisticPhenomenon q: conflict.getValue())
                    System.out.println("Detected conflict "+p.getIRI()+" "+q.getIRI());
            }
            assertTrue(d.getConflicts().isEmpty());
        }
    }

    /**
     * Get a helper to test the feature with the IRI.
     * @param featureIRI final feature code
     * @return helper to test the specified feature
     */
    private RegexLinguisticPhenomenonChecker getChecker(final String featureIRI, final String...replacement) throws IOException {
        return getChecker(featureIRI).build(replacement);
    }

    private RegexLinguisticPhenomenonCheckerFactory getChecker(final String featureIRI) throws IOException {
        return new RegexLinguisticPhenomenonCheckerFactory(getFeature(featureIRI));
    }

    /**
     * Test that the given phenomenon replace the occurrences of src with replcamente
     * @param featureIRI final feature code
     * @param src the string to be replaced
     * @param replacement the replacement string
     */
    private void checkReplacing(final String featureIRI, final String src, final String replacement) throws IOException {
        getChecker(featureIRI, replacement).replacing(src);
    }

    /**
     * Get the feature with the specified IRI
     * @param iri phenomenon IRI
     * @return the phenomenon with the specified IRI
     * @throws IllegalArgumentException if no such phenomenon exists among GS features
     */
    private LinguisticPhenomenon getFeature(final String iri) throws IOException {
        try(final GSFeatures ont = GSFeatures.loadLocal()) {
            final RegexFeatureQuery q=new RegexFeatureQuery().ignoreDeprecated();
            final List<RegexLinguisticPhenomenon> allRegexFeatures = RegexLinguisticPhenomenaReader.read(ont.getModel(), q).getFeatures();
            assertFalse(allRegexFeatures.isEmpty());
            for (final LinguisticPhenomenon f : allRegexFeatures)
                if (iri.equals(f.getIRI()))
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
     * -itu > -ì
     */
    @Test
    void testLeniz6() throws IOException{
        getChecker(NS+"leniz.6","ì").atTheEnd(true, "itu");
    }

    /**
     * -atu > -à
     */
    @Test
    void testLeniz7() throws IOException{
        getChecker(NS+"leniz.7","à").atTheEnd(true, "atu");
    }

    /**
     * -utu > -ù
     */
    @Test
    void testLeniz8() throws IOException{
        getChecker(NS+"leniz.8","ù").atTheEnd(true, "utu");
    }

    /**
     * -iti > - ë̀
     */
    @Test
    void testLeniz9() throws IOException{
        getChecker(NS+"leniz.9","ë̀").atTheEnd(true, "iti");
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
     * -ani > -an
     */
    @Test
    void testLeniz13() throws IOException{
        getChecker(NS+"leniz.13","an").atTheEnd(true, "ani");
    }

    /**
     * -uni > -ön
     */
    @Test
    void testLeniz14() throws IOException{
        getChecker(NS+"leniz.14","ön").atTheEnd(true, "uni");
    }

    /**
     * -eni > -en
     */
    @Test
    void testLeniz15() throws IOException{
        getChecker(NS+"leniz.15","en").atTheEnd(true, "eni");
    }

    /**
     * -inu > -in
     */
    @Test
    void testLeniz16() throws IOException{
        getChecker(NS+"leniz.16","in").atTheEnd(true, "inu");
    }

    /**
     * -unu > -un
     */
    @Test
    void testLeniz17() throws IOException{
        getChecker(NS+"leniz.17","un").atTheEnd(true, "unu");
    }

    // leniz.19 -s- / s- > -s- / s-

    /**
     * -chi- / -chì- / -chj- / -chï- > -ghj- / -ghï-
     */
    @Test
    void testLeniz19() throws IOException{
        getChecker(NS+"leniz.19","ghj","ghï").betweenVowels(true, "chi","chì","chj","chï");
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
        getChecker(NS+"degem.6","b").replacing("bb");
    }

    /**
     * cc > c
     * was f25
     */
    @Test
    void testDegem7() throws IOException{
        getChecker(NS+"degem.7","c").replacing("cc");
    }

    /**
     * -ccari > -chè
     */
    @Test
    void testDegem8() throws IOException {
        getChecker(NS+"degem.8", "chè").atTheEnd(true, "ccari");
    }

    /**
     * gg > g but not in -ggari
     * was f27
     */
    @Test
    void testDegem9() throws IOException{
        getChecker(NS+"degem.9","g").atTheBeginning(false, "gg").inside(false, "gg")
                .notApply("123ggari");
    }

    /**
     * -ggari > -ghè
     */
    @Test
    void testDegem10() throws IOException {
        getChecker(NS+"degem.10","ghè").atTheEnd(true, "ggari");
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

    /**
     * si >  sge / sgë / sgi
     */
    @Test
    void testAssib1() throws IOException{
        getChecker(NS+"assib.1","sge","sgë","sgi").replacing("si");
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

    /**
     * ci > sge / sgë / sgi
     */
    @Test
    void testAssib6() throws IOException{
        getChecker(NS+"assib.6","sge", "sgë", "sgi")
                .replacing("ci");
    }

    /**
     * cì > sgì
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
     *
     * ([aàáäeèéëë̀iìíïoòóöö̀uùúü])rc([eiìï].+|è$)
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
        getChecker(NS+"dissim.2","nd")
                .replacing("nn");
    }

    // Dittongazione non metafonetica

    /**
     * e / è  > ie / iè
     */
    @Test
    void testDitt1() throws IOException{
        getChecker(NS+"ditt.1")
                .replacing("e", "ie")
                .replacing("è","iè");
    }

    /**
     * o / ò > uo / uò
     */
    @Test
    void testDitt2() throws IOException{
        getChecker(NS+"ditt.2")
                .replacing("o","uo")
                .replacing("ò", "uò");
    }

    // Mantenimento del sistema vocalico settentrionale

    /**
     * i > ë
     */
    @Test
    void testVocal2() throws IOException{
        getChecker(NS+"vocal.2", "ë").replacing("i");
    }

    /**
     * ì > ë | é (= ë̀)
     */
    @Test
    void testVocal2Bis() throws IOException{
        getChecker(NS+"vocal.2bis", "ë", "ë̀").replacing("ì");
    }

    /**
     * i / ì > i
     */
    @Test
    void testVocal3() throws IOException{
        getChecker(NS+"vocal.3","i")
                .replacing("ì");
    }

    /**
     * u > ö
     */
    @Test
    void testVocal5() throws IOException{
        getChecker(NS+"vocal.5", "ö").replacing("u");
    }

    /**
     * ù > ö | ó  (= ö̀)̀
     */
    @Test
    void testVocal5Bis() throws IOException{
        getChecker(NS+"vocal.5bis", "ö","ö̀").replacing("ù");
    }

    /**
     * ù > u
     */
    @Test
    void testVocal6() throws IOException{
        getChecker(NS+"vocal.6","u")
                .replacing("ù");
    }

    /**
     * i > e
     */
    @Test
    void testVocal7() throws IOException{
        getChecker(NS+"vocal.7","e")
                .replacing("i");
    }

    /**
     * u > e
     */
    @Test
    void testVocal8() throws IOException{
        getChecker(NS+"vocal.8","e")
                .replacing("u");
    }

    /**
     * -i > -ö
     */
    @Test
    void testVocal9() throws IOException{
        getChecker(NS+"vocal.9", "ö")
                .atTheEnd(true, "i");
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
     * -cari > -chè
     */
    @Test
    void testPalat1() throws IOException{
        getChecker(NS+"palat.1", "chè").atTheEnd(true, "cari");
    }

    /**
     * -càrisi > -chessë
     */
    @Test
    void testPalat2() throws IOException{
        getChecker(NS+"palat.2", "chessë").atTheEnd(true, "càrisi");
    }

    /**
     * -gari > -ghè
     */
    @Test
    void testPalat3() throws IOException{
        getChecker(NS+"palat.3", "ghè").atTheEnd(true, "gari");
    }

    /**
     * -gàrisi > -ghessë
     */
    @Test
    void testPalat4() throws IOException{
        getChecker(NS+"palat.4", "ghessë").atTheEnd(true, "gàrisi");
    }

    /**
     * -ari > -è if not preceded by c, g, i
     */
    @Test
    void testPalat5() throws IOException{
        getChecker(NS+"palat.5", "è").atTheEnd(true, "ari")
                .notApply("123cari")
                .notApply("123gari")
                .notApply("123iari");
    }

    /**
     * -àrisi > -essë if not preceded by c, g, i
     */
    @Test
    void testPalat6() throws IOException{
        getChecker(NS+"palat.6", "essë")
                .atTheEnd(true, "àrisi")
                .notApply("123càrisi")
                .notApply("123gàrisi")
                .notApply("123iàrisi");
    }

    /**
     * -iari > -è
     */
    @Test
    void testPalat7() throws IOException{
        getChecker(NS+"palat.7", "è").atTheEnd(true, "iari");
    }

    /**
     * -iàrisi > -essë
     */
    @Test
    void testPalat8() throws IOException{
        getChecker(NS+"palat.8", "essë").atTheEnd(true, "iàrisi");
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

    /**
     * i- > g- /gi-
     */
    @Test
    void testElim4() throws IOException{
        getChecker(NS+"elim.4", "g","gi").atTheBeginning(true, "i");
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
}
