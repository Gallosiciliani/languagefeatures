package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Extract all terms from the PDF of Vacabolario di Nicosia e Sperlinga. This parser returns only relevant tokens
 * (see {@link ParsingDataConsumer}) which are: lemmas, cojunctions between lemmas, and part of speech
 * @author Cristiano Longo
 * @see <a href="https://apache.googlesource.com/pdfbox/+/trunk/examples/src/main/java/org/apache/pdfbox/examples/util/PrintTextColors.java">PrintTextColors.java</a>
 */
@Slf4j
public class Parser extends PDFTextStripper implements AutoCloseable{

    //font attributes
    private static final float[] HIGHLIGHT = {0f, 1f, 0f, 0f};
    private static final float TYPE_FONT_SIZE = 33.0f;
    private static final float LEMMA_FONT_SIZE = 38.0f;
    private static final String LEMMA_FONT = "VNICArialexpo";

    private final PDDocument pdf;

    private ParsingState currentState;

    /**
     * Create an extractor for a {@link org.apache.pdfbox.pdmodel.PDDocument}
     *
     * @param consumer    will accept recognized lemmas and lemma types
     * @param pdfFilePath PDF file of the Vocabulary
     * @throws IOException If there is an error loading the properties.
     */
    public Parser(final ParsingDataConsumer consumer, final String pdfFilePath) throws IOException {
        addOperator(new SetStrokingColorSpace(this));
        addOperator(new SetNonStrokingColorSpace(this));
        addOperator(new SetStrokingDeviceCMYKColor(this));
        addOperator(new SetNonStrokingDeviceCMYKColor(this));
        addOperator(new SetNonStrokingDeviceRGBColor(this));
        addOperator(new SetStrokingDeviceRGBColor(this));
        addOperator(new SetNonStrokingDeviceGrayColor(this));
        addOperator(new SetStrokingDeviceGrayColor(this));
        addOperator(new SetStrokingColor(this));
        addOperator(new SetStrokingColorN(this));
        addOperator(new SetNonStrokingColor(this));
        addOperator(new SetNonStrokingColorN(this));
        setSortByPosition(true);
        pdf=Loader.loadPDF(new File(pdfFilePath));
        this.currentState=new InitialState(consumer);
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
        log.info("Parsing page {} of {}", pageNum, pdf.getDocumentInformation().getTitle());
        setStartPage(pageNum);
        setEndPage(pageNum);
        getText(pdf);
    }

    @Override
    protected void processTextPosition(final TextPosition text)
    {
        super.processTextPosition(text);
        final String c = text.getUnicode();

        //ignore digits, usually used for superscripts
        if (isDigit(c))
            return;

        if (c.isBlank())
            currentState = currentState.blank(c);
        else if (isLemmaFont(text))
            currentState = currentState.withLemmaFont(c);
        else if (isPOSFont(text))
            currentState = currentState.withPOSFont(c);
        else
            currentState = currentState.withOtherFont(c);
        log.debug("Transition char {} -> {}",c,currentState);
    }

    private boolean isLemmaFont(final TextPosition text){
        return text.getFontSizeInPt()==LEMMA_FONT_SIZE &&
                text.getFont().getName().endsWith(LEMMA_FONT);
    }

    /**
     *
     * @param c a string with just one character
     * @return true if c is in [0..9], false otherwise
     */
    private boolean isDigit(final String c){
        return Character.isDigit(c.charAt(0));
    }

    private boolean isPOSFont(final TextPosition text){
        final float[] nonStrokingColorComponents = getGraphicsState().getNonStrokingColor().getComponents();
        return Arrays.equals(HIGHLIGHT, nonStrokingColorComponents) && text.getFontSizeInPt()==TYPE_FONT_SIZE;
    }
}
