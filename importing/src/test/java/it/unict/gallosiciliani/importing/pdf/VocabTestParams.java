package it.unict.gallosiciliani.importing.pdf;

import java.util.Objects;

/**
 * Helper class for using files obtained by extracting some pages from the original
 * vocabulary.
 */
public class VocabTestParams {
    
    // test PDF file with just some pages of the vocabulary
    public static VocabTestParams TEST_120_126 = new VocabTestParams("nicosiasperlinga-120-126.pdf", 120);
    public static VocabTestParams TEST_514 = new VocabTestParams("nicosiaesperlinga-vocab-514.pdf", 514);

    // original vocabulary PDF file
    public static VocabTestParams VOCAB_FILE = new VocabTestParams("nicosiasperlinga.pdf", 1);

    private final String pdfFileName;
    private final int offset;

    /**
     * Create test params referring to a PDF file in the classpath
     * @param pdfFileName name of the PDF file
     * @param firstPageInFile number (starting from 1) of the first page in the original vocabulary
     *tera                        that have been reported in the pdfFile
     */
    public VocabTestParams(final String pdfFileName, final int firstPageInFile){
        this.pdfFileName=pdfFileName;
        this.offset=firstPageInFile-1;

    }

    /**
     * Path of the PDF file which has to be parsed
     */
    public final String getPdfFilePath(){
        return getPdfFilePath(pdfFileName);
    }

    /**
     * Path of the PDF file which has to be parsed
     */
    public static String getPdfFilePath(final String pdfFileName){
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(pdfFileName)).toString().substring("file:".length());
    }
    
    /**
     * Get the page number in the test file corresponding to the one in the original vocabulary
     * @param pageNumInVocab page number in the vocabulary
     * @return corresponding page number in the test PDF file, but starting from 0
     */
    public int getPageNumInTestFile(final int pageNumInVocab){
        return pageNumInVocab-offset;
    }
}
