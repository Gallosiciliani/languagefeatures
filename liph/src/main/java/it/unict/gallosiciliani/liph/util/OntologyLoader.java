package it.unict.gallosiciliani.liph.util;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    private final String ontologyIRI;
    private final String name;
    private final String comment;
    private final String namespace;

    /**
     * Create the ontology from the resource in the classpath
     *
     * @param classpathResource resource containing the ontology
     * @param ontologyIRI iri of the ontology
     */
    public OntologyLoader(final String classpathResource, final String ontologyIRI) throws IOException {
        this.ontologyIRI=ontologyIRI;
        ontologyAsStr=IOUtils.resourceToString(classpathResource, StandardCharsets.UTF_8, OntologyLoader.class.getClassLoader());
        model=RDFParser.create().fromString(ontologyAsStr)
                .lang(RDFLanguages.TTL)
                .errorHandler(ErrorHandlerFactory.errorHandlerStrict).
                toModel();

        namespace= getModel().getNsPrefixURI(""); //base prefix
        final OntologyItem o=retrieve(ontologyIRI);
        name=o.getLabel();
        comment=o.getComment();

    }

    public OntologyItem retrieve(final String iri){
        final Resource r=model.getResource(iri);
        final String label=r.getProperty(RDFS.label).getString();
        final String comment=r.getProperty(RDFS.comment).getString();
        return new OntologyItem() {
            @Override
            public String getIri() {
                return iri;
            }

            @Override
            public String getLabel() {
                return label;
            }

            @Override
            public String getComment() {
                return comment;
            }
        };
    }

    public HashedOntologyItem retrieveHashed(final String iri){
        final Resource r=model.getResource(iri);
        final String label=r.getProperty(RDFS.label).getString();
        final String comment=r.getProperty(RDFS.comment).getString();
        return new HashedOntologyItem(iri, getNamespace()) {
            @Override
            public String getLabel() {
                return label;
            }

            @Override
            public String getComment() {
                return comment;
            }
        };
    }

    /**
     * Retrieve information about multiple ontology items
     * @param itemIris IRIs of the ontology items
     * @return a list of {@link OntologyItem} providing basic information about the items
     */
    public List<HashedOntologyItem> retrieveHashed(final String[] itemIris){
        final List<HashedOntologyItem> result=new ArrayList<>(itemIris.length);
        for(final String iri : itemIris)
            result.add(retrieveHashed(iri));
        return result;
    }

    @Override
    public void close() {
        model.close();
    }
}
