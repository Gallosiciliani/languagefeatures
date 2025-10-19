package it.unict.gallosiciliani.gs.derivationsextractor.custommissed;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.gs.GSFeaturesCategory;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.Getter;

import java.io.IOException;
import java.util.*;

/**
 * Provide GS Features and some phenomena defined just to be used in the missed field
 * @author Cristiano Longo
 */
public class ExtendedGSPhenomena implements LinguisticPhenomenonByLabelRetriever, AutoCloseable {

    private static class VacuousPhenomenon extends LinguisticPhenomenon{
        VacuousPhenomenon(final String label){
            setId(GSFeatures.NS+label);
            setLabel(label);
            setComment("Vacuous phenomenon "+label);
        }

        public Set<String> apply(String src){
            return Collections.emptySet();
        }
    }

    private final GSFeatures gs;
    private final Collection<LinguisticPhenomenon> vacuousAdditionalGSPhenomena;
    @Getter
    private final LinguisticPhenomenaProvider lpProvider;

    ExtendedGSPhenomena() throws IOException {
        this.gs=new GSFeatures();
        vacuousAdditionalGSPhenomena=new ArrayList<>();
        vacuousAdditionalGSPhenomena.add(createVacuousAdditionalGSPhenomenon("assib.6.d"));
        vacuousAdditionalGSPhenomena.add(createVacuousAdditionalGSPhenomenon("vocal.11"));
        final List<LinguisticPhenomenon> allPhenomena=new LinkedList<>(gs.getRegexLinguisticPhenomena());
        allPhenomena.addAll(vacuousAdditionalGSPhenomena);
        lpProvider=new LinguisticPhenomenaProvider(allPhenomena);
    }

    private static LinguisticPhenomenon createVacuousAdditionalGSPhenomenon(final String label){
        return new VacuousPhenomenon(label);
    }

    @Override
    public LinguisticPhenomenon getByLabel(String label, Locale locale) {
        final LinguisticPhenomenon p=gs.getByLabel(label, locale);
        if (p!=null) return p;
        for(final LinguisticPhenomenon q: vacuousAdditionalGSPhenomena)
            if (q.getLabel().equals(label))
                return q;
        return null;
    }

    public List<GSFeaturesCategory> getCategories(){
        return gs.getCategories();
    }

    @Override
    public void close(){
        gs.close();
    }
}
