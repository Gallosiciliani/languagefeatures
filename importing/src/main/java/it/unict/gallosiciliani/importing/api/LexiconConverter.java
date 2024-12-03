package it.unict.gallosiciliani.importing.api;

import java.io.IOException;

/**
 * Basic interface for converters in for different source file formats.
 * Read lexicon entries from a file and generate the corresponding {@link it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry}
 * instances.
 *
 * @author Cristiano Longo
 */
public interface LexiconConverter {
    /**
     * Read the source file and produce the corresponding entries
     * @param sourceFile the source file
     * @throws IOException is some error occurred while reading and writing
     */
    void read(final String sourceFile) throws IOException;
}
