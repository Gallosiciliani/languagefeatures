package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
     */
    public GSFeatures() throws IOException {
        super("gs-features.ttl");
        regexLinguisticPhenomena = RegexLinguisticPhenomenaReader.read(getModel(), new RegexFeatureQuery().ignoreDeprecated()).getFeatures();
    }
}
