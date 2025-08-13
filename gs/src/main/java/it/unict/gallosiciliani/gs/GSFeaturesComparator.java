package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import lombok.Data;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Gallo-Sicilian feature labels have the following form: category[.featureNumber]
 * where the second part is present only for executable features. This comparator provides an
 * ordering consistent with this form: first the macrofeature label is compared, then the featureNumber,
 * Here we are assuming the feature numbers are >0
 *
 * @author Cristiano Longo
 */
public class GSFeaturesComparator implements Comparator<HashedOntologyItem> {

    @Data
    private static class IdParts {
        /**
         * category label
         */
        private String category;


        /**
         * Relevant in the case of an executable feature.
         * Feature number, in the scope of the feature category
         */
        private int featureNumber;

        /**
         * Use this if the id has no feature number
         */
        static int NO_NUMBER=0;

        /**
         * The third of the id, when present
         */
        private String subFeature;

        /**
         * The third of the id, when present
         */
        private int subSubFeature;

    }

    @Override
    public int compare(final HashedOntologyItem p, HashedOntologyItem q) {
        final IdParts lp= getIdParts(p);
        final IdParts lq= getIdParts(q);
        final int categoryComparizon=lp.getCategory().compareTo(lq.getCategory());
        if (categoryComparizon!=0) return categoryComparizon;
        final int featureNumDiff=lp.getFeatureNumber()-lq.getFeatureNumber();
        if (featureNumDiff!=0) return featureNumDiff;
        final int subFeatureComparison=lp.getSubFeature().compareTo(lq.getSubFeature());
        if (subFeatureComparison!=0) return subFeatureComparison;
        return lp.getSubSubFeature()-lq.getSubSubFeature();
    }

    /**
     * Get parts of the feature identifier. If it is a category label, category number is set to 0
     * @param p the phenomenon
     * @return the corresponding label parts
     */
    private IdParts getIdParts(final HashedOntologyItem p){
        final IdParts l=new IdParts();
        final String[] parts=p.getId().split(Pattern.quote("."));
        l.setCategory(parts[0]);
        l.setFeatureNumber(parts.length>1 ? Integer.parseInt(parts[1]) : IdParts.NO_NUMBER);
        l.setSubFeature(parts.length>2 ? parts[2] : "");
        l.setSubSubFeature(parts.length>3 ? Integer.parseInt(parts[3]) : IdParts.NO_NUMBER);
        if (parts.length>4) throw new IllegalArgumentException("Found feature with more than 4 id parts "+p.getId());
        return l;
    }
}
