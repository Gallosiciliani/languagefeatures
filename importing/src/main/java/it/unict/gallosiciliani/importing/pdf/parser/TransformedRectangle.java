package it.unict.gallosiciliani.importing.pdf.parser;
import java.awt.geom.Point2D;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;

/**
 * Just a copy-paste from <a href="https://github.com/mkl-public/testarea-pdfbox1/blob/master/src/main/java/mkl/testarea/pdfbox1/extract/PDFStyledTextStripper.java}">PDFStyledTextStripper</a>
 * with slight modifications
 */
class TransformedRectangle
{
    private final Point2D p0, p1, p2, p3;

    public TransformedRectangle(final Point2D p0, final Point2D p1, final Point2D p2, final Point2D p3)
    {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public boolean underlines(final TextPosition textPosition)
    {
        Matrix matrix = textPosition.getTextMatrix();
        // TODO: This is a very simplistic implementation only working for horizontal text without page rotation
        // and horizontal rectangular underlines with p0 at the left bottom and p2 at the right top

        // Check if rectangle horizontally matches (at least) the text
        if (p0.getX() > matrix.getTranslateX() + textPosition.getWidth() * .1f || p2.getX() < matrix.getTranslateX() + textPosition.getWidth() * .9f)
            return false;
        // Check whether rectangle vertically is at the right height to underline
        double vertDiff = p0.getY() - matrix.getTranslateX();
        if (vertDiff > 0 || vertDiff < textPosition.getFont().getFontDescriptor().getDescent() * textPosition.getFontSizeInPt() / 500.0)
            return false;
        // Check whether rectangle is small enough to be a line
        return Math.abs(p2.getY() - p0.getY()) < 2;
    }
}
