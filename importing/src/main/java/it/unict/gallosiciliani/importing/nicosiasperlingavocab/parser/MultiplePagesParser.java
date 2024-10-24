package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

import java.io.IOException;

/**
 * Helper class to parse several pages once.
 * 
 * @author Cristiano Longo
 */
public class MultiplePagesParser {

    private final Parser delegate;

    public MultiplePagesParser(final Parser delegate){
        this.delegate=delegate;
    }

    /**
     * Parse and consume all pages in the specified range
     * @param startPage first page to be consumed
     * @param endPage last page to be consumed
     * @throws IOException if an error occurs reading the document
     */
    public void parsePages(final int startPage, final int endPage) throws IOException {
        for(int i=startPage; i<=endPage; i++) {
            System.out.println(i);
            delegate.parsePage(i);
        }
    }
}
