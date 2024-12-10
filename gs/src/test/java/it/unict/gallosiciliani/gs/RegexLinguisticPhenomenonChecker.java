package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexLinguisticPhenomenonChecker {
    public static final String VOWELS = "aàáäeèéëë̀iìíïoòóöö̀uùúü"; //TODO

    private final LinguisticPhenomenon phenomenon;
    private final String[] replacements;

    RegexLinguisticPhenomenonChecker(final LinguisticPhenomenon phenomenon, final String...replacements){
        this.phenomenon=phenomenon;
        this.replacements=replacements;
    }

    /**
     * Test transformations with rules of the form -s- -> -replacement-
     * where - are placeholders for vowels.
     *
     * @param strict if true, the phenomenon does not apply outside vowels (for example, at the beginning of the string)
     * @param src    parts to be replaced
     */
    public RegexLinguisticPhenomenonChecker betweenVowels(boolean strict, final String...src){
        for(final String s: src) {
            final String consonant = "X";
            notApply("123" + consonant + s + consonant + "456");
            for (final char cprev : VOWELS.toCharArray()) {
                notApply("123" + cprev + s + consonant + "456");
                if (strict)
                    notApply("123" + cprev + s);
                for (final char csucc : VOWELS.toCharArray()) {
                    if (strict)
                        notApply(s+csucc+"456");
                    notApply("123" + consonant + s + csucc + "456");
                    final SortedSet<String> expected = new TreeSet<>();
                    for (final String r : replacements)
                        expected.add("123" + cprev + r + csucc + "456");
                    derives("123" + cprev + s + csucc + "456", expected);
                }
            }
        }
        return this;
    }

    /**
     * Test transformations with rules of the form -src -> -replacement
     * where - are placeholders for any character.
     *
     * @param strict if true, the phenomenon must occur only at the end of the string
     * @param src    part to be replaced
     */
    public RegexLinguisticPhenomenonChecker atTheEnd(boolean strict, final String...src) {
        final SortedSet<String> expected = new TreeSet<>();
        for (final String r : replacements)
            expected.add("123" + r);
        for(final String s: src) {
            if (strict) {
                notApply("123" + s + "456");
                notApply(s + "456");
            }
            derives("123" + s, expected);
        }
        return this;
    }

    /**
     * Test transformations with rules of the form -src- -> -replacement-
     * where - are placeholders for any character.
     *
     * @param strict if true, the phenomenon is allowed strictly inside the string, not at the boundaries
     * @param src    parts to be replaced
     */
    public RegexLinguisticPhenomenonChecker inside(boolean strict, final String...src) {
        final SortedSet<String> expected = new TreeSet<>();
        for (final String r : replacements)
            expected.add("123" + r + "456");
        for(final String s: src) {
            if (strict) {
                notApply("123" + s);
                notApply(s + "456");
            }
            derives("123" + s + "456", expected);
        }
        return this;
    }

    /**
     * Test transformations with rules of the form src- -> replacement-
     * where - are placeholders for any character.
     *
     * @param strict if true, the phenomenon applies only at the beginning of a string
     * @param src    parts to be replaced
     * @return this checker
     */
    public RegexLinguisticPhenomenonChecker atTheBeginning(boolean strict, final String...src) {
        for(final String s: src) {
            if (strict) {
                notApply("123" + s + "456");
                notApply("123" + s);
            }
            final SortedSet<String> expected = new TreeSet<>();
            for (final String r : replacements)
                expected.add(r+"456");
            derives(s+"456", expected);
        }
        return this;
    }

    /**
     * Test that the feature transformation does not change the src string
     * @param src test string
     */
    public void notApply(final String src){
        assertTrue(phenomenon.apply(src).isEmpty(), "Expected empty but it was "+phenomenon.apply(src));
    }

    /**
     * Test that the transformation of the source string produce a specified set of derived strings
     * @param src source string
     * @param expected expected derived strings
     */
    public void derives(final String src, final SortedSet<String> expected){
        assertEquals(expected, phenomenon.apply(src), "Unexpected derivation from "+src);
    }

    /**
     * Test rules of the for src -> replacement with no additional constraints
     * @param src the part to be replaced
     */
    public void replacing(final String...src){
        atTheBeginning(false, src);
        inside(false, src);
        atTheEnd(false, src);
    }


}
