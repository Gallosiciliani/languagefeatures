package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.io.DerivationIOUtil;
import it.unict.gallosiciliani.gs.GSFeaturesCategory;
import it.unict.gallosiciliani.gs.GSFeaturesCategoryRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Write the derivation data to CSV
 *
 * @author Cristiano Longo
 */
public class DerivationDataCSVWriter implements AutoCloseable, Consumer<DerivationExtData> {
    public static final String NOUN="nome";
    public static final String VERB="verbo";
    public static final String NA="";
    public static final String YES="sì";
    public static final String NO="no";

    private final CSVPrinter printer;
    private final DerivationIOUtil derivationIOUtil=new DerivationIOUtil();
    private final List<GSFeaturesCategory> categories;
    private final GSFeaturesCategoryRetriever categoryRetriever;

    @Getter
    private int printedRows=0;

    public DerivationDataCSVWriter(final Appendable out, final List<GSFeaturesCategory> categories) throws IOException {
        printer=new CSVPrinter(out, CSVFormat.Builder.create().setHeader(DerivationDataCSVHeader.getHeaderRow(categories)).build());
        this.categories=categories;
        categoryRetriever=new GSFeaturesCategoryRetriever(categories);
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
            final Optional<Float> rate=derivationData.getGalloItalicityRate();
            printer.print(rate.map(aFloat -> String.format("%.3f", aFloat)).orElse(NA));
            print(derivationData.getCategories(categoryRetriever));
            print(derivationData.getFeatures());
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

    /**
     * Print category fields
     * @param foundCategories all categories of features in the derivation
     */
    private void print(final Set<GSFeaturesCategory> foundCategories) throws IOException {
        for(final GSFeaturesCategory c: categories)
            printer.print(foundCategories.contains(c)?YES:NO);
    }

    private void print(final List<LinguisticPhenomenon> phenomena) throws IOException {
        int i=0;
        for(final LinguisticPhenomenon p: phenomena){
            printer.print(p.getLabel());
            i++;
        }
        for(int j=i; j<DerivationDataCSVHeader.FEATURE_HEADERS.length; j++)
            printer.print(NA);
    }
}
