package it.unict.gallosiciliani.liph.regex;

import org.apache.jena.rdf.model.Model;

import java.util.List;

/**
 * SPARQL query to retrieve {@link it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon}
 */
public interface RegexFeatureQuery {
    /**
     * Execute the query against the specified model
     *
     * @param model the model
     * @return query results
     */
    List<RegexFeatureQuerySolution> exec(final Model model);
}