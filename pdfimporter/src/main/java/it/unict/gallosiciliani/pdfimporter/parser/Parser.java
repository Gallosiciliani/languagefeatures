package it.unict.gallosiciliani.pdfimporter.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

/**
 * Extract all terms from the PDF of Vacabolario di Nicosia e Sperlinga. This parser returns only relevant tokens
 * (see {@link ParsingDataConsumer}) which are: lemmas, cojunctions between lemmas, and part of speech
 * @author Cristiano Longo
 * @see <a href="https://apache.googlesource.com/pdfbox/+/trunk/examples/src/main/java/org/apache/pdfbox/examples/util/PrintTextColors.java">PrintTextColors.java</a>
 */
@Slf4j
public class Parser implements AutoCloseable{

    private final PDDocument pdf;
    private final Tokenizer tokenizer;

    /**
     * Create an extractor for a {@link org.apache.pdfbox.pdmodel.PDDocument}
     *
     * @param consumer    will accept recognized lemmas and lemma types
     * @param pdfFilePath PDF file of the Vocabulary
     * @throws IOException If there is an error loading the properties.
     */
    public Parser(final ParsingDataConsumer consumer, final String pdfFilePath) throws IOException {
        pdf=Loader.loadPDF(new File(pdfFilePath));
        this.tokenizer=new Tokenizer(consumer);
    }

    @Override
    public void close() throws IOException {
        pdf.close();
    }

    /**
     * Parse and consume a single page
     * @param pageNum the page number
     * @throws IOException if an error occurs reading the document
     */
    public void parsePage(final int pageNum) throws IOException {
        log.info("Parsing page {}", pageNum);
        final UnderlinedZDetector underlines=new UnderlinedZDetector((pdf.getPage(pageNum-1)));
        underlines.processPage();
        tokenizer.setUnderlines(underlines);
        tokenizer.setStartPage(pageNum);
        tokenizer.setEndPage(pageNum);
        tokenizer.getText(pdf);
    }

    /**
     * Parse and consume all pages in the specified range
     * @param startPage first page to be consumed
     * @param endPage last page to be consumed
     * @throws IOException if an error occurs reading the document
     */
    public void parsePages(final int startPage, final int endPage) throws IOException {
        for(int i=startPage; i<=endPage; i++) {
            parsePage(i);
        }
    }
}
