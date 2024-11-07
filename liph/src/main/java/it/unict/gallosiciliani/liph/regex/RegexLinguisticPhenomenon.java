package it.unict.gallosiciliani.liph.regex;


import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import lombok.Getter;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A language feature which operates on the substrings of the source string
 * which matches a specific regular expression.
 * @author Cristiano Longo
 */
public class RegexLinguisticPhenomenon implements LinguisticPhenomenon {
    private final String iri;
    @Getter
    private final Pattern regex;
    @Getter
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
        final Set<String> res = new RegexLinguisticPhenomenonProcessingStrategy(this, src).apply();
        res.remove(src);
        return res;
    }

    /**
     * Add a replacement
     * @param replacement the replacement
     */
    public void addReplacement(final String replacement){
        replacements.add(replacement);
    }
}
