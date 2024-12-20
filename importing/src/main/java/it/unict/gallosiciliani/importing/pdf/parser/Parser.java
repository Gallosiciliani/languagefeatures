package it.unict.gallosiciliani.importing.pdf.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Extract all terms from the PDF of Vacabolario di Nicosia e Sperlinga. This parser returns only relevant tokens
 * (see {@link ParsingDataConsumer}) which are: lemmas, cojunctions between lemmas, and part of speech
 * @author Cristiano Longo
 * @see <a href="https://apache.googlesource.com/pdfbox/+/trunk/examples/src/main/java/org/apache/pdfbox/examples/util/PrintTextColors.java">PrintTextColors.java</a>
 */
@Slf4j
public class Parser extends PDFTextStripper implements AutoCloseable{

    private static final int HEADER_HEIGHT_PX=94;
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
        this.currentState=new InitialState(new AccentedWithDiacriticsCorrector(consumer));
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

    @Override
    protected void processAnnotation(PDAnnotation a, PDAppearanceStream s) throws IOException {
        System.out.println("---- annotation "+a);
        super.processAnnotation(a,s);
    }
    @Override
    protected void processTextPosition(final TextPosition text)
    {
        super.processTextPosition(text);

        //exclude page header
        if (text.getYDirAdj()<HEADER_HEIGHT_PX) {
            log.debug("Header char {} with Y position {} {}", text.getUnicode(), text.getYDirAdj(), text.getYDirAdj()+text.getFontSize());
            return;
        }

        //ignore digits, usually used for superscripts
        if (isDigit(text.getUnicode()))
            return;

        if (isUnderlined(text))
            log.info("--------------- underlined {}", text.getUnicode());

        final ParsedCharType t=ParsedCharType.get(text,
                getGraphicsState().getNonStrokingColor().getComponents());
        currentState=currentState.parse(text.getUnicode(), t);
        log.debug("Transition char \"{}\" ({}, codes {}) type {} -> {}",text.getUnicode(), getBytesString(text.getUnicode()), text.getCharacterCodes(), t, currentState);
    }

    private String getBytesString(final String unicodeChar){
        final byte[] bytes=unicodeChar.getBytes(StandardCharsets.UTF_8);
        return HexFormat.of().formatHex(bytes);
    }

    /**
     *
     * @param c a string with just one character
     * @return true if c is in [0..9], false otherwise
     */
    private boolean isDigit(final String c){
        return Character.isDigit(c.charAt(0));
    }

    /**
     * Get whether the specified character is underlined
     * @param p the position of the character
     * @return true if it is underlined, false otherwise
     */
    private boolean isUnderlined(final TextPosition p){
        return false;
    }
}
