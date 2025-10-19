package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.io.DerivationIOUtil;
import it.unict.gallosiciliani.derivations.io.DerivationParser;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Create a {@link MissedPhenomenaProvider} by reading missed phenomena from a CSV file containing both the derivation and the missed phenomena.
 */
public class MissedPhenomenaProviderFromCSVFactory {

    public MissedPhenomenaProvider build(final String inputCSVFile, final LinguisticPhenomenonByLabelRetriever lpByLabel) throws IOException {
        final Map<String, SortedSet<LinguisticPhenomenon>> lemmaToMissed=new TreeMap<>();
        final DerivationParser derivationParser=new DerivationIOUtil().getParser(lpByLabel);
        try(final CSVParser sourceParser=CSVParser.parse(new File(inputCSVFile), StandardCharsets.UTF_8, CSVFormat.DEFAULT)){
            for(final CSVRecord record: sourceParser){
                final DerivationPathNode d=derivationParser.parse(record.get(0).trim(), Locale.getDefault());
                check(d);
                final SortedSet<LinguisticPhenomenon> missed=parseMissed(record.get(1).trim(), lpByLabel);
                if (lemmaToMissed.put(d.get(), missed)!=null)
                    throw new IllegalArgumentException("Duplicate missed phenomena for lemma "+d.get());
            }
        }
        return lemmaToMissed::get;
    }

    private void check(final DerivationPathNode d){
        if (d.prev()==null) return;
        final String target=d.get();
        final String src=d.prev().get();
        if (d.getLinguisticPhenomenon().apply(src).contains(target))
            check(d.prev());
        else
            throw new IllegalArgumentException("Invalid derivation "+target+"<-"+d.getLinguisticPhenomenon().getLabel()+"--"+src);
    }

    private SortedSet<LinguisticPhenomenon> parseMissed(final String missedStr, final LinguisticPhenomenonByLabelRetriever lpByLabel){
        final SortedSet<LinguisticPhenomenon> missed=new TreeSet<>(LinguisticPhenomena.COMPARATOR_BY_IRI);
        for(final String l: missedStr.split(" "))
            if (!l.isBlank()) {
                final LinguisticPhenomenon p = lpByLabel.getByLabel(l, Locale.getDefault());
                if (p == null)
                    throw new IllegalArgumentException("Unable to find missed linguistic phenomenon with label \"" + l + "\"");
                missed.add(p);
            }
        return missed;
    }

}
