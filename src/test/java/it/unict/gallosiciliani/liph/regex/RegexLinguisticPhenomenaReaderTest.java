package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.LanguageFeatureTestHelper;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link RegexLinguisticPhenomenaReader}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenaReaderTest {
    @Test
    void shouldGetAllRegexFeatures(){
        final String ontologyTTL = "@prefix :<"+ LinguisticPhenomena.NS+"> .\n" +
                "@base <"+ LinguisticPhenomena.NS+"> .\n"+
                getRegexFeatureTTL("http://test.org/f1", "a", "x") + ".\n" +
                //multiple replacements
                getRegexFeatureTTL("http://test.org/f2", "b", "y") + ";\n" +
                "\t<"+ LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY+"> \"z\" .";

        final Model m = RDFParser.fromString(ontologyTTL).lang(RDFLanguages.TTL).toModel();
        final RegexLinguisticPhenomenaReader reader = RegexLinguisticPhenomenaReader.read(m);
        assertTrue(reader.getExceptions().isEmpty());

        final List<RegexLinguisticPhenomenon> actual = reader.getFeatures();
        assertEquals(2, actual.size());
        new LanguageFeatureTestHelper(actual.get(0)).derives("123a456", "123x456");
        final RegexLinguisticPhenomenon f2 = actual.get(1);
        new LanguageFeatureTestHelper(f2).derives("123b456", "123y456", "123z456");
    }

    @Test
    void shouldReportExceptionOnMultilpeRegex(){
        final String ontologyTTL = "@prefix :<"+ LinguisticPhenomena.NS+"> .\n" +
                getRegexFeatureTTL("http://test.org/f1", "a", "x") + ";\n" +
                "\t<"+ LinguisticPhenomena.REGEX_ANN_PROPERTY+"> \"b\" .";
        final Model m = RDFParser.fromString(ontologyTTL).lang(RDFLanguages.TTL).toModel();
        final RegexLinguisticPhenomenaReader actual = RegexLinguisticPhenomenaReader.read(m);
        assertEquals(1, actual.getExceptions().size());
    }

    /**
     * Triples in TTL format representing a regexe feature
     * @param featureIRI iri of the feature
     * @param regex regular expression
     * @param replacement replacement
     * @return triples
     */
    private String getRegexFeatureTTL(final String featureIRI, final String regex, final String replacement){
        return "\t<"+featureIRI+"> <"+ LinguisticPhenomena.REGEX_ANN_PROPERTY+"> \""+regex+"\" ;\n" +
                "\t<"+ LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY+"> \""+replacement+"\"";
    }
}
