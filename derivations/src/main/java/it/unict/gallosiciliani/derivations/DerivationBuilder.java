package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
public class DerivationBuilder {
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final Collection<? extends DerivationsToTargetContainer> consumers;

    public DerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, final Collection<? extends DerivationsToTargetContainer> consumers){
        this.phenomena=phenomena;
        this.consumers=consumers;
    }

    public void apply(final String src){
        final DerivationPathNode rootNode=new DerivationPathNodeImpl(src);
        final DerivationStrategy strategy=new DerivationsToMultipleTargetsStrategy(rootNode, consumers);
        apply(strategy, phenomena);
    }

    private void apply(final DerivationStrategy strategy, final List<? extends LinguisticPhenomenon> phenomena){
        if (phenomena.isEmpty()){
            strategy.end();
            return;
        }
        final LinguisticPhenomenon currentPhenomenon=phenomena.get(0);
        //notice that the following list is backed by the original one
        final List<? extends LinguisticPhenomenon> remainingPhenomena= phenomena.size()>1 ?
                phenomena.subList(1, phenomena.size()) : Collections.emptyList();
        final DerivationPathNode currentNode=strategy.getDerivation();

        //case phenomenon not applied
        apply(strategy, remainingPhenomena);
        //case phenomenon applied
        boolean activeDerivationPath=false;
        for(final String intermediateForm: currentPhenomenon.apply(currentNode.get())){
            final DerivationPathNode nextNode=new DerivationPathNodeImpl(intermediateForm, currentNode, currentPhenomenon);
            final DerivationStrategy nextStrategy=strategy.extend(nextNode);
            if (nextStrategy.goOn()) {
                apply(nextStrategy, remainingPhenomena);
                activeDerivationPath=true;
            }
            if (!activeDerivationPath)
                strategy.end();
        }
    }
}
