package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class DerivationBuilder {
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final List<? extends Predicate<DerivationPathNode>> consumers;

    public DerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, List<? extends Predicate<DerivationPathNode>> consumers){
        this.phenomena=phenomena;
        this.consumers=consumers;
    }

    public void apply(final String src){
        final DerivationPathNode rootNode=new DerivationPathNodeImpl(src);
        apply(rootNode, phenomena, consumers, false);
    }

    /**
     *
     * @param currentNode derivation
     * @param phenomena the list of phenomena still to be applied
     * @param consumers remaining consumers
     * @param inhibitConsuming if true, does not send the current node to the consumers
     */
    private void apply(final DerivationPathNode currentNode, final List<? extends LinguisticPhenomenon> phenomena,
                       final List<? extends Predicate<DerivationPathNode>> consumers, final boolean inhibitConsuming){
        final List<? extends Predicate<DerivationPathNode>> remainingConsumers=inhibitConsuming ?
                consumers: filter(consumers, currentNode);

        if (phenomena.isEmpty() || remainingConsumers.isEmpty())
            return;

        final LinguisticPhenomenon currentPhenomenon=phenomena.get(0);
        //notice that the following list is backed by the original one
        final List<? extends LinguisticPhenomenon> remainingPhenomena= phenomena.size()>1 ?
                phenomena.subList(1, phenomena.size()) : Collections.emptyList();

        //case phenomenon not applied
        apply(currentNode, remainingPhenomena, remainingConsumers, true);
        //case phenomenon applied
        for(final String intermediateForm: currentPhenomenon.apply(currentNode.get())){
            final DerivationPathNode nextNode=new DerivationPathNodeImpl(intermediateForm, currentNode, currentPhenomenon);
            apply(nextNode, remainingPhenomena, remainingConsumers, false);
        }
    }

    private List<? extends Predicate<DerivationPathNode>> filter(final List<? extends Predicate<DerivationPathNode>> consumers,
                                                                final DerivationPathNode currentNode){
        return consumers.stream().filter(c -> c.test(currentNode)).toList();
    }


}
