package it.unict.gallosiciliani.liph.regex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

/**
 * Check whether a set of linguistic phenomena contains has internal conflicts.
 * Here two linguistic phenomena based on regular expressions are said to be
 * conflicting if and only if the intersection of their regular expressions is not
 * empty, so that they may operate on the same portion of a very same string.
 */
public class RegexLinguisticPhenomenaConflictsDetector implements Consumer<FiniteStateLinguisticPhenomenon> {
    private final List<FiniteStateLinguisticPhenomenon> accepted=new LinkedList<>();
    @Getter
    private final Map<FiniteStateLinguisticPhenomenon, Set<FiniteStateLinguisticPhenomenon>> conflicts=new TreeMap<>(LinguisticPhenomena.COMPARATOR_BY_IRI);

    @Override
    public void accept(final FiniteStateLinguisticPhenomenon p){
        final Automaton ap = new RegExp(p.getMatchingPattern()).toAutomaton();
        for(final FiniteStateLinguisticPhenomenon q: accepted){
            final Automaton aq = new RegExp(q.getMatchingPattern()).toAutomaton();
            if (!ap.intersection(aq).isEmpty()) {
                final Set<FiniteStateLinguisticPhenomenon> conflictingWithQ=conflicts.get(q);
                conflictingWithQ.add(p);
                conflicts.put(p, conflictingWithQ);
            }
        }
        //no conflicts detected
        if (!conflicts.containsKey(p)){
            Set<FiniteStateLinguisticPhenomenon> conflictingWithP=new TreeSet<>(LinguisticPhenomena.COMPARATOR_BY_IRI);
            conflictingWithP.add(p);
            conflicts.put(p, conflictingWithP);
        }
        accepted.add(p);
    }
}
