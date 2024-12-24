package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class DerivationBuilder {
    private final List<? extends LinguisticPhenomenon> phenomena;
    private final List<? extends Predicate<DerivationPathNode>> consumers;

    @Getter
    private long filteringTime=0;
    public DerivationBuilder(final List<? extends LinguisticPhenomenon> phenomena, List<? extends Predicate<DerivationPathNode>> consumers){
        this.phenomena=phenomena;
        this.consumers=consumers;
    }

    public void apply(final String src){
        filteringTime=0;
        final DerivationPathNode rootNode=new DerivationPathNodeImpl(src);
        apply(rootNode, phenomena, consumers, false);
//        applyNoFilter(rootNode, phenomena);
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

    /**
     *
     * @param currentNode derivation
     * @param phenomena the list of phenomena still to be applied
     */
    private void applyNoFilter(final DerivationPathNode currentNode, final List<? extends LinguisticPhenomenon> phenomena){

        if (phenomena.isEmpty()){
            final long filteringTimeBegin=System.currentTimeMillis();
            consumers.forEach(c->c.test(currentNode));
            filteringTime=System.currentTimeMillis()-filteringTimeBegin;
            return;
        }

        final LinguisticPhenomenon currentPhenomenon=phenomena.get(0);
        //notice that the following list is backed by the original one
        final List<? extends LinguisticPhenomenon> remainingPhenomena= phenomena.size()>1 ?
                phenomena.subList(1, phenomena.size()) : Collections.emptyList();

        //case phenomenon not applied
        applyNoFilter(currentNode, remainingPhenomena);
        //case phenomenon applied
        for(final String intermediateForm: currentPhenomenon.apply(currentNode.get())){
            final DerivationPathNode nextNode=new DerivationPathNodeImpl(intermediateForm, currentNode, currentPhenomenon);
            applyNoFilter(nextNode, remainingPhenomena);
        }
    }

    private List<? extends Predicate<DerivationPathNode>> filter(final List<? extends Predicate<DerivationPathNode>> consumers,
                                                                final DerivationPathNode currentNode){
        final long filteringTimeStart=System.currentTimeMillis();
//        consumers.forEach(c -> c.test(currentNode));
//        return consumers;
        final List<? extends Predicate<DerivationPathNode>> res=consumers.stream().filter(c -> c.test(currentNode)).toList();
        filteringTime+=(System.currentTimeMillis()-filteringTimeStart);
        return res;
    }


}
