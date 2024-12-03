package it.unict.gallosiciliani.importing.pdf.writing;

import it.unict.gallosiciliani.importing.persistence.FileEntityManagerFactoryHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Base model factory for the ontology containing the Nicosia e Sperlinga Gallo-Italic variety.
 * It starts with an ontology containing just the lexicon.
 *
 * @author Cristiano Longo
 */
public class NicosiaSperlingaEntityManagerFactory extends FileEntityManagerFactoryHelper {

    public static final String NS="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#";

    public NicosiaSperlingaEntityManagerFactory(final String filePath) throws IOException {
        super(copyBaseOntology(filePath));
    }

    /**
     * Copy the base ontology stored as a resource to a path into the file system
     * @param filePath destination
     * @return the file path (identical to the filePath param)
     */
    private static String copyBaseOntology(final String filePath) throws IOException {
        final ClassLoader classloader=Thread.currentThread().getContextClassLoader();
        try(final InputStream s= Objects.requireNonNull(classloader.getResourceAsStream("nicosiaesperlinga-base.ttl"))){
            Files.copy(s, Path.of(filePath));
        }
        return filePath;
    }
}
