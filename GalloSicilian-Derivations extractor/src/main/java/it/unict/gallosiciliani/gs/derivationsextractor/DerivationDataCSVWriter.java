package it.unict.gallosiciliani.gs.derivationsextractor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Write the derivation data to CSV
 *
 * @author Cristiano Longo
 */
public class DerivationDataCSVWriter implements AutoCloseable, Consumer<DerivationData> {
    private final CSVPrinter printer;

    public DerivationDataCSVWriter(final Appendable out) throws IOException {
        printer=new CSVPrinter(out, CSVFormat.Builder.create().setHeader(DerivationDataCSVHeader.getHeaderRow()).build());
    }

    @Override
    public void close() throws IOException {
        printer.flush();
        printer.close();
    }

    @Override
    public void accept(DerivationData derivationData) {
        try {
            printer.print(Integer.toString(derivationData.getRowNumber()));
            printer.println();
        }catch (final IOException e){
            System.err.println("Unable to write row "+derivationData.getRowNumber());
        }
    }
}
