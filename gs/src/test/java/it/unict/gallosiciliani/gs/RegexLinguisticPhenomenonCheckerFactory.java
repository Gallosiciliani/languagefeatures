package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.Getter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Helper class for {@link RegexLinguisticPhenomenonChecker}
 */
public class RegexLinguisticPhenomenonCheckerFactory {
    @Getter
    private final LinguisticPhenomenon phenomenon;
    private final GSFeaturesCategoryRetriever categoryRetriever;

    public RegexLinguisticPhenomenonCheckerFactory(final LinguisticPhenomenon phenomenon, final GSFeaturesCategoryRetriever categoryRetriever) {
        this.phenomenon = phenomenon;
        this.categoryRetriever= categoryRetriever;
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
     * @return a checker
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

    public RegexLinguisticPhenomenonCheckerFactory inside(final String src, final String replacement) {
        build(replacement).inside(true, src);
        return this;
    }

    public RegexLinguisticPhenomenonCheckerFactory derives(final String src, final String target) {
        build().derives(src, target);
        return this;
    }

    /**
     * Test transformations with rules of the form -s- -> -replacement-
     * where - are placeholders for vowels.
     *
     * @param strict if true, the phenomenon does not apply outside vowels (for example, at the beginning of the string)
     * @param src    parts to be replaced
     */
    public RegexLinguisticPhenomenonCheckerFactory betweenVowels(boolean strict, final String src, final String replacement) {
        build(replacement).betweenVowels(strict, src);
        return this;
    }

    /**
     * Ensure that the phenomenon belongs to the specified category
     * @param categoryIRI IRI characterizing the category
     * @return this factory
     */
    public RegexLinguisticPhenomenonCheckerFactory category(final String categoryIRI){
        assertEquals(categoryIRI, categoryRetriever.get(phenomenon).getIri());
        return this;
    }
}