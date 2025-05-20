package it.unict.gallosiciliani.importing;

import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.derivations.strategy.CompoundDerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.NearestStrategySelector;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.importing.pdf.generator.LexicalEntriesGenerator;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.pdf.parser.Parser;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.sicilian.SicilianVocabulary;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    private int processedEntries=0;
    private long totalProcessingTime=0;
    private final List<NearestShortestDerivation> derivations;
    private final DerivationBuilder derivationBuilder;
//    private final DerivationBuilderWithStrategy derivationBuilder;

    Main(final String pdfFilePath, final int startPage, final int endPage) throws IOException {
        derivations=importWholeDictionary(pdfFilePath, startPage, endPage, "nicosiasperlinga-lemmas.txt");
        try(final GSFeatures gs=new GSFeatures()){
            final List<LinguisticPhenomenon> phenomena=gs.getRegexLinguisticPhenomena();
            derivationBuilder=new DerivationBuilderWithStrategy(phenomena, new CompoundDerivationStrategyFactory(derivations, NearestStrategySelector.FACTORY));
        }
    }

    Main(final String entriesFilePath) throws IOException {
        derivations=importDictionaryEntriesFromFile(entriesFilePath);
        try(final GSFeatures gs=new GSFeatures()){
            final List<LinguisticPhenomenon> phenomena=gs.getRegexLinguisticPhenomena();
            derivationBuilder=new DerivationBuilderWithStrategy(phenomena, new CompoundDerivationStrategyFactory(derivations, NearestStrategySelector.FACTORY));
        }
    }

    private static List<NearestShortestDerivation> importDictionaryEntriesFromFile(final String entriesFilePath) throws IOException {
        final List<NearestShortestDerivation> emptyDerivations=new LinkedList<>();
        try(final BufferedReader reader=new BufferedReader(new FileReader(entriesFilePath))){
            reader.lines().forEach((lemma)->emptyDerivations.add(new NearestShortestDerivation(lemma)));
            return emptyDerivations;
        }
    }

    private static List<NearestShortestDerivation> importWholeDictionary(final String pdfFilePath, final int startPage, final int endPage,
                                                                         final String outFilePath) throws IOException {
        final List<NearestShortestDerivation> emptyDerivations=new LinkedList<>();
        final Set<String> writtenRep = new TreeSet<>();
        final Set<String> formsIri = new TreeSet<>();
        final Set<String> duplicates = new TreeSet<>();
        try(final PrintStream out=new PrintStream(outFilePath)) {
            final Consumer<LexicalEntry> consumer = lexicalEntry -> {
                final Form form = lexicalEntry.getCanonicalForm();
                if (writtenRep.add(form.getWrittenRep().get()) != formsIri.add(form.getId())) {
                    duplicates.add(form.getWrittenRep().get());
                    System.err.println("Found duplicate for "+form.getWrittenRep());
                    //throw new IllegalArgumentException("Found duplicate form for "+form.getWrittenRep());
                }
                else emptyDerivations.add(new NearestShortestDerivation(form.getWrittenRep().get()));
                out.println(form.getWrittenRep());
            };
            final LexicalEntriesGenerator generator = new LexicalEntriesGenerator(consumer, "http://localhost/nicosiasperlinga#", new POSIndividualProvider());
            try(final Parser parser = new Parser(generator, pdfFilePath)) {
                for(int i=startPage; i<=endPage; i++)
                    parser.parsePage(i);
                System.out.println("Found " + writtenRep.size() + " forms");
                System.out.println("Duplicates "+duplicates);
                return emptyDerivations;
            }
        }
    }

    public void writeNearestShortestDerivations(final Appendable out) throws IOException {
        try(final CSVPrinter printer=new CSVPrinter(out, CSVFormat.DEFAULT)) {
            for (final NearestShortestDerivation nearest : derivations) {
                final BigDecimal distanceNormalized = BigDecimal.valueOf(nearest.getDistance()).divide(BigDecimal.valueOf(nearest.getTarget().length()), new MathContext(2, RoundingMode.HALF_UP));

                for (final DerivationPathNode n : nearest.getDerivation())
                    printer.printRecord(nearest.getTarget(), nearest.getDistance(), distanceNormalized, toString(n));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final Main m=args[0].endsWith(".pdf") ? new Main(args[0],121, 1084) : new Main(args[0]);
        SicilianVocabulary.visit(s -> {
            System.out.println(s);
            m.acceptSicilianVocabularyEntry(s);
        });
        try(final FileWriter w=new FileWriter("derivations.csv")){
            m.writeNearestShortestDerivations(w);
        }
        System.out.println("Processed "+m.processedEntries+" entries");
        System.out.println("Total processing time "+m.totalProcessingTime);
    }

    public void acceptSicilianVocabularyEntry(final String sicilianVocabularyEntry) {
        final long startTime=System.currentTimeMillis();
        derivationBuilder.apply(sicilianVocabularyEntry);
        final long endTime=System.currentTimeMillis();
        final long elapsedTime=endTime-startTime;
        totalProcessingTime+=elapsedTime;
        System.out.println((processedEntries++)+": elapsed time "+elapsedTime+", total time "+totalProcessingTime+".");
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
        final String phenomenonLabel = n.getLinguisticPhenomenon().getId().substring(GSFeatures.NS.length());
        return n.get()+"<-"+phenomenonLabel+"--"+toString(n.prev());
    }
}