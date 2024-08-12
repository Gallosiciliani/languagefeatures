package it.unict.gallosiciliani.util;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Ontology stored on the classpath
 */
@Getter
public class OntologyLoader implements AutoCloseable{

    /**
     * Raw representation of the file read from class path
     */
    private final String ontologyAsStr;
    private final Model model;
    /**
     * Create the ontology from the resource in the classpath
     * @param classpathResource resource containing the ontology
     */
    public OntologyLoader(final String classpathResource) throws IOException {
        ontologyAsStr=IOUtils.resourceToString(classpathResource, StandardCharsets.UTF_8, OntologyLoader.class.getClassLoader());
        model=RDFParser.create().fromString(ontologyAsStr)
                .lang(RDFLanguages.TTL)
                .errorHandler(ErrorHandlerFactory.errorHandlerStrict).
                toModel();
    }

    /**
     * Create the ontology from an online resource
     * @param iri IRI of the ontology
     */
    public OntologyLoader(final URI iri) throws IOException {
        ontologyAsStr = IOUtils.toString(iri, StandardCharsets.UTF_8);
        model= RDFParser.create().fromString(ontologyAsStr)
                .lang(RDFLanguages.TTL)
                .errorHandler(ErrorHandlerFactory.errorHandlerStrict).
                toModel();
    }

    @Override
    public void close() {
        model.close();
    }
}
