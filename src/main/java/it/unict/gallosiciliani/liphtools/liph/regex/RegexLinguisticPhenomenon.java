package it.unict.gallosiciliani.liphtools.liph.regex;


import it.unict.gallosiciliani.liphtools.liph.LinguisticPhenomenon;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A language feature which operates on the substrings of the source string
 * which matches a specific regular expression.
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenon implements LinguisticPhenomenon {
    private final String iri;
    private final Pattern regex;
    private final List<String> replacements;

    public RegexLinguisticPhenomenon(final String iri, final String regex, final String... replacements){
        this.iri=iri;
        this.regex =Pattern.compile(regex);
        this.replacements= new LinkedList<>(Arrays.asList(replacements));
    }

    @Override
    public String getIRI(){
        return iri;
    }

    @Override
    public Set<String> apply(final String src) {
        final Matcher m = regex.matcher(src);
        final Set<String> res = generateStringWithReplacements(src, m, 0, "");
        res.remove(src);
        return res;
    }

    /**
     * @param src       the original string
     * @param m         a matcher produced matching the src string against the regular expression
     * @param offset    index of the character where to start processing
     * @param processed partial result of processing of src
     * @return combinations of the original string where the matched parts have been replaced or not
     */
    private Set<String> generateStringWithReplacements(final String src, final Matcher m, final int offset, String processed){
//        final Matcher m = regex.matcher(src);
        final Set<String> res = new TreeSet<>();
        if (!m.find(offset)){
            res.add(processed+src.substring(offset));
            return res;
        }
        res.addAll(generateWithoutReplacing(src, m, offset, processed));
        //reset the matcher
        m.find(offset);
        res.addAll(generateWithReplacing(src, m, offset, processed));
        return res;
    }

    /**
     * Proceed with the recursion on src without replacing the matched part.
     * here we proceed char by char to deal with overlapping patterns
     *
     * @param src       the string
     * @param m         a matcher which just matched the pattern
     * @param offset    index of the character where to start processing
     * @param processed partial result of processing of src
     * @return combination of the matched parte without replacement with all the remaining
     */
    private Set<String> generateWithoutReplacing(final String src, final Matcher m, int offset, String processed){
        return generateStringWithReplacements(src, m, offset+1, processed+src.charAt(offset));
    }

    /**
     * Proceed with the recursion on src with replacing the matched part.
     *
     * @param src       the string
     * @param m         a matcher which just matched the pattern
     * @param offset    index of the character where to start processing
     * @param processed partial result of processing of src
     * @return combination of the matched parte without replacement with all the remaining
     */
    private Set<String> generateWithReplacing(final String src, final Matcher m, int offset, String processed){
        final Set<String> res = new TreeSet<>();
        final String unchanged = src.substring(offset, m.start());
        final int newOffset = m.end();
        for(final String updated : getReplacementsHandlingMatchedGroup(m)) {
            m.find(newOffset);
            res.addAll(generateStringWithReplacements(src, m, newOffset, processed + unchanged + updated));
        }
        return res;
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

    /**
     * Add a replacement
     * @param replacement the replacement
     */
    public void addReplacement(final String replacement){
        replacements.add(replacement);
    }
}
