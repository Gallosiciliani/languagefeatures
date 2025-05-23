package it.unict.gallosiciliani.pdfimporter.parser;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Detect underlined z characters
 */
class UnderlinedZDetector extends PDFGraphicsStreamEngine {

    private final GeneralPath currentPath=new GeneralPath();
    private final List<TransformedRectangle> rectangles=new ArrayList<>();

    UnderlinedZDetector(PDPage page) {
        super(page);
    }

    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3){
        //do nothing
    }

    @Override
    public void drawImage(PDImage pdImage){
        //do nothing
    }

    @Override
    public void clip(int i){
        //do nothing
    }

    @Override
    public void moveTo(float x, float y){
        currentPath.moveTo(x,y);
    }

    @Override
    public void lineTo(float x, float y){
        Point2D.Float endPoint=transformedPoint(x,y);
        rectangles.add(new TransformedRectangle(currentPath.getCurrentPoint(), endPoint));
        currentPath.lineTo(x,y);
    }

    @Override
    public void curveTo(float v, float v1, float v2, float v3, float v4, float v5){
        //do nothing
    }

    @Override
    public Point2D getCurrentPoint(){
        return currentPath.getCurrentPoint();
    }

    @Override
    public void closePath(){
        currentPath.closePath();
        //do nothing
    }

    @Override
    public void endPath(){
        //do nothing
    }

    @Override
    public void strokePath(){
        //do nothing
    }

    @Override
    public void fillPath(int i){
        //do nothing
    }

    @Override
    public void fillAndStrokePath(int i){
        //do nothing
    }

    @Override
    public void shadingFill(COSName cosName){
        //do nothing
    }

    /**
     * Perform processing
     * @throws IOException error reading the page
     */
    void processPage() throws IOException {
        processPage(getPage());
    }

    private boolean isUnderlined(final TextPosition text){
        return rectangles.stream().anyMatch(p->p.underlines(text));
    }

    /**
     * Get the character represented in the text position. If it is z, check if it should be underlined and, in case, return
     * the underlined version
     * @param text character extracted from page
     * @return the Unicode character, eventually with underscore
     */
    String getUnicode(final TextPosition text){
        final String o=text.getUnicode();
        return "z".equals(o) && isUnderlined(text) ? "ẕ" : o;
    }
}
