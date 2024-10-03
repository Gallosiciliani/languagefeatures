package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.NearestDerivation;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Consumer;

public class GSDerivationsGenerator implements Consumer<CSVRecord>, AutoCloseable{

    private final GSFeatures gs;
    private final CSVPrinter printer;

    @Getter
    private int processed;

    @Getter
    private int found;

    /**
     *
     * @param out where the resulting CSV rows will be appended
     *
     * @throws IOException on I/O error
     */
    GSDerivationsGenerator(final Appendable out) throws IOException {
        gs = GSFeatures.loadLocal();
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    public static void main(final String[] args) throws IOException {
        //final String csvFilePath = args[1];
        final String csvFilePath = "nicosia.csv";

        System.out.println("Loading lexemes and etymons from "+csvFilePath);
        try (final FileReader csvReader = new FileReader(csvFilePath);
             final FileWriter writer = new FileWriter("derivations.csv");
             final GSDerivationsGenerator generator = new GSDerivationsGenerator(writer)) {
            CSVParser.parse(csvReader, CSVFormat.DEFAULT.builder().setSkipHeaderRecord(true).build()).forEach(generator);
            System.out.println("Processed "+generator.getProcessed()+" pairs");
            System.out.println(generator.getFound()+" complete derivations found");
        }

    }


    @Override
    public void accept(final CSVRecord record) {
        final String target = record.get(1);
        if (target.equals("Lemma")) return; //header
        final String etymon = record.get(6);
        if (etymon.trim().isEmpty()) return;

        processed++;
        System.out.println("Processing "+etymon+" to "+target);
        final NearestShortestDerivation nearest = gs.derives(etymon, target);
        if (nearest.getDistance()==0) found++;
        final BigDecimal distanceNormalized = BigDecimal.valueOf(nearest.getDistance()).divide(BigDecimal.valueOf(target.length()), new MathContext(2, RoundingMode.HALF_UP));

        for(final DerivationPathNode n : nearest.getDerivation()) {
            try {
                printer.printRecord(target, nearest.getDistance(), distanceNormalized, toString(n));
            } catch (IOException e) {
                throw new RuntimeException("unable to write");
            }
        }
    }

    /**
     * Print the derivations chain ending with this node
     *
     * @param n derivation path node
     * @return a string el <-- phenomenon -- el ... el <-- phenomenon -- el
     */
    private String toString(final DerivationPathNode n){
        if (n.prev()==null)
            return n.get();
        //here we are assuming that the phenomenon is in GSFeatures
        final String phenomenonLabel = n.getLinguisticPhenomenon().getIRI().substring(GSFeatures.NS.length());
        return n.get()+"<-"+phenomenonLabel+"--"+toString(n.prev());
    }

    @Override
    public void close() throws IOException {
        gs.close();
        printer.close(true);
    }
}
