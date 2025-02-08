package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;

/**
 * Helper class for {@link RegexLinguisticPhenomenonChecker}
 */
public class RegexLinguisticPhenomenonCheckerFactory {
    private final LinguisticPhenomenon phenomenon;

    public RegexLinguisticPhenomenonCheckerFactory(final LinguisticPhenomenon phenomenon) {
        this.phenomenon = phenomenon;
    }

    public RegexLinguisticPhenomenonChecker build(final String... replacements) {
        return new RegexLinguisticPhenomenonChecker(phenomenon, replacements);
    }

    public RegexLinguisticPhenomenonCheckerFactory replacing(final String src, final String replacement) {
        build(replacement).replacing(src);
        return this;
    }

    /**
     * Test that the feature transformation does not change the src string
     *
     * @param src test string
     * @return
     */
    public RegexLinguisticPhenomenonCheckerFactory notApply(final String src) {
        build().notApply(src);
        return this;
    }

    /**
     * Test transformations with rules of the form -src -> -replacement
     * where - are placeholders for any character.
     *
     * @param src         part to be replaced
     * @param replacement replacement string
     * @return this object
     */
    public RegexLinguisticPhenomenonCheckerFactory atTheEnd(final String src, final String replacement) {
        build(replacement).atTheEnd(true, src);
        return this;
    }

    /**
     * Test transformations with rules of the form -s- -> -replacement-
     * where the first - is a placeholder for vowels and the latter one stands for
     * any character.
     *
     * @param src         parts to be replaced
     * @param replacement replacemnt string
     */
    public RegexLinguisticPhenomenonCheckerFactory notEndingprecededByVowel(final String src, final String replacement) {
        build(replacement).notEndingprecededByVowel(src);
        return this;
    }

    public RegexLinguisticPhenomenonCheckerFactory endingPrecededByVowel(final String src, final String replacement) {
        build(replacement).endingPrecededByVowel(src);
        return this;
    }

    public RegexLinguisticPhenomenonCheckerFactory atTheBeginning(final String src, final String replacement) {
        build(replacement).atTheBeginning(true, src);
        return this;
    }
}