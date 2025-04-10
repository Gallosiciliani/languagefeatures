package it.unict.gallosiciliani.liph.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Instances of this class represent linguistic phenomena, i.e., phenomena that change lexical expressions.
 * @author Cristiano Longo
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri= LinguisticPhenomena.LINGUISTIC_PHENOMENON_CLASS)
@Data
public class LinguisticPhenomenon extends Thing {
    /**
     * Only for executable phenomena. Override this method to implement the actual phenomenon
     * @param src source string
     * @return all the strings which are obtained with applying the feature to the source string
     *
     * //TODO create an intermediate class for executable phenomena
     */
    public Set<String> apply(String src){
        throw new UnsupportedOperationException();
    }
}
