package it.unict.gallosiciliani.gs;

//import it.unict.gallosiciliani.gs.util.GSFeaturesOntologyGenerator;
//import it.unict.gallosiciliani.languagefeatures.util.*;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * An ontology for all the language features defined in the
 * Gallosiciliani project.
 */

@Slf4j
public class GSFeatures extends OntologyLoader implements LinguisticPhenomenonLabelProvider {
    public static String IRI = "https://gallosiciliani.unict.it/ns/gs-features";
    public static String NS = IRI+"#";

    public static final String NICOSIA_FEATURE_OBJ_PROPERTY = NS+"nicosiaFeature";
    public static final String NOVARA_DI_SICILIA_FEATURE_OBJ_PROPERTY = NS+"novaraDiSiciliaFeature";
    public static final String SAN_FRATELLO_FEATURE_OBJ_PROPERTY = NS+"sanFratelloFeature";
    public static final String SPERLINGA_FEATURE_OBJ_PROPERTY = NS+"sperlingaFeature";
    public static String NORTHERN_FEATURE_OBJ_PROPERTY=NS+"northernItalyFeature";
    public static String SOUTHERN_FEATURE_OBJ_PROPERTY=NS+"southernItalyFeature";

    @Getter
    private final List<RegexLinguisticPhenomenon> regexLinguisticPhenomena;

    /**
     * Private constructor, use factory methods.
     *
     * @param classpathResource resource containing the ontology
     */
    private GSFeatures(final String classpathResource) throws IOException {
        super(classpathResource);
        regexLinguisticPhenomena = RegexLinguisticPhenomenaReader.read(getModel()).getFeatures();
    }

    /**
     * Private constructor, use factory methods.
     *
     * @param iri IRI of the ontology
     */
    private GSFeatures(final URI iri) throws IOException {
        super(iri);
        regexLinguisticPhenomena = RegexLinguisticPhenomenaReader.read(getModel()).getFeatures();
    }

    /**
     * Load a base version of this ontology, containing just definitions
     * @return base version of the ontology.
     */
    public static GSFeatures loadBase() throws IOException {
        return new GSFeatures("gs-features-base.ttl");
    }

    /**
     * Load the full ontology online
     * @return the ontology
     */
    public static GSFeatures loadOnline() throws IOException {
        return new GSFeatures(URI.create(IRI));
    }

    /**
     * Load the full ontology from ../ns
     * @return the ontology
     */
    public static GSFeatures loadLocal() throws IOException {
        return new GSFeatures("gs-features.ttl");
    }

    /**
     * Load into this ontology a set of GS Features described in a CSV file
     * @param csvFileReader reader for the CSV file containing features
     * @throws IOException on read error
     */
    public void loadFeatures(final Reader csvFileReader) throws IOException {
        CSVParser.parse(csvFileReader, CSVFormat.DEFAULT).forEach(new GSFeaturesOntologyGenerator(getModel()));
    }

    /**
     * Get the label for the specified feature
     * @param featureCode code characterizing the feature
     * @param locale label local
     * @return the label associated to the feature with the speicified code in the give locale
     */
    public String getLabel(final GSLanguageFeatureCode featureCode, final Locale locale) {
        final String iri = NS+featureCode;
        return getLabel(NS+featureCode, locale);
    }

    @Override
    public String getLabel(final LinguisticPhenomenon linguisticPhenomenon, final Locale locale) {
        return getLabel(linguisticPhenomenon.getIRI(), locale);
    }

    private String getLabel(final String featureIRI, final Locale locale) {
        try {
            final Property p = getModel().getProperty(featureIRI);
            return getModel().getRequiredProperty(p, RDFS.label).getString();
        } catch (final PropertyNotFoundException e){
            throw new IllegalArgumentException("Unable to get label for phenomenon "+featureIRI);
        }
    }


    /**
     * Attempt to derive target from etymon using the regex features
     * @param etymon
     * @param target
     * @return nearest derivations
     */
/*    public NearestDerivation derives(final String etymon, final String target){
        final NearestDerivation d = new NearestDerivation(target);
        new DerivationPathNodeImpl(etymon).apply(d, regexLinguisticPhenomena);
        return d;
    }*/
}
