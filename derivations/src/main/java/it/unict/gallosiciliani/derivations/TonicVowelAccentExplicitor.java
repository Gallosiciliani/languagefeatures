package it.unict.gallosiciliani.derivations;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Change a word (or a multi-word) to make the grave accent explicit on
 * the tonic vowel, if it is not.
 * @author Cristiano Longo
 */
public class TonicVowelAccentExplicitor {
    /**
     * The plain tonic vowel found inside a word
     */
    private static class TonicVowel{
        public final char vowel;
        public final int offset;

        /**
         *
         * @param vowel the tonic vowel
         * @param offset offset of the vowel with respect to the string
         */
        TonicVowel(final char vowel, final int offset){
            this.vowel=vowel;
            this.offset=offset;
        }
    }

    private final Predicate<String> hasAccentedVowel;
    private final Pattern endingWithVowelAndNPattern;
    private final Pattern secondToLastVowelPattern;
    private final Pattern thirdToLastVowelPattern;

    public TonicVowelAccentExplicitor(){
        hasAccentedVowel=Pattern.compile("[àèìòù]|ä̀|ë̀|ï̀|ö̀|ǜ").asPredicate();
        final String atonicVowels="aeiouáéíóúäëïöü";
        endingWithVowelAndNPattern=Pattern.compile("["+atonicVowels+"]n$");
        secondToLastVowelPattern=Pattern.compile("["+atonicVowels+"][^"+atonicVowels+"]*["+atonicVowels+"][^"+atonicVowels+"]*$");
        thirdToLastVowelPattern=Pattern.compile("["+atonicVowels+"][^"+atonicVowels+"]*"+ secondToLastVowelPattern.pattern());
    }

    public String addGraveAccent(final String word){
        if (hasAccentedVowel.test(word)) return word;
        final Optional<TonicVowel> tonicVowel=findTonicVowel(word);
        if (tonicVowel.isEmpty())
            return word;

        final String tonicVowelWithGraveAccent=addGraveAccent(tonicVowel.get().vowel);
        return word.substring(0, tonicVowel.get().offset)+
                tonicVowelWithGraveAccent+
                word.substring(tonicVowel.get().offset+1);
    }

    private Optional<TonicVowel> findTonicVowel(final String word){
        final Optional<TonicVowel> endingWithVowelAndN=findVowel(endingWithVowelAndNPattern, word);
        if (endingWithVowelAndN.isPresent())
            return endingWithVowelAndN;
        final Optional<TonicVowel> secondToLastVowel=findVowel(secondToLastVowelPattern ,word);
        if (secondToLastVowel.isEmpty() || 'ï'!=secondToLastVowel.get().vowel)
            return secondToLastVowel;
        return findVowel(thirdToLastVowelPattern, word);
    }

    private Optional<TonicVowel> findVowel(final Pattern pattern, final String word){
        final Matcher m=pattern.matcher(word);
        if (!m.find())
            return Optional.empty();
        return Optional.of(new TonicVowel(word.charAt(m.start()), m.start()));
    }
    /**
     * Add grave accent to a plain vowel
     * @param plainVowel a plain vowel
     * @return the vowel with the grave accent
     */
    private String addGraveAccent(final char plainVowel){
        return switch (plainVowel) {
            case 'a' -> "à";
            case 'e' -> "è";
            case 'i' -> "ì";
            case 'o' -> "ò";
            case 'u' -> "ù";
            case 'ä' -> "ä̀";
            case 'ë' -> "ë̀";
            case 'ï' -> "ï̀";
            case 'ö' -> "ö̀";
            case 'ü' -> "ǜ";
            default -> throw new IllegalArgumentException("Not a plain vowel " + plainVowel);
        };
    }
}
