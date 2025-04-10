package it.unict.gallosiciliani.liph.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Transient;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenonProcessingStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * An executable phenomenon described in terms of regular relations.
 * @author Cristiano Longo
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@OWLClass(iri= LinguisticPhenomena.FINITE_STATE_LINGUISTIC_PHENOMENON_CLASS)
public class FiniteStateLinguisticPhenomenon extends LinguisticPhenomenon {

    @OWLDataProperty(iri=LinguisticPhenomena.MATCHING_PATTERN_DATA_PROPERTY)
    String matchingPattern;

    @Transient
    private Pattern regex;

    public void setMatchingPattern(final String matchingPattern){
        this.matchingPattern=matchingPattern;
        this.regex =Pattern.compile(matchingPattern);
    }

    @OWLDataProperty(iri=LinguisticPhenomena.REPLACE_WITH_DATA_PROPERTY)
    @Setter
    String replaceWith;

    @Override
    public Set<String> apply(String src){
        final Set<String> res = new RegexLinguisticPhenomenonProcessingStrategy(this, src).apply();
        res.remove(src);
        return res;
    }
}
