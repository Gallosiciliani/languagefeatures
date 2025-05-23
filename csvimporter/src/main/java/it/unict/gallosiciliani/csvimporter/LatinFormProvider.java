package it.unict.gallosiciliani.csvimporter;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;

import java.util.stream.Stream;

/**
 * Provide latin forms to other components
 */
public interface LatinFormProvider {

    /**
     * Provide a latin form corresponding to the specified latin expression
     *
     * @param latinExpression the etymon as string
     * @param suggestedIRI a desired IRI for the generated form
     * @return a form corresponding to the latin expression
     */
    Form getLatinForm(String latinExpression, String suggestedIRI);

    /**
     * Get all available latin forms (all forms in the dictionary this provided is based on)
     * @return Stream with all available forms provided by this provider
     */
    Stream<String> getAll();
}
