package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.IllegalLinguisticPhenomenonDefinition;
import lombok.Getter;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Get regex features from SPARQL result set
 */
public class RegexLinguisticPhenomenaReader implements Consumer<RegexFeatureQuerySolution> {
    @Getter
    private final List<RegexLinguisticPhenomenon> features = new ArrayList<>();
    /**
     * Exceptions occurred during the retrieving
     */
    @Getter
    private final List<IllegalLinguisticPhenomenonDefinition> exceptions = new LinkedList<>();

    private String currentFeatureIRI = null;
    private String currentFeatureRegex = null;
    private RegexLinguisticPhenomenon currentFeature;

    /**
     * Factory method, read all regex features from a model
     * @param model the model
     * @param query a selection query for regex features
     * @return a {@link RegexLinguisticPhenomenaReader} with all the recognizable and fully-specified
     * features in the model
     */
    public static RegexLinguisticPhenomenaReader read(final Model model, final RegexFeatureQuery query) {
        final RegexLinguisticPhenomenaReader reader = new RegexLinguisticPhenomenaReader();
        query.exec(model).forEach(reader);
        return reader;
    }

    /**
     * Factory method, read all regex features from a model
     * @param model the model
     * @return a {@link RegexLinguisticPhenomenaReader} with all the recognizable and fully-specified
     * features in the model
     */
    public static RegexLinguisticPhenomenaReader read(final Model model) {
        return read(model, new RegexFeatureQuery());
    }

    @Override
    public void accept(final RegexFeatureQuerySolution solution) {
        if (solution.getFeatureIRI().equals(currentFeatureIRI)) {
            if (solution.getRegex().equals(currentFeatureRegex))
                currentFeature.addReplacement(solution.getReplacement());
            else exceptions.add(new IllegalLinguisticPhenomenonDefinition("Multiple regex for "+currentFeatureIRI+": "
                    +currentFeatureRegex+" and "+solution.getRegex()));
        }
        else { // this is also the case when currentFeatureIRI == null
            currentFeatureIRI = solution.getFeatureIRI();
            currentFeatureRegex = solution.getRegex();
            currentFeature = new RegexLinguisticPhenomenon(solution.getFeatureIRI(), solution.getRegex(),
                    solution.getReplacement());
            features.add(currentFeature);
        }
    }
}
