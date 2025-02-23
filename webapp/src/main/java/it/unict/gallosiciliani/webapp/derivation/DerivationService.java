package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.derivations.strategy.NearestStrategySelector;
import it.unict.gallosiciliani.derivations.strategy.NotFartherStrategySelector;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategyFactory;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategySelectorFactory;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.sicilian.SicilianVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Provide derivations using Gallo-Sicilian features
 *
 * @author Cristiano Longo
 */
@Service
public class DerivationService {

    private final TargetedDerivationStrategySelectorFactory selectorFactory=NotFartherStrategySelector.FACTORY;

    @Autowired
    GSFeatures gsFeatures;

    /**
     * Provide the derivations from etymon to target through Gallo-Sicilian features
     * @param etymon etymon
     * @param target derivation target
     * @return nearest and shortest derivations from the etymon to the target
     */
    public NearestShortestDerivation derives(final String etymon, final String target){
        final NearestShortestDerivation consumer=new NearestShortestDerivation(target);
        final TargetedDerivationStrategyFactory strategyFactory = new TargetedDerivationStrategyFactory(consumer, selectorFactory);
        new DerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), strategyFactory).apply(etymon);
        return consumer;
    }

    public NearestShortestDerivation findSicilianEtymon(final String lemma) throws IOException {
        final NearestShortestDerivation consumer=new NearestShortestDerivation(lemma);
        final TargetedDerivationStrategyFactory strategyFactory = new TargetedDerivationStrategyFactory(consumer, NearestStrategySelector.FACTORY);
        final DerivationBuilder derivationBuilder=new DerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), strategyFactory);
        SicilianVocabulary.visit(derivationBuilder::apply);
        return consumer;
    }
}
