package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.derivations.DerivationPathNodeIterable;
import it.unict.gallosiciliani.gs.GSFeaturesCategory;
import it.unict.gallosiciliani.gs.GSFeaturesCategoryRetriever;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import lombok.Getter;

import java.util.*;

/**
 * All the information of interest concerning a single derivation. Perform all the required calculations
 * provided the raw data concerning an entry and the corresponding derivations
 *
 * @author Cristiano Longo
 */
public class DerivationExtData {

    @Getter
    private final String lemma;
    @Getter
    private final boolean noun;
    @Getter
    private final DerivationPathNode derivation;
    private final DerivationPhenomena phenomena;

    public DerivationExtData(final DerivationRawData src) {
        final LexicalEntry entry = src.getEntry();
        lemma = entry.getCanonicalForm().getWrittenRep().get();
        noun = LexInfo.NOUN_INDIVIDUAL.equals(entry.getPartOfSpeech().getId());
        derivation = derivationChain2PathNode(src);
        phenomena=new MissedPhenomenaFinder(src.getEligibleLinguisticPhenomena().getAll()).getMissedPhenomena(derivation, LinguisticPhenomena.COMPARATOR_BY_LABEL);


    }

    private DerivationPathNode derivationChain2PathNode(final DerivationRawData src) {
        final DerivationPathNode etymonPathNode=new DerivationPathNodeImpl(src.getEntry().getEtymology().iterator().next()
                .getStartingLink().getEtySubSource().iterator().next().getWrittenRep().get());
        return derivationChain2PathNode(src.getDerivationChain(), etymonPathNode);
    }

    /**
     * Convert a list of {@link LinguisticPhenomenonOccurrence} to a corresponding {@link DerivationPathNode}
     * @param derivationChain list of {@link LinguisticPhenomenonOccurrence}
     * @param etymonPathNode last derivation step
     * @return DerivationPathNode
     */
    private DerivationPathNode derivationChain2PathNode(final List<LinguisticPhenomenonOccurrence> derivationChain,
                                                        final DerivationPathNode etymonPathNode) {
        if (derivationChain.isEmpty()){
            return etymonPathNode;
        }
        final LinguisticPhenomenonOccurrence o=derivationChain.get(0);
        return new DerivationPathNodeImpl(o.getTarget().getWrittenRep().get(), o.getOccurrenceOf(),
                derivationChain2PathNode(derivationChain.subList(1, derivationChain.size()), etymonPathNode));
    }


    public SortedSet<LinguisticPhenomenon> getMissed(){
        return phenomena.getMissedPhenomena();
    }
    /**
     * features / (features + missed phenomena)
     * It does not apply to empty derivations
     * @return not available, if the derivation is empty. The GalloItalicity rate otherwise.
     */
    public Optional<Float> getGalloItalicityRate(){
        if (derivation.prev()==null) return Optional.empty();
        final float expected=phenomena.getSuitablePhenomena().size();
        final float missed=phenomena.getMissedPhenomena().size();
        return Optional.of(1f-(missed/expected));
    }

    /**
     * Retrieve all the categories of phenomena occurring in the derivation
     *
     * @param categoryRetriever all the categories
     * @return categories of phenomena occurring in the derivation
     */
    public Set<GSFeaturesCategory> getCategories(final GSFeaturesCategoryRetriever categoryRetriever){
        final Set<GSFeaturesCategory> res=new TreeSet<>(GSFeaturesCategory.COMPARATOR_BY_IRI);
        for(final DerivationPathNode n: new DerivationPathNodeIterable(derivation))
            if (n.getLinguisticPhenomenon()!=null)
                res.add(categoryRetriever.get(n.getLinguisticPhenomenon()));
        return res;
    }

    /**
     * Features as they occur in the derivation (from lemma to etymon)
     * @return {@link LinguisticPhenomenon} in the derivation
     */
    public List<LinguisticPhenomenon> getFeatures(){
        return getFeatures(derivation);
    }

    private List<LinguisticPhenomenon> getFeatures(final DerivationPathNode n){
        if (n.prev()==null)
            return new LinkedList<>();

        final List<LinguisticPhenomenon> res=getFeatures(n.prev());
        res.add(0, n.getLinguisticPhenomenon());
        return res;
    }
}
