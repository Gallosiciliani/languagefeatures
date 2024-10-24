package it.unict.gallosiciliani.importing.nicosiasperlingavocab.writing;

import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.MultiplePagesParser;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.Parser;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.ParsingDataConsumer;
import it.unict.gallosiciliani.importing.partofspeech.POS;
import it.unict.gallosiciliani.importing.partofspeech.UnexpectedPOSStringException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * Get all POS strings from a parser and write them down to a CSV file where
 * the first column is the POS string and the second one is the assigned POS value
 */
public class POStoCSVwriter implements ParsingDataConsumer{
    private final Set<String> processed=new TreeSet<>();
    private final CSVPrinter printer;

    /**
     * Create a writer that will accept tokens
     * and will write them to the given output
     *
     * @param output where POS will be written
     */
    public POStoCSVwriter(final Appendable output) throws IOException {
        this.printer=new CSVPrinter(output, CSVFormat.DEFAULT);
    }

    @Override
    public void lemma(String lemma) {
        // do nothing
    }

    @Override
    public void pos(final String pos) {
        if (processed.add(pos))
            try{
                try {
                    printer.printRecord(pos,POS.get(pos),"");
                } catch (final UnexpectedPOSStringException e) {
                    printer.printRecord(pos, e.getSuggestedPOS(),"unexpected");
                }
            } catch (final IOException e) {
                throw new RuntimeException("Unable to write ",e);
            }
    }

    @Override
    public void conjunction() {
        // do nothing
    }

    public static void main(final String[] args) throws IOException {
        try(final PrintStream posCSV=new PrintStream("pos.csv")) {
            final POStoCSVwriter w = new POStoCSVwriter(posCSV);
            try (final Parser parser = new Parser(w, "nicosiasperlinga.pdf")) {
                new MultiplePagesParser(parser).parsePages(1, 1084);
            }
        }
    }
}
