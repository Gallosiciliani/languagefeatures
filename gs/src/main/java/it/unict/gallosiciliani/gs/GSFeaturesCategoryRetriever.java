package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.util.OntologyItem;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reverse map which associates each {@link it.unict.gallosiciliani.liph.model.LinguisticPhenomenon} to
 * the corresponding {@link it.unict.gallosiciliani.gs.GSFeaturesCategory}
 */
public class GSFeaturesCategoryRetriever {

    private final Map<String, GSFeaturesCategory> phenomenonIRIToCategory=new TreeMap<>();

    public GSFeaturesCategoryRetriever(final Collection<GSFeaturesCategory> allCategories){
        for(final GSFeaturesCategory c: allCategories)
            for(final OntologyItem p: c.getMembers())
                if (phenomenonIRIToCategory.put(p.getIri(), c)!=null)
                    throw new IllegalArgumentException("Multiple categories for phenomenon "+p.getIri());
    }
    /**
     * Get the category this linguistic phenomenon belongs to
     * @param p a {@link LinguisticPhenomenon}
     * @return the category this linguistic phenomenon belongs to, if any. Null, otherwise.
     */
    public GSFeaturesCategory get(final LinguisticPhenomenon p){
        return phenomenonIRIToCategory.get(p.getId());
    }
}
