package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.derivations.strategy.*;
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
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class GSDerivationsGenerator implements Consumer<CSVRecord>, AutoCloseable{

    private final GSFeatures gs;
    private final CSVPrinter printer;
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final TargetedDerivationStrategySelectorFactory selectorFactory;
    private final DerivationPrinter derivationPrinter;
    @Getter
    private int processed;

    @Getter
    private int found;

    /**
     * @param out             where the resulting CSV rows will be appended
     * @param selectorFactory factory that influences for derivation strategies
     * @throws IOException on I/O error
     */
    GSDerivationsGenerator(final Appendable out, final TargetedDerivationStrategySelectorFactory selectorFactory) throws IOException {
        gs = GSFeatures.loadLocal();
        printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        phenomena=RegexLinguisticPhenomenaReader.read(gs.getModel(), new RegexFeatureQuery().ignoreDeprecated()).getFeatures();
        this.selectorFactory=selectorFactory;
        derivationPrinter=new DerivationPrinter(GSFeatures.LABEL_PROVIDER_ID);
    }

    public static void main(final String[] args) throws IOException {
        final String srcFilePath = args[0];
        final String dstFilePath = args[1];
//        final String csvFilePath = "testbed.csv";
        final TargetedDerivationStrategySelectorFactory strategyFactory=args.length>2 && "fast".equals(args[2]) ?
                NearestStrategySelector.FACTORY : NotFartherStrategySelector.FACTORY;

        System.out.println("Loading lexemes and etymons from "+srcFilePath);
        try (final FileReader csvReader = new FileReader(srcFilePath);
             final FileWriter writer = new FileWriter(dstFilePath);
             final GSDerivationsGenerator generator = new GSDerivationsGenerator(writer, strategyFactory)) {
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
     * @param etymon candidate etymon
     * @param target expected final lexical expression
     * @return true if a complete derivation has been found, false otherwise
     */
    private boolean derive(final String etymon, final String target){
        System.out.println("Processing "+etymon+" to "+target);
        final NearestShortestDerivation c=new NearestShortestDerivation(target);
        final DerivationStrategyFactory strategyFactory=new DerivationStrategyFactory() {
            @Override
            public DerivationStrategy build(final DerivationPathNode initialDerivation) {
                return new TargetedDerivationStrategy(initialDerivation,c,selectorFactory);
            }
        };
        final DerivationBuilder b=new DerivationBuilder(phenomena, strategyFactory);
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
        return derivationPrinter.print(n, Locale.ENGLISH);
    }

    @Override
    public void close() throws IOException {
        gs.close();
        printer.close(true);
    }
}
