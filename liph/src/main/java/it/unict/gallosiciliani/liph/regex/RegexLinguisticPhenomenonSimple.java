package it.unict.gallosiciliani.liph.regex;


import com.github.andrewoma.dexx.collection.Sets;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simplified version of {@link RegexLinguisticPhenomenon}. These phenomena consider just a single replacement, and does
 * not handle overlapping patterns. Conversely, their applications should be considerably faster.
* @author Cristiano Longo
 */
public class RegexLinguisticPhenomenonSimple implements LinguisticPhenomenon {
    private final String iri;
    private final Pattern regex;
    private final String replacement;

    public RegexLinguisticPhenomenonSimple(final String iri, final String regex, final String... replacements){
        this.iri=iri;
        this.regex =Pattern.compile(regex);
        this.replacement=replacements[0];
        if (replacements.length>1) throw new  IllegalArgumentException("Multiple replacements not handled");
    }

    @Override
    public String getIRI(){
        return iri;
    }

    @Override
    public Set<String> apply(final String src) {
        final Matcher matcher=regex.matcher(src);
        final Set<String> res = apply(src, 0, matcher, new StringBuffer());
        res.remove(src);
        return res;
    }

    private Set<String> apply(final String src, final int offset, final Matcher matcher, final StringBuffer prev){
        if (!matcher.find(offset)){
            return Collections.singleton(prev.append(src.substring(offset)).toString());
        }
        final int matchStart=matcher.start();;
        final int matchEnd=matcher.end();
        final StringBuffer replaced=new StringBuffer(prev);
        matcher.appendReplacement(replaced, replacement);
        final Set<String> results=new TreeSet<>(apply(src, matchEnd, matcher, replaced));
        prev.append(src, offset, matchEnd);
        results.addAll(apply(src, matchEnd, matcher, prev));
        return results;
    }

    @Override
    public String toString(){
        return iri;
    }
}
