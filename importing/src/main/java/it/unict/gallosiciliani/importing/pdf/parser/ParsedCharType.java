package it.unict.gallosiciliani.importing.pdf.parser;

import org.apache.pdfbox.text.TextPosition;

import java.util.Arrays;

/**
 * Recognized types of parsed characters
 */
enum ParsedCharType {

    WITH_LEMMA_FONT, //character with lemma font
    WITH_POS_FONT, //lemma with font characterizing parts of speech
    OTHER; //none of the preceding

    private static final float[] HIGHLIGHT = {0f, 1f, 0f, 0f};
    private static final float LEMMA_FONT_SIZE = 38.0f;
    private static final String LEMMA_FONT = "VNICArialexpo";

    /**
     * Get the type corresponding to the given char with the specified color
     *
     * @param text {@link TextPosition} of the character
     * @param color character color
     * @return char type
     */
    static ParsedCharType get(final TextPosition text, final float[] color){
        if (text.getFontSizeInPt()==LEMMA_FONT_SIZE && text.getFont().getName().endsWith(LEMMA_FONT))
            return WITH_LEMMA_FONT;
        //on page 969, part of speech has a smaller font size. In light of this, we don't check font size
        if (Arrays.equals(HIGHLIGHT, color))
            return WITH_POS_FONT;
        return OTHER;
    }
}
