package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * Node of a derivation path from a candidate etymon to strings obtained applying language features
 */
public class DerivationPathNodeImpl implements DerivationPathNode{

    /**
     * Compare path nodes by the characterizing strings
     */
    private static final Comparator<DerivationPathNodeImpl> COMPARATOR = Comparator.comparing(DerivationPathNodeImpl::get);

    private final String s;
    private final DerivationPathNodeImpl prev;
    private final LinguisticPhenomenon transformation;
    private final int length;

    /**
     * Create a root node for a new path
     * @param initialLexeme initial lexeme for derivations
     */
    public DerivationPathNodeImpl(final String initialLexeme){
        this(initialLexeme, null, null);
    }

    /**
     * Private constructor, use the public one to create a root node, or the apply method to build complex paths.
     *
     * @param s the characterizing string of this node
     * @param prev previous part of the path
     * @param transformation transformation which produced the string
     */
    private DerivationPathNodeImpl(final String s, final DerivationPathNodeImpl prev, final LinguisticPhenomenon transformation){
        this.s=s;
        this.prev=prev;
        this.transformation=transformation;
        length = prev == null ? 1 : prev.length() + 1;
    }

    @Override
    public String get(){
        return s;
    }

    @Override
    public DerivationPathNode prev(){
        return prev;
    }

    @Override
    public LinguisticPhenomenon getLinguisticPhenomenon(){
        return transformation;
    }

    @Override
    public int length() {
        return length;
    }

    /**
     * Apply the transformations to the string characterizing this node. Use the transformation results in order to build
     * new paths by appending these results to this path
     * @param allTranformations a list of {@link LinguisticPhenomenon} to be applied to the string of this path
     */
    public void apply(final Predicate<DerivationPathNode> consumer, final List<? extends LinguisticPhenomenon> allTranformations){
        consumer.test(this);
        apply(consumer, 0, allTranformations);
    }

    private void applyOld(final Predicate<DerivationPathNode> consumer, final int i, final List<? extends LinguisticPhenomenon> allTranformations){
        if (i==allTranformations.size()) {
            consumer.test(this);
            return;
        }
        final LinguisticPhenomenon t = allTranformations.get(i);
        //first create a branch where the current transformation has not been applied
        apply(consumer,i+1, allTranformations);
        for(final String derived : t.apply(s)) {
            //extends the path only if the transformation changed the string
            final DerivationPathNodeImpl seqNode = s.equals(derived) ? this : new DerivationPathNodeImpl(derived, this, t);
            seqNode.apply(consumer, i+1, allTranformations);
        }
    }

    private void apply(final Predicate<DerivationPathNode> consumer, final int i, final List<? extends LinguisticPhenomenon> allTranformations){
        if (i==allTranformations.size())
            return;

        final LinguisticPhenomenon t = allTranformations.get(i);
        final Set<String> allDerived=t.apply(s);
        if (allDerived.isEmpty()){
            apply(consumer, i+1, allTranformations);
            return;
        }
        for(final String derived : allDerived) {
            //extends the path only if the transformation changed the string
            if (s.equals(derived))
                apply(consumer, i+1, allTranformations);
            else {
                final DerivationPathNodeImpl seqNode=new DerivationPathNodeImpl(derived, this, t);
                //go on only if the novel derivation is not worse than the current one
                if (consumer.test(seqNode))
                    seqNode.apply(consumer, i+1, allTranformations);
            }
        }
    }

    @Override
    public String toString(){
        if (prev==null)
            return s;
        return s+"<-"+transformation+"--"+prev;
    }
}
