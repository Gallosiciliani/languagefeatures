package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
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

    /**
     * Get a helper to test the feature with the IRI.
     * @param featureIRI final feature code
     * @return helper to test the specified feature
     */
    private RegexLinguisticPhenomenonChecker getChecker(final String featureIRI, final String...replacement) throws IOException {
        return new RegexLinguisticPhenomenonChecker(getFeature(featureIRI), replacement);
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
     */
    @Test
    void testLeniz1() throws IOException {
        getChecker(NS+"leniz.1","g").betweenVowels(true, "c");
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
     * -uni > -ö̀n
     */
    @Test
    void testLeniz14() throws IOException{
        getChecker(NS+"leniz.14","ö̀n").atTheEnd(true, "uni");
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
     * -spr- / spr- > -sbr- / sbr-
     */
    @Test
    void testLeniz20() throws IOException{
        getChecker(NS+"leniz.20","sbr").betweenVowels(false, "spr").atTheBeginning(false, "spr");
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
        getChecker(NS+"degem.2","t").betweenVowels(true, "tt");
    }

    /**
     * -dd- > -d-
     */
    @Test
    void testDegem3() throws IOException{
        getChecker(NS+"degem.3","d").betweenVowels(true, "dd");
    }

    /**
     * -ḍḍ- > -dd-
     */
    @Test
    void testDegem4() throws IOException {
        getChecker(NS+"degem.4","dd").betweenVowels(true, "ḍḍ");
    }

    /**
     * -pp- > -p-
     */
    @Test
    void testDegem5() throws IOException{
        getChecker(NS+"degem.5","p").betweenVowels(true, "pp");
    }

    /**
     * -bb- > -b-
     */
    @Test
    void testDegem6() throws IOException{
        getChecker(NS+"degem.6","b").betweenVowels(true, "bb");
    }

    /**
     * -cc- > -c-
     * was f25
     */
    @Test
    void testDegem7() throws IOException{
        getChecker(NS+"degem.7","c").betweenVowels(true, "cc");
    }

    /**
     * -cca- > -chè
     */
    @Test
    @Disabled
    void testDegem8() {
        fail("-cca- > -chè, verificare se si applica solo alla fine della parola");
        //getChecker(NS+"f26","chè").betweenVowels("cc");
    }

    /**
     * -gg- > -g- / gh
     * was f27
     */
    @Test
    void testDegem9() throws IOException{
        getChecker(NS+"degem.9","g","gh").betweenVowels(true, "gg");
    }

    /**
     * -gga- > -ghè
     */
    @Test
    @Disabled
    void testDegem10() {
        fail("-gga- > -ghè");
    }

    /**
     * -cchi- / -cchì- / -cchï- / -cchj- > -chj- / -chï-
     * was f29
     */
    @Test
    void testDegem11() throws IOException{
        getChecker(NS+"degem.11","chj","chï")
                .betweenVowels(true, "cchi","cchì","cchï","cchj");
    }


    /**
     * -gghi- / -gghì / -gghï- / -gghj- > -ghj- / -ghï-
     * was f30
     */
    @Test
    void testDegem12() throws IOException{
        getChecker(NS+"degem.12","ghj","ghï")
                .betweenVowels(true, "gghi","gghì","gghï","gghj");
    }

    /**
     * -ff- > f-
     */
    @Test
    @Disabled
    void testDegem13() throws IOException{
        fail("-ff- > f-");
    }

    /**
     * -vv- > v-
     */
    @Test
    @Disabled
    void testDegem14() throws IOException{
        fail("-vv- > v-");
    }

    /**
     * -ss- > -s-
     * was f33
     */
    @Test
    void testDegem15() throws IOException{
        getChecker(NS+"degem.15","s").betweenVowels(true, "ss");
    }

    /**
     * żż > ẕẕ
     * was f34
     */
    @Test
    void testDegem17() throws IOException{
        getChecker(NS+"degem.17","ẕẕ").replacing("żż");
    }

    /**
     * -cci- / -ccï-  > -ci- / -cï-
     */
    @Test
    void testDegem18() throws IOException{
        getChecker(NS+"degem.18","ci","cï").betweenVowels(true, "cci","ccï");
    }

    /**
     * -cce- > -ce-
     */
    @Test
    void testDegem19() throws IOException{
        getChecker(NS+"degem.19","ce").betweenVowels(true, "cce");
    }

    /**
     * -ggi- / -ggì / -ggï- > -gi- / -gì / -gï-
     * was f37
     */
    @Test
    void testDegem20() throws IOException{
        getChecker(NS+"degem.20","gi","gì","gï")
                .betweenVowels(true, "ggi","ggì","ggï");
    }

    /**
     * -gge- /-ggè- > -ge- / -gè-
     * was f38
     */
    @Test
    void testDegem21() throws IOException{
        getChecker(NS+"degem.21","ge","gè")
                .betweenVowels(true, "gge","ggè");
    }

    /**
     * mm > m
     * was f39
     */
    @Test
    void testDegem23() throws IOException{
        getChecker(NS+"degem.23","m").replacing("mm");
    }

    /**
     * nn > n
     */
    @Test
    void testDegem24() throws IOException{
        getChecker(NS+"degem.24","n").replacing("nn");
    }

    /**
     * l- > dd-
     */
    @Test
    void testDegem25() throws IOException{
        getChecker(NS+"degem.25","dd").atTheBeginning(true, "l");
    }

    /**
     * -ll- > -
     */
    @Test
    void testDegem26() throws IOException{
        getChecker(NS+"degem.26","").betweenVowels(true, "ll");
    }

    /**
     * -ḍḍ- > -
     */
    @Test
    void testDegem27() throws IOException{
        getChecker(NS+"degem.27","").betweenVowels(true, "ḍḍ");
    }

    /**
     * -ḍḍr- > -dr-
     */
    @Test
    void testDegem29() throws IOException{
        getChecker(NS+"degem.29","dr").betweenVowels(true, "ḍḍr");
    }

    /**
     * -ṭṭṛ- > -tr-
     */
    @Test
    void testDegem30() throws IOException{
        getChecker(NS+"degem.30","tr").betweenVowels(true, "ṭṭṛ");
    }

    //ASSIBILAZIONE

    /**
     * -si- / -sì- / -sï- > sge / sgë / sgi / sgì / sgï
     */
    @Test
    void testAssib1() throws IOException{
        getChecker(NS+"assib.1","sge","sgë","sgi","sgì","sgï").betweenVowels(true, "si","sì","sï");
    }

    /**
     * -ce- / -cè- / -ci- / -cì- / -cï- > sge / sgè / sgë / sgi / sgì / sgï
     */
    @Test
    void testAssib2() throws IOException{
        getChecker(NS+"assib.2","sge","sgè","sgë","sgi","sgì","sgï")
                .betweenVowels(true, "ce","cè","ci","cì","cï");
    }

    /**
     * -si / -sï > sge / sgë / sgi / sgï
     */
    @Test
    void testAssib3() throws IOException{
        getChecker(NS+"assib.3","sge","sgë","sgi","sgï")
                .atTheEnd(true, "si","sï");
    }

    /**
     * -ce / -ci / -cï > sge / sgë / sgi / sgï
     */
    @Test
    void testAssib4() throws IOException{
        getChecker(NS+"assib.4","sge","sgë","sgi","sgï")
                .atTheEnd(true, "ce","ci","cï");
    }

    /**
     * nce / ncè / nci / ncì / ncï > nze / nzè / nzi / nzì / nzï
     */
    @Test
    void testAssib5() throws IOException{
        getChecker(NS+"assib.5","nze","nzè","nzi","nzì","nzï")
                .replacing("nce","ncè","nci","ncì","ncï");
    }

    /**
     * nge / ngè / ngi / ngì / ngï >  nẕe / nẕè / nẕi / nẕì / nẕï
     */
    @Test
    void testAssib6() throws IOException{
        getChecker(NS+"assib.6","nẕe","nẕè","nẕi","nẕì","nẕï")
                .replacing("nge","ngè","ngi","ngì","ngï");
    }

    /**
     * -rce- / -rcè / -rci-/ -rcì- / -rcï- > -rze- / -rzè- / -rzi- / -rzì- / -rzï-
     */
    @Test
    void testAssib7() throws IOException{
        getChecker(NS+"assib.7","rze","rzè","rzi","rzì","rzï")
                .betweenVowels(true, "rce","rcè","rci","rcì","rcï");
    }

    /**
     * ce- / cè- / ci- / cì- / cï- > zze- / zzè- /zzi- / zzì- / zzï-
     */
    @Test
    void testAssib8() throws IOException{
        getChecker(NS+"assib.8","zze","zzè","zzi","zzì","zzï")
                .atTheBeginning(true, "ce","cè","ci","cì","cï");
    }

    /**
     * ge- / gè- / gi- / gì- / gï- > zze- / zze- / ẕẕi- / ẕẕì- / zzï-
     */
    @Test
    void testAssib9() throws IOException{
        getChecker(NS+"assib.9","zze","zze","ẕẕi","ẕẕì","zzï")
                .atTheBeginning(true, "ge","gè","gi","gì","gï");
    }

    /**
     * se- / sè- / si- / sì- > zze- /zzè- / zzi- / zzì-
     */
    @Test
    void testAssib10() throws IOException{
        getChecker(NS+"assib.10","zze","zzè","zzi","zzì")
                .atTheBeginning(true, "se","sè","si","sì");
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
        getChecker(NS+"ditt.1","ie","iè")
                .replacing("e","è");
    }

    /**
     * o / ò > uo / uò
     */
    @Test
    void testDitt2() throws IOException{
        getChecker(NS+"ditt.2","uo","uò")
                .replacing("o","ò");
    }

    // Mantenimento del sistema vocalico settentrionale

    /**
     * i / ì > ë  / ë̀
     */
    @Test
    void testVocal2() throws IOException{
        getChecker(NS+"vocal.2","ë","ë̀")
                .replacing("i","ì");
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
     * u / ù > ö / ö̀
     */
    @Test
    void testVocal5() throws IOException{
        getChecker(NS+"vocal.5","ö","ö̀")
                .replacing("u","ù");
        getChecker(NS+"vocal.5","ö","ö̀")
                .derives("buffeta", new TreeSet<>(Set.of("böffeta", "bö̀ffeta")));
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
        final RegexLinguisticPhenomenonChecker c = getChecker(NS+"vocal.9", "ö");
        c.notApply("i456");
        c.notApply("123i456");
        for(final char v: RegexLinguisticPhenomenonChecker.VOWELS.toCharArray()){
            c.notApply("123"+v+"i");
        }
        final SortedSet<String> expected=new TreeSet<>();
        expected.add("123ö");
        c.derives("123i", expected);
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
     * -cari- > -chè
     */
    @Test
    @Disabled
    void testPalat1() throws IOException{
        fail("-cari- > -chè");
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
     * -ari > -è
     */
    @Test
    void testPalat5() throws IOException{
        getChecker(NS+"palat.5", "è").atTheEnd(true, "ari");
    }

    /**
     * -àrisi > -essë
     */
    @Test
    void testPalat6() throws IOException{
        getChecker(NS+"palat.6", "essë").atTheEnd(true, "àrisi");
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
     */
    @Test
    void testF82() throws IOException{
        getChecker(NS+"deretr.2", "ttr").replacing("ṭṭṛ");
    }

    /**
     * ṣṭṛ > str
     */
    @Test
    void testF83() throws IOException{
        getChecker(NS+"deretr.3", "str").replacing("ṣṭṛ");
    }
}
