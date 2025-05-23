package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Build all valuable derivations, valuable in the sense of a specific strategy,
 * by applying a set of {@link it.unict.gallosiciliani.liph.LinguisticPhenomena}
 */
@Slf4j
public class DerivationBuilderWithStrategy implements DerivationBuilder {
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final DerivationStrategyFactory strategyFactory;


    public DerivationBuilderWithStrategy(final List<? extends LinguisticPhenomenon> phenomena, final DerivationStrategyFactory strategyFactory){
        this.phenomena=phenomena;
        this.strategyFactory=strategyFactory;
    }

    @Override
    public void apply(final String src){
        final DerivationPathNode rootNode=new DerivationPathNodeImpl(src);
        final DerivationStrategy strategy=strategyFactory.build(rootNode);
        apply(strategy, phenomena);
    }

    private void apply(final DerivationStrategy strategy, final List<? extends LinguisticPhenomenon> phenomena){
        if (phenomena.isEmpty()){
//            System.out.println(print(strategy, null, this.phenomena.size())+" DONE");
            strategy.end();
            return;
        }
        final LinguisticPhenomenon currentPhenomenon=phenomena.get(0);
        //notice that the following list is backed by the original one
        final List<? extends LinguisticPhenomenon> remainingPhenomena= phenomena.size()>1 ?
                phenomena.subList(1, phenomena.size()) : Collections.emptyList();
        final DerivationPathNode currentNode=strategy.getDerivation();

        final List<DerivationPathNode> branches=currentPhenomenon.apply(currentNode.get()).stream().<DerivationPathNode>map((f)->new DerivationPathNodeImpl(f, currentPhenomenon, currentNode)).toList();
        final Collection<DerivationStrategy> strategies=strategy.branch(branches);

//        System.out.println(print(strategy, currentPhenomenon, this.phenomena.size()-phenomena.size())+" BEGIN");
        strategies.forEach((s)-> apply(s, remainingPhenomena));
//        System.out.println(print(strategy, currentPhenomenon,this.phenomena.size()-phenomena.size())+" END");
    }
}
