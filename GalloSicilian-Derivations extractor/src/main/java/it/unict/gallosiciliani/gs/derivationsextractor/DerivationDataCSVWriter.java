package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.io.DerivationIOUtil;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.function.Consumer;

/**
 * Write the derivation data to CSV
 *
 * @author Cristiano Longo
 */
public class DerivationDataCSVWriter implements AutoCloseable, Consumer<DerivationExtData> {
    public static final String NOUN="nome";
    public static final String VERB="verbo";
    private final CSVPrinter printer;
    private final DerivationIOUtil derivationIOUtil=new DerivationIOUtil();

    @Getter
    private int printedRows=0;

    public DerivationDataCSVWriter(final Appendable out) throws IOException {
        printer=new CSVPrinter(out, CSVFormat.Builder.create().setHeader(DerivationDataCSVHeader.getHeaderRow()).build());
    }

    @Override
    public void close() throws IOException {
        printer.flush();
        printer.close();
    }

    @Override
    public void accept(DerivationExtData derivationData) {
        final int rowNum=++printedRows;
        try {
            printer.print(rowNum);
            printer.print(derivationData.getLemma());
            printer.print(derivationData.isNoun() ? NOUN : VERB);
            printer.print(derivationIOUtil.print(derivationData.getDerivation(), Locale.getDefault()));
            printer.print(asString(derivationData.getMissed()));
            printer.println();
        }catch (final IOException e){
            System.err.println("Unable to write row "+rowNum);
        }
    }

    /**
     * Create s string representation a set of {@link LinguisticPhenomenon} space-separated
     * @param phenomena a set of {@link it.unict.gallosiciliani.liph.LinguisticPhenomena}
     * @return a space-separated list of phenomena labels
     */
    private String asString(SortedSet<LinguisticPhenomenon> phenomena) {
        final StringBuilder b=new StringBuilder();
        final Iterator<LinguisticPhenomenon> it=phenomena.iterator();
        while(it.hasNext()) {
            b.append(it.next().getLabel());
            if (it.hasNext())
                b.append(" ");
        }
        return b.toString();
    }
}
