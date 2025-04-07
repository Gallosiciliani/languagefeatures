package it.unict.gallosiciliani.importing.api;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.io.IOException;

/**
 * Basic interface for converters in for different source file formats.
 * Read lexicon entries from a file and generate the corresponding {@link LexicalEntry}
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
