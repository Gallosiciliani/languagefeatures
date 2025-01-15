package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * An ontology for all the language features defined in the
 * Gallosiciliani project.
 */

@Getter
@Slf4j
public class GSFeatures extends OntologyLoader implements LinguisticPhenomenonLabelProvider {
    public static String IRI = "https://gallosiciliani.unict.it/ns/gs-features";
    public static String NS = IRI+"#";

    private final List<RegexLinguisticPhenomenon> regexLinguisticPhenomena;

    /**
     * Provide local identifiers as linguistic phenomena labels
     */
    public static final LinguisticPhenomenonLabelProvider LABEL_PROVIDER_ID=new LinguisticPhenomenonLabelProvider() {
        @Override
        public String getLabel(LinguisticPhenomenon linguisticPhenomenon, Locale locale) {
            return linguisticPhenomenon.getIRI().substring(NS.length());
        }
    };

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

    @Override
    @Deprecated
    public String getLabel(final LinguisticPhenomenon linguisticPhenomenon, final Locale locale) {
        return getLabel(linguisticPhenomenon.getIRI());
    }

    private String getLabel(final String featureIRI) {
        try {
            final Property p = getModel().getProperty(featureIRI);
            return getModel().getRequiredProperty(p, RDFS.label).getString();
        } catch (final PropertyNotFoundException e){
            throw new IllegalArgumentException("Unable to get label for phenomenon "+featureIRI);
        }
    }
}
