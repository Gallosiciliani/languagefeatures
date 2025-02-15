package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.derivations.strategy.NotFartherStrategySelector;
import it.unict.gallosiciliani.derivations.strategy.TargetedDerivationStrategyFactory;
import it.unict.gallosiciliani.gs.GSFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provide derivations using Gallo-Sicilian features
 *
 * @author Cristiano Longo
 */
@Service
public class DerivationService {

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
        final TargetedDerivationStrategyFactory strategyFactory=new TargetedDerivationStrategyFactory(consumer, NotFartherStrategySelector.FACTORY);
        new DerivationBuilder(gsFeatures.getRegexLinguisticPhenomena(), strategyFactory).apply(etymon);
        return consumer;
    }
}
