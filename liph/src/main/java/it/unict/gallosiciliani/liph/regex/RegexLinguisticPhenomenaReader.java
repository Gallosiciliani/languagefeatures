package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.Getter;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Get regex features from SPARQL result set
 */
@Getter
public class RegexLinguisticPhenomenaReader{
    private final List<LinguisticPhenomenon> features = new ArrayList<>();

    /**
     * Factory method, read all regex features from a model
     * @param model the model
     * @param query a selection query for regex features
     */
    public void read(final Model model, final RegexFeatureQuery query) {
        for (RegexFeatureQuerySolution s : query.exec(model))
            accept(s);

    }

    /**
     * Factory method, read all regex features from a model
     * @param model the model
     * @return a {@link RegexLinguisticPhenomenaReader} with all the recognizable and fully-specified
     * features in the model
     */
    public static RegexLinguisticPhenomenaReader read(final Model model) {
        final RegexLinguisticPhenomenaReader reader=new RegexLinguisticPhenomenaReader();
        reader.read(model, new RegexLiph1FeatureQuery().ignoreDeprecated());
        reader.read(model, new FiniteStatePhenomenaQuery());
        return reader;
    }

    private void accept(final RegexFeatureQuerySolution solution) {
        final FiniteStateLinguisticPhenomenon p=new FiniteStateLinguisticPhenomenon();
        p.setId(solution.getFeatureIRI());
        p.setLabel(solution.getFeatureLabel());
        p.setComment(solution.getFeatureComment());
        p.setMatchingPattern(solution.getRegex());
        p.setReplaceWith(solution.getReplacement());
        features.add(p);
    }
}
