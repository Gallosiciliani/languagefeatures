package it.unict.gallosiciliani.importing.nicosiasperlingavocab;

import java.util.Objects;

public abstract class VocabTestParams {

    // test PDF file with just some pages of the vocabulary
    public static VocabTestParams TEST_FILE = new VocabTestParams("nicosiasperlinga-120-126.pdf") {

        @Override
        public int getLastPage() {
            return 126;
        }

        @Override
        public int getPageNumInTestFile(final int pageNumInVocab) {
            return pageNumInVocab-119;
        }
    };

    // original vocabulary PDF file
    public static VocabTestParams VOCAB_FILE = new VocabTestParams("nicosiasperlinga.pdf") {

        @Override
        public int getLastPage() {
            return 1084;
        }

        @Override
        public int getPageNumInTestFile(final int pageNumInVocab) {
            return pageNumInVocab;
        }
    };

    private final String pdfFileName;

    /**
     * Create test params referring to a PDF file in the classpath
     * @param pdfFileName name of the PDF file
     */
    private VocabTestParams(final String pdfFileName){
        this.pdfFileName=pdfFileName;

    }

    /**
     * Path of the PDF file which has to be parsed
     */
    public final String getPdfFilePath(){
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(classloader.getResource(pdfFileName)).toString().substring("file:".length());
    }

    /**
     * Get the first vocabulary page number reported in the PDF file
     * @return first page of the vocabulary reported in the test PDF file
     */
    public final int getFirstPage(){
        return 121;
    }

    /**
     * Get the last vocabulary page number reported in the PDF file
     *
     * @return last page of the vocabulary reported in the test PDF file
     */
    public abstract int getLastPage();

    /**
     * Get the page number in the test file corresponding to the one in the original vocabulary
     * @param pageNumInVocab page number in the vocabulary
     * @return corresponding page number in the test PDF file
     */
    public abstract int getPageNumInTestFile(final int pageNumInVocab);
}
