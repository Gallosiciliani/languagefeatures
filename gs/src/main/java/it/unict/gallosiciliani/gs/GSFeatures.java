package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * An ontology for all the language features defined in the
 * Gallosiciliani project.
 */

@Getter
@Slf4j
public class GSFeatures extends OntologyLoader{
    public static String IRI = "https://gallosiciliani.unict.it/ns/gs-features";
    public static String NS = IRI+"#";

    private final List<RegexLinguisticPhenomenon> regexLinguisticPhenomena;

    /**
     * Provide local identifiers as linguistic phenomena labels
     */
    public static final LinguisticPhenomenonLabelProvider LABEL_PROVIDER_ID= (linguisticPhenomenon, locale) -> linguisticPhenomenon.getIRI().substring(NS.length());

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
}
