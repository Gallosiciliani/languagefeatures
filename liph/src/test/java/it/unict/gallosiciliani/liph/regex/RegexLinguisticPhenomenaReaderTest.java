package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.LanguageFeatureTestHelper;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link RegexLinguisticPhenomenaReader}
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenaReaderTest {
    @Test
    void shouldGetAllRegexFeatures(){
        final String ontologyTTL = "@prefix :<"+ LinguisticPhenomena.NS+"> .\n" +
                "@base <"+ LinguisticPhenomena.NS+"> .\n"+
                getLiph2RegexFeatureTTL("http://test.org/f1", "f1", "a", "x") + ".\n" +
                //multiple replacements
                getLiph2RegexFeatureTTL("http://test.org/f2", "f2","b", "y") + " .";
        shouldGetAllRegexFeatures(ontologyTTL);
    }
    
    @Test
    void shouldGetAllLiph1RegexFeatures(){
        final String ontologyTTL = "@prefix :<"+ LinguisticPhenomena.NS+"> .\n" +
                "@base <"+ LinguisticPhenomena.NS+"> .\n"+
                getLiph1RegexFeatureTTL("http://test.org/f1", "a", "x") + ".\n" +
                //multiple replacements
                getLiph1RegexFeatureTTL("http://test.org/f2", "b", "y") + " .";
        shouldGetAllRegexFeatures(ontologyTTL);
    }

    void shouldGetAllRegexFeatures(final String ontologyTTL){

        final Model m = RDFParser.fromString(ontologyTTL, RDFLanguages.TTL).toModel();
        final RegexLinguisticPhenomenaReader reader = RegexLinguisticPhenomenaReader.read(m);

        final List<LinguisticPhenomenon> actual = reader.getFeatures();
        assertEquals(2, actual.size());
        new LanguageFeatureTestHelper(actual.get(0)).derives("123a456", "123x456");
        new LanguageFeatureTestHelper(actual.get(1)).derives("123b456", "123y456");
    }

    /**
     * Triples in TTL format representing a regexe feature specified as indicated in Liph 1
     * @param featureIRI iri of the feature
     * @param regex regular expression
     * @param replacement replacement
     * @return triples
     */
    private String getLiph1RegexFeatureTTL(final String featureIRI, final String regex, final String replacement){
        return "\t<"+featureIRI+"> <"+ LinguisticPhenomena.REGEX_ANN_PROPERTY+"> \""+regex+"\" ;\n" +
                "\t<"+ LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY+"> \""+replacement+"\"";
    }

    /**
     * Triples in TTL format representing a regexe feature specified as indicated in Liph 2
     * @param featureIRI iri of the feature
     * @param label feature label
     * @param regex regular expression
     * @param replacement replacement
     * @return triples
     */
    private String getLiph2RegexFeatureTTL(final String featureIRI, final String label, final String regex, final String replacement){
        return "\t<"+featureIRI+"> <"+ RDFS.label.getURI() +"> \""+label+"\" ;\n" +
                "\t\t<"+ LinguisticPhenomena.MATCHING_PATTERN_DATA_PROPERTY+"> \""+regex+"\" ;\n" +
                "\t\t<"+ LinguisticPhenomena.REPLACE_WITH_DATA_PROPERTY+"> \""+replacement+"\"";
    }

}
