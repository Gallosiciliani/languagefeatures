package it.unict.gallosiciliani.gs.derivationsextractor;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.Set;
import java.util.TreeSet;

/**
 * {@link LinguisticPhenomenon} implementation which returns the same set of derived lexical expressions
 */
@OWLClass(iri="http://test.com/Actionable")
public class ActionablePhenomenon extends LinguisticPhenomenon {
    @OWLDataProperty(iri="http://test.com/actionableout")
    private Set<String> out=new TreeSet<>();

    ActionablePhenomenon set(final LinguisticPhenomenon src){
        setId(src.getId());
        setLabel(src.getLabel());
        setComment(src.getComment());
        return this;
    }

    @Override
    public Set<String> apply(String src){
        return out;
    }

    void setOut(final Set<String> out){
        this.out=out;
    }
}
