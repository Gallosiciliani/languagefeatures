package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonLabelProvider;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.FiniteStatePhenomenaQuery;
import it.unict.gallosiciliani.liph.regex.RegexLiph1FeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
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

    private final List<LinguisticPhenomenon> regexLinguisticPhenomena;

    /**
     * Provide local identifiers as linguistic phenomena labels
     */
    public static final LinguisticPhenomenonLabelProvider LABEL_PROVIDER_ID= (linguisticPhenomenon, locale) -> linguisticPhenomenon.getId().substring(NS.length());

    /**
     * Private constructor, use factory methods.
     */
    public GSFeatures() throws IOException {
        super("gs-features.ttl");
        final RegexLinguisticPhenomenaReader reader=new RegexLinguisticPhenomenaReader();
        reader.read(getModel(), new RegexLiph1FeatureQuery().ignoreDeprecated());
        reader.read(getModel(), new FiniteStatePhenomenaQuery());
        regexLinguisticPhenomena = reader.getFeatures();
    }
}
