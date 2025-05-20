package it.unict.gallosiciliani.sicilian;

import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Attempt to find the Sicilian etymons for lemmas in the Gallo-Italic variety of Nicosia e Sperlinga
 */
public class SicilianToNicosiaESperlingaDerivations implements Consumer<String> {

    private final TonicVowelAccentExplicitor accentExplicitor=new TonicVowelAccentExplicitor();

    private final BruteForceDerivationBuilder derivationBuilder;
    private int processedEntries=0;
    private long totalProcessingTime=0;

    SicilianToNicosiaESperlingaDerivations() throws IOException {
        try (final GSFeatures gs = new GSFeatures(); final NicosiaESperlinga nicosiaESperlinga=new NicosiaESperlinga()) {
            //derivations=nicosiaESperlinga.getAllForms().map((f)->new NearestShortestDerivation(f.getWrittenRep())).toList();
            final List<String> lemmas=nicosiaESperlinga.getAllForms()
                    .map((f)->{return f.getWrittenRep().get();})
                    .map(accentExplicitor::addGraveAccent).toList();
            derivationBuilder=new BruteForceDerivationBuilder(gs.getRegexLinguisticPhenomena(), lemmas);
//                    derivationBuilderFactory.build(gs.getRegexLinguisticPhenomena(), lemmas);
        }
    }

    @Override
    public void accept(final String sicilianVocabularyEntry) {
        final int numwords=sicilianVocabularyEntry.split("\\s").length;
        if (numwords>1){
            System.err.println("IGNORED "+sicilianVocabularyEntry);
            return;
        }

        System.out.println((processedEntries)+" Processing "+sicilianVocabularyEntry);
        final long startTime = System.currentTimeMillis();
        final String sicilianVocabularyEntryWithAccent = accentExplicitor.addGraveAccent(sicilianVocabularyEntry);
        derivationBuilder.apply(sicilianVocabularyEntryWithAccent);
        final long endTime = System.currentTimeMillis();
        final long elapsedTime = endTime - startTime;
        totalProcessingTime += elapsedTime;
        System.out.println((processedEntries++) + " " + sicilianVocabularyEntry + "(" + sicilianVocabularyEntryWithAccent + "): elapsed time " + elapsedTime + ", total time " + totalProcessingTime + ".");
    }

    /**
     * write a set of rows representing the current derivations for the Nicosia e Sperilnga's lemmas.
     *
     * @param out the output stream
     * @throws IOException if unable to write to the output stream
     */
    public int writeNearestShortestDerivations(final Appendable out) throws IOException {
        return derivationBuilder.write(out, GSFeatures.LABEL_PROVIDER_ID, Locale.ENGLISH);
//        final DerivationIOUtil derivationPrinter=new DerivationIOUtil(GSFeatures.LABEL_PROVIDER_ID);
//        try(final CSVPrinter printer=new CSVPrinter(out, CSVFormat.DEFAULT)) {
//            for (final NearestShortestDerivation nearest : derivations) {
//                final BigDecimal distanceNormalized = BigDecimal.valueOf(nearest.getDistance()).divide(BigDecimal.valueOf(nearest.getTarget().length()), new MathContext(2, RoundingMode.HALF_UP));
//
//                for (final DerivationPathNode n : nearest.getDerivation())
//                    printer.printRecord(nearest.getTarget(), nearest.getDistance(), distanceNormalized, derivationPrinter.print(n, Locale.ENGLISH));
//            }
//        }
    }

    public static void main(final String[] args) throws IOException {
        final SicilianToNicosiaESperlingaDerivations d=new SicilianToNicosiaESperlingaDerivations();
        SicilianVocabulary.visit(d);
        try(final FileWriter w=new FileWriter(args[0])){
            final int n=d.writeNearestShortestDerivations(w);
            System.out.println("Processed "+d.processedEntries+" entries");
            System.out.println("Total processing time "+d.totalProcessingTime);
            System.out.println("Found etymon for "+n+" lemmas");
        }
    }

}
