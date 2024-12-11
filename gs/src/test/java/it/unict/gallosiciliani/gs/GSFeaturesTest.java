package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import lombok.extern.slf4j.Slf4j;
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
                assertNotNull(gs.getLabel(p, locale), "label for " + p + " not found");
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
        try(final GSFeatures ont = GSFeatures.loadLocal()) {
            final RegexFeatureQuery q=new RegexFeatureQuery().ignoreDeprecated();
            final List<RegexLinguisticPhenomenon> allRegexFeatures = RegexLinguisticPhenomenaReader.read(ont.getModel(), q).getFeatures();
            assertFalse(allRegexFeatures.isEmpty());
            for (final LinguisticPhenomenon f : allRegexFeatures)
                if (featureIRI.equals(f.getIRI()))
                    return new RegexLinguisticPhenomenonChecker(f, replacement);
            throw new IllegalArgumentException("Unable to get feature " + featureIRI);
        }
    }

    /**
     * f1 -c-  > -g-
     */
    @Test
    void testF1() throws IOException {
        getChecker(NS+"f1","g").betweenVowels(true, "c");
    }

    /**
     * f2 -cr- > -gr-
     */
    @Test
    void testF2() throws IOException{
        getChecker(NS+"f2","gr").betweenVowels(true, "cr");

    }

    /**
     * -p-  > -v-
     */
    @Test
    void testF3() throws IOException{
        getChecker(NS+"f3","v").betweenVowels(true, "p");
    }

    /**
     * -pr- > -vr-
     */
    @Test
    void testF4() throws IOException{
        getChecker(NS+"f4","vr").betweenVowels(true, "pr");
    }

    /**
     * -t- > -d-
     */
    @Test
    void testF5() throws IOException{
        getChecker(NS+"f5","d").betweenVowels(true, "t");
    }

    /**
     * -itu > -ì
     */
    @Test
    void testF6() throws IOException{
        getChecker(NS+"f6","ì").atTheEnd(true, "itu");
    }

    /**
     * -atu > -à
     */
    @Test
    void testF7() throws IOException{
        getChecker(NS+"f7","à").atTheEnd(true, "atu");
    }

    /**
     * -utu > -ù
     */
    @Test
    void testF8() throws IOException{
        getChecker(NS+"f8","ù").atTheEnd(true, "utu");
    }

    /**
     * -iti > - ë̀
     */
    @Test
    void testF9() throws IOException{
        getChecker(NS+"f9","ë̀").atTheEnd(true, "iti");
    }

    /**
     * -ṭṛ- > -ir-
     */
    @Test
    void testF10() throws IOException{
        getChecker(NS+"f10", "ir").betweenVowels(true, "ṭṛ");
    }

    /**
     * -dr- > -ir-
     */
    @Test
    void testF11() throws IOException{
        getChecker(NS+"f11", "ir").betweenVowels(true, "dr");
    }

    /**
     * -l- > --
     */
    @Test
    void testF12() throws IOException{
        getChecker(NS+"f12","").betweenVowels(true, "l");
    }

    /**
     * -ani > -an
     */
    @Test
    void testF13() throws IOException{
        getChecker(NS+"f13","an").atTheEnd(true, "ani");
    }

    /**
     * -uni > -ö̀n
     */
    @Test
    void testF14() throws IOException{
        getChecker(NS+"f14","ö̀n").atTheEnd(true, "uni");
    }

    /**
     * -eni > -en
     */
    @Test
    void testF15() throws IOException{
        getChecker(NS+"f15","en").atTheEnd(true, "eni");
    }

    /**
     * -inu > -in
     */
    @Test
    void testF16() throws IOException{
        getChecker(NS+"f16","in").atTheEnd(true, "inu");
    }

    /**
     * -unu > -un
     */
    @Test
    void testF17() throws IOException{
        getChecker(NS+"f17","un").atTheEnd(true, "unu");
    }

    /**
     * -chi- / -chì- / -chj- / -chï- > -ghj- / -ghï-
     */
    @Test
    void testF18() throws IOException{
        getChecker(NS+"f18","ghj","ghï").betweenVowels(true, "chi","chì","chj","chï");
    }

    /**
     * -spr- / spr- > -sbr- / sbr-
     */
    @Test
    void testF19() throws IOException{
        getChecker(NS+"f19","sbr").betweenVowels(false, "spr").atTheBeginning(false, "spr");
    }

    /**
     * -tt- > -it-
     */
    @Test
    void testF20() throws IOException{
        getChecker(NS+"f20","it").betweenVowels(true, "tt");
    }

    /**
     * -tt- > -t-
     */
    @Test
    void testF21() throws IOException{
        getChecker(NS+"f21","t").betweenVowels(true, "tt");
    }

    /**
     * -dd- > -d-
     */
    @Test
    void testF22() throws IOException{
        getChecker(NS+"f22","d").betweenVowels(true, "dd");
    }

    /**
     * -pp- > -p-
     */
    @Test
    void testF23() throws IOException{
        getChecker(NS+"f23","p").betweenVowels(true, "pp");
    }

    /**
     * -bb- > -b-
     */
    @Test
    void testF24() throws IOException{
        getChecker(NS+"f24","b").betweenVowels(true, "bb");
    }

    /**
     * -cc- > -c-
     */
    @Test
    void testF25() throws IOException{
        getChecker(NS+"f25","c").betweenVowels(true, "cc");
    }

    /**
     * -cca- > -chè
     */
    @Test
    void testF26() {
        fail("-cca- > -chè, verificare se si applica solo alla fine della parola");
        //getChecker(NS+"f26","chè").betweenVowels("cc");
    }

    /**
     * -gg- > -g- / gh
     */
    @Test
    void testF27() throws IOException{
        getChecker(NS+"f27","g","gh").betweenVowels(true, "gg");
    }

    /**
     * -gga- > -ghè
     */
    @Test
    void testF28() {
        fail("-gga- > -ghè");
    }

    /**
     * -cchi- / -cchì- / -cchï- / -cchj- > -chj- / -chï-
     */
    @Test
    void testF29() throws IOException{
        getChecker(NS+"f29","chj","chï")
                .betweenVowels(true, "cchi","cchì","cchï","cchj");
    }


    /**
     * -gghi- / -gghì / -gghï- / -gghj- > -ghj- / -ghï-
     */
    @Test
    void testF30() throws IOException{
        getChecker(NS+"f30","ghj","ghï")
                .betweenVowels(true, "gghi","gghì","gghï","gghj");
    }

    /**
     * -ff- > f-
     */
    @Test
    void testF31() throws IOException{
        fail("-ff- > f-");
    }

    /**
     * -vv- > v-
     */
    @Test
    void testF32() throws IOException{
        fail("-vv- > v-");
    }

    /**
     * -ss- > -s-
     */
    @Test
    void testF33() throws IOException{
        getChecker(NS+"f33","s").betweenVowels(true, "ss");
    }

    /**
     * żż > ẕẕ
     */
    @Test
    void testF34() throws IOException{
        getChecker(NS+"f34","ẕẕ").replacing("żż");
    }

    /**
     * -cci- / -ccï-  > -ci- / -cï-
     */
    @Test
    void testF35() throws IOException{
        getChecker(NS+"f35","ci","cï").betweenVowels(true, "cci","ccï");
    }

    /**
     * -cce- > -ce-
     */
    @Test
    void testF36() throws IOException{
        getChecker(NS+"f36","ce").betweenVowels(true, "cce");
    }

    /**
     * -ggi- / -ggì / -ggï- > -gi- / -gì / -gï-
     */
    @Test
    void testF37() throws IOException{
        getChecker(NS+"f37","gi","gì","gï")
                .betweenVowels(true, "ggi","ggì","ggï");
    }

    /**
     * -gge- /-ggè- > -ge- / -gè-
     */
    @Test
    void testF38() throws IOException{
        getChecker(NS+"f38","ge","gè")
                .betweenVowels(true, "gge","ggè");
    }

    /**
     * mm > m
     */
    @Test
    void testF39() throws IOException{
        getChecker(NS+"f39","m").replacing("mm");
    }

    /**
     * nn > n
     */
    @Test
    void testF40() throws IOException{
        getChecker(NS+"f40","n").replacing("nn");
    }

    /**
     * l- > dd-
     */
    @Test
    void testF41() throws IOException{
        getChecker(NS+"f41","dd").atTheBeginning(true, "l");
    }

    /**
     * -ll- > -
     */
    @Test
    void testF42() throws IOException{
        getChecker(NS+"f42","").betweenVowels(true, "ll");
    }

    /**
     * -ḍḍ- > -
     */
    @Test
    void testF43() throws IOException{
        getChecker(NS+"f43","").betweenVowels(true, "ḍḍ");
    }

    /**
     * -ḍḍr- > -dr-
     */
    @Test
    void testF44() throws IOException{
        getChecker(NS+"f44","dr").betweenVowels(true, "ḍḍr");
    }

    /**
     * -ṭṭṛ- > -tr-
     */
    @Test
    void testF45() throws IOException{
        getChecker(NS+"f45","tr").betweenVowels(true, "ṭṭṛ");
    }

    /**
     * -si- / -sì- / -sï- > sge / sgë / sgi / sgì / sgï
     */
    @Test
    void testF46() throws IOException{
        getChecker(NS+"f46","sge","sgë","sgi","sgì","sgï").betweenVowels(true, "si","sì","sï");
    }

    /**
     * -ce- / -cè- / -ci- / -cì- / -cï- > sge / sgè / sgë / sgi / sgì / sgï
     */
    @Test
    void testF47() throws IOException{
        getChecker(NS+"f47","sge","sgè","sgë","sgi","sgì","sgï")
                .betweenVowels(true, "ce","cè","ci","cì","cï");
    }

    /**
     * -si / -sï > sge / sgë / sgi / sgï
     */
    @Test
    void testF48() throws IOException{
        getChecker(NS+"f48","sge","sgë","sgi","sgï")
                .atTheEnd(true, "si","sï");
    }

    /**
     * -ce / -ci / -cï > sge / sgë / sgi / sgï
     */
    @Test
    void testF49() throws IOException{
        getChecker(NS+"f49","sge","sgë","sgi","sgï")
                .atTheEnd(true, "ce","ci","cï");
    }

    /**
     * nce / ncè / nci / ncì / ncï > nze / nzè / nzi / nzì / nzï
     */
    @Test
    void testF50() throws IOException{
        getChecker(NS+"f50","nze","nzè","nzi","nzì","nzï")
                .replacing("nce","ncè","nci","ncì","ncï");
    }

    /**
     * nge / ngè / ngi / ngì / ngï >  nẕe / nẕè / nẕi / nẕì / nẕï
     */
    @Test
    void testF51() throws IOException{
        getChecker(NS+"f51","nẕe","nẕè","nẕi","nẕì","nẕï")
                .replacing("nge","ngè","ngi","ngì","ngï");
    }

    /**
     * -rce- / -rcè / -rci-/ -rcì- / -rcï- > -rze- / -rzè- / -rzi- / -rzì- / -rzï-
     */
    @Test
    void testF52() throws IOException{
        getChecker(NS+"f52","rze","rzè","rzi","rzì","rzï")
                .betweenVowels(true, "rce","rcè","rci","rcì","rcï");
    }

    /**
     * ce- / cè- / ci- / cì- / cï- > zze- / zzè- /zzi- / zzì- / zzï-
     */
    @Test
    void testF53() throws IOException{
        getChecker(NS+"f53","zze","zzè","zzi","zzì","zzï")
                .atTheBeginning(true, "ce","cè","ci","cì","cï");
    }

    /**
     * ge- / gè- / gi- / gì- / gï- > zze- / zze- / ẕẕi- / ẕẕì- / zzï-
     */
    @Test
    void testF54() throws IOException{
        getChecker(NS+"f54","zze","zze","ẕẕi","ẕẕì","zzï")
                .atTheBeginning(true, "ge","gè","gi","gì","gï");
    }

    /**
     * se- / sè- / si- / sì- > zze- /zzè- / zzi- / zzì-
     */
    @Test
    void testF55() throws IOException{
        getChecker(NS+"f55","zze","zzè","zzi","zzì")
                .atTheBeginning(true, "se","sè","si","sì");
    }

    /**
     * mm > mb
     */
    @Test
    void testF56() throws IOException{
        getChecker(NS+"f56","mb")
                .replacing("mm");
    }

    /**
     * nn > nd
     */
    @Test
    void testF57() throws IOException{
        getChecker(NS+"f57","nd")
                .replacing("nn");
    }

    /**
     * e / è  > ie / iè
     */
    @Test
    void testF58() throws IOException{
        getChecker(NS+"f58","ie","iè")
                .replacing("e","è");
    }

    /**
     * o / ò > uo / uò
     */
    @Test
    void testF59() throws IOException{
        getChecker(NS+"f59","uo","uò")
                .replacing("o","ò");
    }

    /**
     * i / ì > ë  / ë̀
     */
    @Test
    void testF60() throws IOException{
        getChecker(NS+"f60","ë","ë̀")
                .replacing("i","ì");
    }

    /**
     * i / ì > i
     */
    @Test
    void testF61() throws IOException{
        getChecker(NS+"f61","i")
                .replacing("ì");
    }

    /**
     * u / ù > ö / ö̀
     */
    @Test
    void testF62() throws IOException{
        getChecker(NS+"f62","ö","ö̀")
                .replacing("u","ù");
    }

    /**
     * ù > u
     */
    @Test
    void testF63() throws IOException{
        getChecker(NS+"f63","u")
                .replacing("ù");
    }

    /**
     * i > e
     */
    @Test
    void testF64() throws IOException{
        getChecker(NS+"f64","e")
                .replacing("i");
    }

    /**
     * u > e
     */
    @Test
    void testF65() throws IOException{
        getChecker(NS+"f65","e")
                .replacing("u");
    }

    /**
     * -i > -ö
     */
    @Test
    void testF66() throws IOException{
        final RegexLinguisticPhenomenonChecker c = getChecker(NS+"f66", "ö");
        c.notApply("i456");
        c.notApply("123i456");
        for(final char v: RegexLinguisticPhenomenonChecker.VOWELS.toCharArray()){
            c.notApply("123"+v+"i");
        }
        final SortedSet<String> expected=new TreeSet<>();
        expected.add("123ö");
        c.derives("123i", expected);
    }

    /**
     * a- > -
     */
    @Test
    void testF67() throws IOException{
        getChecker(NS+"f67","")
                .atTheBeginning(true, "a");
    }

    /**
     * -cari- > -chè
     */
    @Test
    void testF68() throws IOException{
        fail("-cari- > -chè");
    }

    /**
     * -càrisi > -chessë
     */
    @Test
    void testF69() throws IOException{
        getChecker(NS+"f69", "chessë").atTheEnd(true, "càrisi");
    }

    /**
     * -gari > -ghè
     */
    @Test
    void testF70() throws IOException{
        getChecker(NS+"f70", "ghè").atTheEnd(true, "gari");
    }

    /**
     * -gàrisi > -ghessë
     */
    @Test
    void testF71() throws IOException{
        getChecker(NS+"f71", "ghessë").atTheEnd(true, "gàrisi");
    }

    /**
     * -ari > -è
     */
    @Test
    void testF72() throws IOException{
        getChecker(NS+"f72", "è").atTheEnd(true, "ari");
    }

    /**
     * -àrisi > -essë
     */
    @Test
    void testF73() throws IOException{
        getChecker(NS+"f73", "essë").atTheEnd(true, "àrisi");
    }

    /**
     * -iari > -è
     */
    @Test
    void testF74() throws IOException{
        getChecker(NS+"f74", "è").atTheEnd(true, "iari");
    }

    /**
     * -iàrisi > -essë
     */
    @Test
    void testF75() throws IOException{
        getChecker(NS+"f75", "essë").atTheEnd(true, "iàrisi");
    }

    /**
     * v- > b-
     */
    @Test
    void testF76() throws IOException{
        getChecker(NS+"f76", "b").atTheBeginning(true, "v");
    }

    /**
     * bb- > b-
     */
    @Test
    void testF77() throws IOException{
        getChecker(NS+"f77", "b").atTheBeginning(true, "bb");
    }

    /**
     * r- > br-
     */
    @Test
    void testF78() throws IOException{
        getChecker(NS+"f78", "br").atTheBeginning(true, "r");
    }

    /**
     * i- > g- /gi-
     */
    @Test
    void testF79() throws IOException{
        getChecker(NS+"f79", "g","gi").atTheBeginning(true, "i");
    }

    /**
     * r- > gr-
     */
    @Test
    void testF80() throws IOException{
        getChecker(NS+"f80", "gr").atTheBeginning(true, "r");
    }

    /**
     * ṭṛ > tr
     */
    @Test
    void testF81() throws IOException{
        getChecker(NS+"f81", "tr").replacing("ṭṛ");
    }

    /**
     * ṭṭṛ > ttr
     */
    @Test
    void testF82() throws IOException{
        getChecker(NS+"f82", "ttr").replacing("ṭṭṛ");
    }

    /**
     * ṣṭṛ > str
     */
    @Test
    void testF83() throws IOException{
        getChecker(NS+"f83", "str").replacing("ṣṭṛ");
    }
}
