package it.unict.gallosiciliani.liph.regex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

/**
 * Check whether a set of linguistic phenomena contains has internal conflicts.
 * Here two linguistic phenomena based on regular expressions are said to be
 * conflicting if and only if the intersection of their regular expressions is not
 * empty, so that they may operate on the same portion of a very same string.
 */
public class RegexLinguisticPhenomenaConflictsDetector implements Consumer<RegexLinguisticPhenomenon> {
    private final List<RegexLinguisticPhenomenon> accepted=new LinkedList<>();
    @Getter
    private final Map<RegexLinguisticPhenomenon, Set<RegexLinguisticPhenomenon>> conflicts=new TreeMap<>(LinguisticPhenomena.COMPARATOR_BY_IRI);

    @Override
    public void accept(final RegexLinguisticPhenomenon p){
        final Automaton ap = new RegExp(p.getRegex().pattern()).toAutomaton();
        for(final RegexLinguisticPhenomenon q: accepted){
            final Automaton aq = new RegExp(q.getRegex().pattern()).toAutomaton();
            final Automaton i=ap.intersection(aq);
            if (!ap.intersection(aq).isEmpty())
                getConflicting(p).add(q);
        }
        accepted.add(p);
    }

    private Set<RegexLinguisticPhenomenon> getConflicting(final RegexLinguisticPhenomenon p) {
        final Set<RegexLinguisticPhenomenon> existing = conflicts.get(p);
        if (existing != null) return existing;
        final Set<RegexLinguisticPhenomenon> novel = new TreeSet<>(LinguisticPhenomena.COMPARATOR_BY_IRI);
        conflicts.put(p, novel);
        return novel;
    }
}
