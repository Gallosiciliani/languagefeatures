package it.unict.gallosiciliani.webapp.derivation;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.derivations.strategy.DerivationBuilderWithStrategy;
import it.unict.gallosiciliani.derivations.strategy.NearestStrategySelector;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategySelectorFactory;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.sicilian.SicilianVocabulary;
import it.unict.gallosiciliani.webapp.ontologies.LinguisticPhenomenaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Provide derivations using Gallo-Sicilian features
 *
 * @author Cristiano Longo
 */
@Service
public class DerivationService {

    private final TargetedDerivationStrategySelectorFactory selectorFactory= NearestStrategySelector.FACTORY;

    @Autowired
    LinguisticPhenomenaProvider lpProvider;

    @Autowired
    EntityManager entityManager;

    /**
     * Provide the derivations from etymon to target through Gallo-Sicilian features
     *
     * @param etymon etymon
     * @param lemma  derivation target
     * @return nearest and shortest derivations from the etymon to the target
     */
    public Collection<DerivationPathNode> derives(final String etymon, final String lemma){
        final Collection<DerivationPathNode> resExact=derivesExact(etymon, lemma);
        return resExact.isEmpty() ? derivesNearest(etymon, lemma) : resExact;
    }

    /**
     * Provide the derivations from etymon to target through Gallo-Sicilian features
     * @param etymon etymon
     * @param lemma derivation target
     * @return nearest and shortest derivations from the etymon to the target
     */
    public Collection<DerivationPathNode> derivesNearest(final String etymon, final String lemma){
        final NearestShortestDerivation consumer=new NearestShortestDerivation(lemma);
        getNearestDerivationBuilder(consumer).apply(etymon);
        return consumer.getDerivation();
    }

    /**
     * Provide the derivations from etymon to target through Gallo-Sicilian features
     * @param etymon etymon
     * @param lemma derivation target
     * @return shortest derivations from the etymon to the target
     */
    public Collection<DerivationPathNode> derivesExact(final String etymon, final String lemma){
        final BruteForceDerivationBuilder derivationBuilder=new BruteForceDerivationBuilder(lpProvider.getAll(), List.of(lemma));
        derivationBuilder.apply(etymon);
        return derivationBuilder.getDerivations();
    }

    public NearestShortestDerivation findSicilianEtymon(final String lemma) throws IOException {
        final NearestShortestDerivation consumer=new NearestShortestDerivation(lemma);
        final DerivationBuilder derivationBuilder=getNearestDerivationBuilder(consumer);
        SicilianVocabulary.visit(derivationBuilder::apply);
        return consumer;
    }


    private DerivationBuilder getNearestDerivationBuilder(final NearestShortestDerivation consumer){
        final TargetedDerivationStrategyFactory strategyFactory = new TargetedDerivationStrategyFactory(consumer, selectorFactory);
        return new DerivationBuilderWithStrategy(lpProvider.getAll(), strategyFactory);
//        return new BruteForceDerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), List.of(consumer.getTarget()));
    }

    /**
     * Retrieve from the knowledge base a list of {@link LinguisticPhenomenonOccurrence}
     * representing a chain of occurrences such that the first occurrence has the lemma as target and the last occurrence
     * has the etymon as source.
     *
     * @param lemma the lemma form
     * @param etymon the etymon form
     * @return a list of {@link LinguisticPhenomenonOccurrence} such that the source of each item is the target of the preceding one.
     */
    public List<LinguisticPhenomenonOccurrence> getDerivationChain(final Form lemma, final Form etymon){
        final DerivationChainRetriever r=new DerivationChainRetriever(lemma, etymon, entityManager);
        final List<LinguisticPhenomenonOccurrence> derivation=r.getOccurrencesSorted();
        setPhenomenaLabel(derivation);
        return derivation;
    }

    /**
     * This is a workaround due to the fact that JOPA is unable to retrieve individuals in imported ontologies
     * @param phenomena the penomena
     */
    private void setPhenomenaLabel(List<LinguisticPhenomenonOccurrence> phenomena) {
        for(final LinguisticPhenomenonOccurrence o: phenomena){
            final LinguisticPhenomenon p=o.getOccurrenceOf();
            p.setLabel(lpProvider.getById(p.getId()).getLabel());
        }
    }

}
