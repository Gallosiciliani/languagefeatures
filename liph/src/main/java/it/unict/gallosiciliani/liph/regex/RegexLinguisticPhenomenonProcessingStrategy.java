package it.unict.gallosiciliani.liph.regex;

import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;

/**
 * Strategy to apply a {@link FiniteStateLinguisticPhenomenon} to a string.
 * This strategy can deal with nested pattern occurrences.
 *
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenonProcessingStrategy {

    private final List<String> replacements;
    private final Matcher matcher;
    private final String src;

    /**
     * Initiate the recursion
     *
     * @param phenomenon the phenomenon to apply
     */
    public RegexLinguisticPhenomenonProcessingStrategy(final FiniteStateLinguisticPhenomenon phenomenon, final String src){
        this.replacements=List.of(phenomenon.getReplaceWith());
        this.matcher=phenomenon.getRegex().matcher(src);
        this.src=src;
    }

    public Set<String> apply(){
        return apply(src);
    }

    private Set<String> apply(final String s){
        final Set<String> results=new TreeSet<>();

        final int offset=src.length()-s.length();
        //recursion final step
        if (!matcher.find(offset)){
            results.add(s);
            return results;
        }

        //we store matcher information because the matcher will be reused
        //in subsequent steps
        final int matchStart=matcher.start()-offset;
        final int matchEnd=matcher.end()-offset;
        final List<String> replacedMatch=getReplacementsHandlingMatchedGroup(matcher);
        
        apply(s, matchStart, results);
        apply(s, matchStart, matchEnd, replacedMatch, results);
        return results;
    }

    /**
     *
     */
    private void apply(final String s, final int matchStart, final Set<String> results){
        final String current=s.substring(0, matchStart+1);
        final String after=s.substring(matchStart+1);
        for(final String remainingProcessed: apply(after))
            results.add(current+remainingProcessed);
    }

    /**
     * Recursion step which applies the replacements to the matched part
     */
    private void apply(final String s, final int matchStart, final int matchEnd, final List<String> replacedMatch, final Set<String> results){
        //the initial part of the string not affected by the pattern
        final String prev=s.substring(0, matchStart);

        //part of the string after the matched part
        final String after=s.substring(matchEnd);

        final Set<String> remainingProcessed=apply(after);
        for(final String replaced: replacedMatch)
            for(final String remaining: remainingProcessed)
                results.add(prev+replaced+remaining);

    }

    /**
     * Return the replacements where $1 is replaced with the group detected by the given matcher
     * @param m the matcher which captured groups
     * @return list of replacements where $n have been replaced with the h-th captured group
     */
    private List<String> getReplacementsHandlingMatchedGroup(final Matcher m){
        if (m.groupCount()==0)
            return replacements;
        final List<String> replacementsWithGroups = new ArrayList<>(replacements.size());
        for(final String replacement : replacements)
            replacementsWithGroups.add(replaceGroups(replacement, m));
        return replacementsWithGroups;
    }

    /**
     * Replace all expressions $ngroup with the corresponding groups detected by the matcher
     * @param str string
     * @param m matcher
     * @return the string with groups expression replaced with detected groups
     */
    private String replaceGroups(final String str, final Matcher m){
        String r = str;
        for(int i =1 ; i<=m.groupCount(); i++){
            r = r.replace("$" + i, m.group(i)==null ? "" : m.group(i));
        }
        return r;
    }

}
