package it.unict.gallosiciliani.importing;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator.LexicalEntriesGenerator;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.Parser;
import it.unict.gallosiciliani.importing.sicilianvocab.SicilianVocabulary;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main implements Predicate<DerivationPathNode> {

    private int processedEntries=0;
    private long totalProcessingTime=0;
    private final List<RegexLinguisticPhenomenon> phenomena;
    private final Collection<NearestShortestDerivation> derivations;

    Main(final String pdfFilePath, final int startPage, final int endPage) throws IOException {
        derivations=importWholeDictionary(pdfFilePath, startPage, endPage, "nicosiasperlinga-lemmas.txt");
        try(final GSFeatures gs=GSFeatures.loadLocal()){
            phenomena=gs.getRegexNorthernItalyFeatures();
        }
    }

    private static Collection<NearestShortestDerivation> importWholeDictionary(final String pdfFilePath, final int startPage, final int endPage,
        final String outFilePath) throws IOException {
        final Collection<NearestShortestDerivation> emptyDerivations=new LinkedList<>();
        final Set<String> writtenRep = new TreeSet<>();
        final Set<String> formsIri = new TreeSet<>();
        final Set<String> duplicates = new TreeSet<>();
        try(final PrintStream out=new PrintStream(outFilePath)) {
            final Consumer<LexicalEntry> consumer = lexicalEntry -> {
                final Form form = lexicalEntry.getCanonicalForm();
                if (writtenRep.add(form.getWrittenRep()) != formsIri.add(form.getId())) {
                    duplicates.add(form.getWrittenRep());
                    System.err.println("Found duplicate for "+form.getWrittenRep());
                    //throw new IllegalArgumentException("Found duplicate form for "+form.getWrittenRep());
                }
                else emptyDerivations.add(new NearestShortestDerivation(form.getWrittenRep()));
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
        final Main m=new Main(args[0],121, 1084);
        SicilianVocabulary.visit(s -> {
            System.out.println(s);
            m.acceptSicilianVocabularyEntry(s);
        });
        try(final FileWriter w=new FileWriter("derivations.out")){
            m.writeNearestShortestDerivations(w);
        }
    }

    public void acceptSicilianVocabularyEntry(final String sicilianVocabularyEntry) {
        final long startTime=System.currentTimeMillis();
        new DerivationPathNodeImpl(sicilianVocabularyEntry).apply(derivations, phenomena);
        final long endTime=System.currentTimeMillis();
        final long elapsedTime=endTime-startTime;
        totalProcessingTime+=elapsedTime;
        System.out.println((processedEntries++)+": elapsed time "+elapsedTime+", total time "+totalProcessingTime);
    }



    @Override
    public boolean test(final DerivationPathNode derivationPathNode) {
        boolean notWorse=false;
        for(final NearestShortestDerivation d: derivations)
            if (d.test(derivationPathNode)) return true;
        return false;
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
}