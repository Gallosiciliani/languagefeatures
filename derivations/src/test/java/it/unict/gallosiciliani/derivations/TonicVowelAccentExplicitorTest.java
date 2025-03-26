package it.unict.gallosiciliani.derivations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link TonicVowelAccentExplicitor}
 *
 * @author Cristiano Longo
 */
public class TonicVowelAccentExplicitorTest {
    private final TonicVowelAccentExplicitor e=new TonicVowelAccentExplicitor();

    @Test
    void shouldNotAlterWordsWithAGraveAccent(){
        assertEquals("àbetö", e.addGraveAccent("àbetö")); //galloitalic
        assertEquals("nserraghjè", e.addGraveAccent("nserraghjè"));
        assertEquals("mìserö", e.addGraveAccent("mìserö"));
        assertEquals("cuòsgiö", e.addGraveAccent("cuòsgiö"));
        assertEquals("servetù", e.addGraveAccent("servetù"));

        //invented
        assertEquals("123ä̀456", e.addGraveAccent("123ä̀456"));
        assertEquals("123ë̀456", e.addGraveAccent("123ë̀456"));
        assertEquals("123ï̀456", e.addGraveAccent("123ï̀456"));
        assertEquals("123ö̀456", e.addGraveAccent("123ö̀456"));
        assertEquals("123ǜ456", e.addGraveAccent("123ǜ456"));
    }

    @Test
    void shouldNotAlterWordsWithASingleVowel(){
        assertEquals("abs", e.addGraveAccent("abs"));
    }

    @Test
    void shouldAddGraveAccentToTheSecondToLastVowel(){
        // on Gallo-Italian terms
        assertEquals("àla", e.addGraveAccent("ala"));
        assertEquals("afànö", e.addGraveAccent("afanö"));
        assertEquals("aciprètë", e.addGraveAccent("acipretë"));
        assertEquals("alegrìa", e.addGraveAccent("alegria"));
        assertEquals("abòrtö", e.addGraveAccent("abortö")); //galloitalic
        assertEquals("abùsö", e.addGraveAccent("abusö")); //galloitalic
        assertEquals("acidë̀ntë", e.addGraveAccent("acidëntë")); //galloitalic
        assertEquals("agö̀stö", e.addGraveAccent("agöstö"));

        // da chiedere aghjö agrefuoghjö

        //on Sicilian terms
        // a
        assertEquals("àba", e.addGraveAccent("aba"));
        assertEquals("abbaccarunàtu", e.addGraveAccent("abbaccarunatu"));
        //e
        assertEquals("abballèttu", e.addGraveAccent("abballettu"));
        //i
        assertEquals("abbabbalìri", e.addGraveAccent("abbabbaliri"));
        // o
        assertEquals("abbaruòzzu", e.addGraveAccent("abbaruozzu"));
        //u
        assertEquals("abbabbanùtu", e.addGraveAccent("abbabbanutu"));
        // yöëïüj
        // ü
        assertEquals("rraǜsu", e.addGraveAccent("rraüsu"));
    }

    @Test
    void shouldAddGraveAccentToThirdToLastIfSecondToLastIsIWithDieresis(){
        assertEquals("zïhka", e.addGraveAccent("zïhka"));
        assertEquals("xyàzïhka", e.addGraveAccent("xyazïhka"));
        assertEquals("xyèzïhka", e.addGraveAccent("xyezïhka"));
        assertEquals("xyìzïhka", e.addGraveAccent("xyizïhka"));
        assertEquals("xyòzïhka", e.addGraveAccent("xyozïhka"));
        assertEquals("xyùzïhka", e.addGraveAccent("xyuzïhka"));
        assertEquals("xyä̀zïhka", e.addGraveAccent("xyäzïhka"));
        assertEquals("xyë̀zïhka", e.addGraveAccent("xyëzïhka"));
        assertEquals("xyö̀zïhka", e.addGraveAccent("xyözïhka"));
        assertEquals("xyǜzïhka", e.addGraveAccent("xyüzïhka"));
    }
}
