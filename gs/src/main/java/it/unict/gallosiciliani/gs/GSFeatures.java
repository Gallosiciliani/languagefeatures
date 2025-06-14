package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.FiniteStatePhenomenaQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.util.LinguisticPhenomenonByLabelRetrieverImpl;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * An ontology for all the language features defined in the
 * Gallosiciliani project.
 */

@Getter
@Slf4j
public class GSFeatures extends OntologyLoader implements LinguisticPhenomenonByLabelRetriever{
    public static final String IRI = "https://gallosiciliani.unict.it/ns/gs-features";
    public static final String NS = IRI+"#";
    public static final String VERSION = "2.0.1";

    public static final String GALLOSICILIAN_FEATURE_CLASS=NS+"GalloSicilianFeature";
    public static final String LENIZ_CLASS=NS+"Leniz";
    public static final String DEGEM_CLASS=NS+"Degem";
    public static final String ASSIB_CLASS=NS+"Assib";
    public static final String DISSIM_CLASS=NS+"Dissim";
    public static final String DITT_CLASS=NS+"Ditt";
    public static final String VOCAL_CLASS=NS+"Vocal";
    public static final String AFER_CLASS=NS+"Afer";
    public static final String ELIM_CLASS =NS+"Elim";
    public static final String PALAT_CLASS=NS+"Palat";
    public static final String INF_CLASS =NS+"Inf";

    public static final String[] CATEGORY_CLASSES={LENIZ_CLASS, DEGEM_CLASS, ASSIB_CLASS, DISSIM_CLASS, DITT_CLASS,
            VOCAL_CLASS, AFER_CLASS, ELIM_CLASS, PALAT_CLASS};

    private final List<LinguisticPhenomenon> regexLinguisticPhenomena;
    private final LinguisticPhenomenonByLabelRetriever phenomenonByLabelRetriever;

    /**
     * Private constructor, use factory methods.
     */
    public GSFeatures() throws IOException {
        super("gs-features.ttl", IRI);
        final RegexLinguisticPhenomenaReader reader=new RegexLinguisticPhenomenaReader();
        reader.read(getModel(), new FiniteStatePhenomenaQuery());
        regexLinguisticPhenomena=reader.getFeatures();
        phenomenonByLabelRetriever=LinguisticPhenomenonByLabelRetrieverImpl.build(regexLinguisticPhenomena);
    }

    @Override
    public LinguisticPhenomenon getByLabel(final String label, final Locale locale) {
        return phenomenonByLabelRetriever.getByLabel(label, locale);
    }
}
