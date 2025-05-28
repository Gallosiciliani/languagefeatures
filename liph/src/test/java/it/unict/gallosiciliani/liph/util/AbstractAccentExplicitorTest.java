package it.unict.gallosiciliani.liph.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractAccentExplicitorTest {

    protected abstract String getAccented(final String expr);

    @Test
    void shouldNotAlterWordsWithAGraveAccent(){
        assertEquals("àbetö", getAccented("àbetö")); //galloitalic
        assertEquals("nserraghjè", getAccented("nserraghjè"));
        assertEquals("mìserö", getAccented("mìserö"));
        assertEquals("cuòsgiö", getAccented("cuòsgiö"));
        assertEquals("servetù", getAccented("servetù"));

        //invented
        assertEquals("123ä̀456", getAccented("123ä̀456"));
        assertEquals("123ë̀456", getAccented("123ë̀456"));
        assertEquals("123ï̀456", getAccented("123ï̀456"));
        assertEquals("123ö̀456", getAccented("123ö̀456"));
        assertEquals("123ǜ456", getAccented("123ǜ456"));
    }

    @Test
    void shouldNotAlterWordsWithASingleVowel(){
        assertEquals("abs", getAccented("abs"));
    }

    @Test
    void shouldAddGraveAccentToTheSecondToLastVowel(){
        // on Gallo-Italian terms
        assertEquals("àla", getAccented("ala"));
        assertEquals("afànö", getAccented("afanö"));
        assertEquals("aciprètë", getAccented("acipretë"));
        assertEquals("alegrìa", getAccented("alegria"));
        assertEquals("abòrtö", getAccented("abortö")); //galloitalic
        assertEquals("abùsö", getAccented("abusö")); //galloitalic
        assertEquals("acidë̀ntë", getAccented("acidëntë")); //galloitalic
        assertEquals("agö̀stö", getAccented("agöstö"));

        // da chiedere aghjö agrefuoghjö

        //on Sicilian terms
        // a
        assertEquals("àba", getAccented("aba"));
        assertEquals("abbaccarunàtu", getAccented("abbaccarunatu"));
        //e
        assertEquals("abballèttu", getAccented("abballettu"));
        //i
        assertEquals("abbabbalìri", getAccented("abbabbaliri"));
        // o
        assertEquals("abbaruòzzu", getAccented("abbaruozzu"));
        //u
        assertEquals("abbabbanùtu", getAccented("abbabbanutu"));
        // yöëïüj
        // ü
        assertEquals("rraǜsu", getAccented("rraüsu"));
    }

    @Test
    void shouldAddGraveAccentToThirdToLastIfSecondToLastIsIWithDieresis(){
        assertEquals("zïhka", getAccented("zïhka"));
        assertEquals("xyàzïhka", getAccented("xyazïhka"));
        assertEquals("xyèzïhka", getAccented("xyezïhka"));
        assertEquals("xyìzïhka", getAccented("xyizïhka"));
        assertEquals("xyòzïhka", getAccented("xyozïhka"));
        assertEquals("xyùzïhka", getAccented("xyuzïhka"));
        assertEquals("xyä̀zïhka", getAccented("xyäzïhka"));
        assertEquals("xyë̀zïhka", getAccented("xyëzïhka"));
        assertEquals("xyö̀zïhka", getAccented("xyözïhka"));
        assertEquals("xyǜzïhka", getAccented("xyüzïhka"));
    }

    @Test
    void shouldAddGraveAccentToTheLastVowelOnWordsEndingVowelAndN(){
        assertEquals("xàn", getAccented("xan"));
        assertEquals("xèn", getAccented("xen"));
        assertEquals("xìn", getAccented("xin"));
        assertEquals("xòn", getAccented("xon"));
        assertEquals("xùn", getAccented("xun"));
        assertEquals("xä̀n", getAccented("xän"));
        assertEquals("xë̀n", getAccented("xën"));
        assertEquals("xö̀n", getAccented("xön"));
        assertEquals("xǜn", getAccented("xün"));
        assertEquals("xaxàn", getAccented("xaxan"));
        assertEquals("xaxèn", getAccented("xaxen"));
        assertEquals("xaxìn", getAccented("xaxin"));
        assertEquals("xaxòn", getAccented("xaxon"));
        assertEquals("xaxùn", getAccented("xaxun"));
        assertEquals("xaxä̀n", getAccented("xaxän"));
        assertEquals("xaxë̀n", getAccented("xaxën"));
        assertEquals("xaxï̀n", getAccented("xaxïn"));
        assertEquals("xaxö̀n", getAccented("xaxön"));
        assertEquals("xaxǜn", getAccented("xaxün"));
    }

    @Test
    void shouldNotApplyToPolyrematicExpressions(){
        assertEquals("polirematic expression", getAccented("polirematic expression"));
    }

}
