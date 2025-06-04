package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.*;
import it.unict.gallosiciliani.derivations.strategy.DerivationBuilderWithStrategy;
import it.unict.gallosiciliani.derivations.strategy.NearestStrategySelector;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategySelectorFactory;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.sicilian.SicilianVocabulary;
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
    GSFeatures gsFeatures;

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
        final BruteForceDerivationBuilder derivationBuilder=new BruteForceDerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), List.of(lemma));
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
        return new DerivationBuilderWithStrategy(gsFeatures.getRegexLinguisticPhenomena(), strategyFactory);
//        return new BruteForceDerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), List.of(consumer.getTarget()));
    }
}
