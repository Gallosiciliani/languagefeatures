package it.unict.gallosiciliani.importing.csv;

import it.unict.gallosiciliani.importing.api.LexiconConverter;
import it.unict.gallosiciliani.importing.api.LexiconConverterFactory;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Parse rows in a CSV file with producing lexical entries corresponding to each row.
 *
 * @author Cristiano Longo
 */
public class CSVLexiconConverter implements LexiconConverter {

    public static final LexiconConverterFactory FACTORY= CSVLexiconConverter::new;

    private final Consumer<LexicalEntry> consumer;
    private final LatinFormProvider latinFormProvider;
    private final POSIndividualProvider posProvider;
    private final IRIProvider iris;

    /**
     * Test use only. Use the FACTORY instead.
     *
     * @param consumer where generated entries will be sent
     * @param iris provider to get the entry IRIs
     * @param posProvider Part Of Speech individuals
     */
    CSVLexiconConverter(final Consumer<LexicalEntry> consumer,
                        final IRIProvider iris,
                        final POSIndividualProvider posProvider) {
        this.consumer=consumer;
        this.iris=iris;
        this.posProvider=posProvider;
        this.latinFormProvider=new CachingLatinFormProvider();
    }

    @Override
    public void read(final String sourceFile) throws IOException {
        try(final FileInputStream csvFileInputStream = new FileInputStream(sourceFile);
            final InputStreamReader reader = new InputStreamReader(csvFileInputStream);
            final CSVParser csvParser=CSVParser.parse(reader, CSVFormat.DEFAULT)){

            final Iterator<CSVRecord> rowsIt = csvParser.iterator();
            final GSKBCSVParser parser = GSKBCSVParser.withHeaders(consumer,
                    rowsIt.next(),
                    latinFormProvider,
                    posProvider, iris);
            rowsIt.forEachRemaining(parser);
        }

    }
}
