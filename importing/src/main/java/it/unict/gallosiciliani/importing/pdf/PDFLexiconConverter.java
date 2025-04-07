package it.unict.gallosiciliani.importing.pdf;

import it.unict.gallosiciliani.importing.api.LexiconConverter;
import it.unict.gallosiciliani.importing.api.LexiconConverterFactory;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.pdf.generator.LexicalEntriesGenerator;
import it.unict.gallosiciliani.importing.pdf.parser.Parser;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Create a converter that generates {@link LexicalEntry}
 * from the source PDF vocabulary
 */
public class PDFLexiconConverter implements LexiconConverter {

    public static final LexiconConverterFactory FACTORY = (consumer, iris, posIndividualProvider) -> new PDFLexiconConverter(consumer, iris, posIndividualProvider, 121, 1084);

    private final LexicalEntriesGenerator entriesGenerator;
    private final int startPage;
    private final int endPage;

    /**
     * Package private, test use only. Use the factory instead.
     *
     * @param consumer where generated entries will be sent
     * @param iris provider to get the entry IRIs
     * @param posIndividualProvider Part Of Speech individuals
     * @param startPage page of the pdf file where to start (base 1)
     * @param endPage last page of the pdf file to be processed (base 1)
     */
    public PDFLexiconConverter(final Consumer<LexicalEntry> consumer,
                               final IRIProvider iris,
                               final POSIndividualProvider posIndividualProvider,
                               final int startPage,
                               final int endPage){
        entriesGenerator=new LexicalEntriesGenerator(consumer, iris, posIndividualProvider);
        this.startPage=startPage;
        this.endPage=endPage;
    }

    @Override
    public void read(final String sourceFile) throws IOException {
        try(final Parser p=new Parser(entriesGenerator, sourceFile)){
            p.parsePages(startPage, endPage);
        }
    }
}
