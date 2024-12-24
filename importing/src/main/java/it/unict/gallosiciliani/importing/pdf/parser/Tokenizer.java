package it.unict.gallosiciliani.importing.pdf.parser;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Extract lemmas and corresponding part of speech from the source PDF
 */
@Slf4j
class Tokenizer extends PDFTextStripper {
    private static final int HEADER_HEIGHT_PX=92;
    @Setter
    private UnderlinedZDetector underlines;
    private ParsingState currentState;

    Tokenizer(final ParsingDataConsumer consumer) {
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
        this.currentState=new InitialState(new AccentedWithDiacriticsCorrector(consumer));
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

        // detect underlined z
        final String c=underlines.getUnicode(text);

        final ParsedCharType t=ParsedCharType.get(text,
                getGraphicsState().getNonStrokingColor().getComponents());
        currentState=currentState.parse(c, t);
        log.debug("Transition char \"{}\" ({}, codes {}) type {} -> {} ,position {} {}",c, getBytesString(text.getUnicode()), text.getCharacterCodes(), t, currentState, text.getTextMatrix().getTranslateX(), text.getTextMatrix().getTranslateY());
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

}
