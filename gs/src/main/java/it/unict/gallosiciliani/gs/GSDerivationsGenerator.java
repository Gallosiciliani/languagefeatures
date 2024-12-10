package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GSDerivationsGenerator implements Consumer<CSVRecord>, AutoCloseable{

    private final GSFeatures gs;
    private final CSVPrinter printer;
    private final List<? extends LinguisticPhenomenon> phenomena;
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
        phenomena=RegexLinguisticPhenomenaReader.read(gs.getModel(), new RegexFeatureQuery().ignoreDeprecated()).getFeatures();
    }

    public static void main(final String[] args) throws IOException {
        //final String csvFilePath = args[1];
        final String csvFilePath = "testbed.csv";

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
        final String target = record.get(0);
        // Siciliano
        final String etymon = record.get(1);

        processed++;
        boolean complete = derive(etymon.trim(), target);
        if (complete) found++;
    }

    /**
     * Generate all the derivations starting from etymon and using GS features
     * @param etymon
     * @param target
     * @return true if a complete derivation has been found, false otherwise
     */
    private boolean derive(final String etymon, final String target){
        System.out.println("Processing "+etymon+" to "+target);
        final NearestShortestDerivation c=new NearestShortestDerivation(target);
        final DerivationBuilder b=new DerivationBuilder(phenomena, Collections.singletonList(c));
        b.apply(etymon);
        final BigDecimal distanceNormalized = BigDecimal.valueOf(c.getDistance()).divide(BigDecimal.valueOf(target.length()), new MathContext(2, RoundingMode.HALF_UP));

        for(final DerivationPathNode n : c.getDerivation()) {
            try {
                printer.printRecord(target, c.getDistance(), distanceNormalized, toString(n));
            } catch (IOException e) {
                throw new RuntimeException("unable to write");
            }
        }
        return c.getDistance()==0;
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
