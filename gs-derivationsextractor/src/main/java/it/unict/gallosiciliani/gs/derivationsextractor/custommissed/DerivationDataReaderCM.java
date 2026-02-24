package it.unict.gallosiciliani.gs.derivationsextractor.custommissed;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.io.DerivationIOUtil;
import it.unict.gallosiciliani.derivations.io.DerivationParser;
import it.unict.gallosiciliani.gs.derivationsextractor.DerivationRawData;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Ontolex;
import it.unict.gallosiciliani.liph.model.lemonety.LemonEty;
import it.unict.gallosiciliani.liph.util.DerivationChainRetriever;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

/**
 * Produce {@link DerivationRawData} from suitable entries
 * in the knowledge base.
 *
 * @author Cristiano Longo
 */
public class DerivationDataReaderCM implements Iterator<DerivationRawDataCM> {

    private final Iterator<CSVRecord> records;
    private final LinguisticPhenomenonByLabelRetriever lpByLabel;
    private final DerivationParser derivationParser;

    public DerivationDataReaderCM(final CSVParser sourceParser, final LinguisticPhenomenonByLabelRetriever lpByLabel){
        this.records=sourceParser.iterator();
        this.lpByLabel=lpByLabel;
        derivationParser=new DerivationIOUtil().getParser(lpByLabel);
    }

    @Override
    public boolean hasNext() {
        return records.hasNext();
    }

    @Override
    public DerivationRawDataCM next() {
        final CSVRecord record=records.next();
        final DerivationPathNode d=derivationParser.parse(record.get(0).trim(), Locale.getDefault());
        check(d);
        final SortedSet<LinguisticPhenomenon> missed=parseMissed(record.get(1).trim());

        return new DerivationRawDataCM() {
            @Override
            public LexicalEntry getEntry() {
                return null;
            }

            @Override
            public DerivationPathNode getDerivationChain() {
                return d;
            }

            @Override
            public SortedSet<LinguisticPhenomenon> getMissed() {
                return missed;
            }
        };
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

    private SortedSet<LinguisticPhenomenon> parseMissed(final String missedStr){
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
