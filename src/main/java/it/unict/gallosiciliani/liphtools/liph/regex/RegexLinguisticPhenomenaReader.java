package it.unict.gallosiciliani.liphtools.liph.regex;

import it.unict.gallosiciliani.liphtools.liph.IllegalLinguisticPhenomenonDefinition;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Get regex features from SPARQL result set
 */
@Slf4j
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
     * @return a {@link RegexLinguisticPhenomenaReader} with all the recognizable and fully-specified
     * features in the model
     */
    public static RegexLinguisticPhenomenaReader read(final Model model) {
        final RegexLinguisticPhenomenaReader reader = new RegexLinguisticPhenomenaReader();
        try(final QueryExecution e = QueryExecutionFactory.create(RegexFeatureQuerySolution.QUERY, model)){
            e.execSelect().forEachRemaining(querySolution -> reader.accept(new RegexFeatureQuerySolution(querySolution)));
        }
        return reader;
    }

    @Override
    public void accept(final RegexFeatureQuerySolution solution) {
        if (solution.featureIRI.equals(currentFeatureIRI)) {
            if (solution.regex.equals(currentFeatureRegex))
                currentFeature.addReplacement(solution.replacement);
            else exceptions.add(new IllegalLinguisticPhenomenonDefinition("Multiple regex for "+currentFeatureIRI+": "
                    +currentFeatureRegex+" and "+solution.regex));
        }
        else { // this is also the case when currentFeatureIRI == null
            currentFeatureIRI = solution.featureIRI;
            currentFeatureRegex = solution.regex;
            currentFeature = solution.buildFeature();
            features.add(currentFeature);
        }
    }
}
