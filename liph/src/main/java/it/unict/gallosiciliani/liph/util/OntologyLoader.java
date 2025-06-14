package it.unict.gallosiciliani.liph.util;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.io.StringWriter;
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

        final OntologyItem o=retrieve(ontologyIRI);
        name=o.getLabel();
        comment=o.getComment();
    }

    public OntologyItem retrieve(final String iri){
        final Resource r=model.getResource(iri);
        final String label=r.getProperty(RDFS.label).getString();
        final String comment=r.getProperty(RDFS.comment).getString();
        return new OntologyItem(iri, label, comment);
    }

    /**
     * Retrieve information about multiple ontology items
     * @param itemIris IRIs of the ontology items
     * @return a list of {@link OntologyItem} providing basic information about the items
     */
    private List<OntologyItem> retrieve(final String[] itemIris){
        final List<OntologyItem> result=new ArrayList<>(itemIris.length);
        for(final String iri : itemIris)
            result.add(retrieve(iri));
        return result;
    }

    @Override
    public void close() {
        model.close();
    }

    /**
     * Just print the ontology
     * @return the ontology as TTL
     */
    public String print(){
        final StringWriter w = new StringWriter();
        RDFWriter.source(getModel()).format(RDFFormat.TTL).build().output(w);
        return w.toString();
    }
}
