package it.unict.gallosiciliani.derivations;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

/**
 * Node of a derivation path from a candidate etymon to strings obtained applying language features
 */
public class DerivationPathNodeImpl implements DerivationPathNode{

    private final String s;
    private final DerivationPathNode prev;
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
     * @param s              the characterizing string of this node
     * @param transformation transformation which produced the string
     * @param prev           previous part of the path
     */
    public DerivationPathNodeImpl(final String s, final LinguisticPhenomenon transformation, final DerivationPathNode prev){
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

    @Override
    public String toString(){
        if (prev==null)
            return s;
        return s+"<-"+transformation+"--"+prev;
    }
}
