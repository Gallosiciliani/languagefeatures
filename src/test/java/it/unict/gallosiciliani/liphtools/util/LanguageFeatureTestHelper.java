package it.unict.gallosiciliani.liphtools.util;

import it.unict.gallosiciliani.liphtools.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liphtools.liph.LinguisticPhenomenon;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Helper class for {@link LinguisticPhenomena}
 */
public class LanguageFeatureTestHelper {
    public static final String OCCLUSIVE = "bptq";
    public static final String VOWELS = "aàeèéiìíoòóuùúyöëïü";
    public static final String VOWELS_EXT = VOWELS+'j';
    public static final String VEL_VOWELS = "aàoòóuùú";
    private static final String CONSONANTS = "bcdfghlmnpqrstxz";
    private final LinguisticPhenomenon t;

    /**
     * @param t feature under test
     */
    public LanguageFeatureTestHelper(final LinguisticPhenomenon t){
        this.t=t;
    }

    /**
     * Test that the feature transformation does not change the src string
     * @param src test string
     * @return this helper
     */
    public LanguageFeatureTestHelper notApply(final String src){
        assertTrue(t.apply(src).isEmpty(), "Expected empty but it was "+t.apply(src));
        return this;
    }

    /**
     * Test that the transformation of the source string produce a specified set of derived strings
     * @param src source string
     * @param expected expected derived strings
     * @return this object
     */
    public LanguageFeatureTestHelper derives(final String src, final String...expected){
        final SortedSet<String> expectedSet = new TreeSet<>(Arrays.asList(expected));
        assertEquals(expectedSet, t.apply(src), "Unexpected derivation from "+src);
        return this;
    }

    /**
     * Test transformations with rules of the form -srcChar1-srcChar2 -> destCharr,
     * i.e. test the replacements of all occurrences of srcChar1scrChar2 preceded by vowels
     * @param srcChar1 first character in the source string (must be different from X)
     * @param srcChar2 first character in the source string (must be different from X)
     * @param replacement replacement string
     */
    public LanguageFeatureTestHelper precededByVowelExt(final char srcChar1, final char srcChar2, final String replacement){
        notApply("123XY456").notApply("123x"+srcChar1+srcChar2+"456");
        for(int i = 0; i<VOWELS_EXT.length(); i++){
            final char before = VOWELS_EXT.charAt(i);
            derives("123"+before+srcChar1+srcChar2+"456", "123"+before+replacement+"456");
        }
        return this;
    }

    /**
     * Test transformations with rules of the form -srcChar-r -> destCharr
     * @param srcChar character in the source string (must be different from X)
     * @param destChar corresponding character in the replacement string
     */
    public LanguageFeatureTestHelper lenitionFollowedByR(final char srcChar, final char destChar){
        notApply("123XY456").notApply("123x"+srcChar+"r456");
        for(int i = 0; i<VOWELS.length(); i++){
            final char before = VOWELS.charAt(i);
            derives("123"+before+srcChar+"r456", "123"+before+destChar+"r456");
        }
        return this;
    }

    /**
     * Perform a test taking into consideration the source, but preceded by a vocal and followed by a vocal or 'r'
     * @param srcChar character in the source string (must be different from X)
     * @param destChar corresponding character in the replacement string
     */
    public LanguageFeatureTestHelper lenition(final char srcChar, final char destChar){
        assertTrue(srcChar == 'c' || OCCLUSIVE.contains(srcChar+""));
        notApply("123Xz456").notApply("123x"+srcChar+"z456");
        final String following = srcChar == 'c' ? VEL_VOWELS : VOWELS;
        for (int i = 0; i < VOWELS.length(); i++) {
            final char before = VOWELS.charAt(i);
            for (int j = 0; j < following.length(); j++) {
                final char after = following.charAt(j);
                notApply("123"+before+srcChar+"X456")
                        .notApply("123X"+srcChar+after+"456")
                        .notApply("123"+before+"X"+after+"456")
                        .derives("123"+before+srcChar+after+"456", "123"+before+destChar+after+"456");
            }
        }
        return this;
    }

        /* S.Menza
La sillaba aperta è quella che finisce con una vocale, quella chiusa finisce invece per consonante.
Così, ad es., in cantare, can è una sillaba chiusa, le altre sono aperte.
Banalmente, se una vocale è seguita da due consonanti, allora è in sillaba chiusa (perché la prima delle due consonanti
chiude la sillaba precedente).
Se la vocale (Carattere) è in fine di parola, allora la sua sillaba è aperta.
Quando la sillaba è aperta, mette un puntino dopo la vocali. ( e questa cosa poi è utilizzata dalle regole della matrice m)

*/

    /**
     * Test for replacing vowels in open syllable.
     * An open syllable is that which ends with the vowel. So, a vowel in a string belongs to a open syllable if and only
     * if one of the followings occurs:
     * 1) the vowel is at the end of the string
     * 2) the vowel is followed by a consonant and another vowel
     * @param srcVowel the vowel
     * @param replacements how the vowel has to be replaced
     * @return this object
     *
     * TODO e con le doppie?
     */
    public LanguageFeatureTestHelper inOpenSyllable(final char srcVowel, final String...replacements){
        notApply("123XY456");
        notApply(srcVowel+"456");
        final String[] expectedPrefixes = new String[replacements.length];
        for(int i=0; i<replacements.length;i++)
            expectedPrefixes[i]="123"+replacements[i];
        // 1) the vowel is at the end of the string
        derives("123"+srcVowel, expectedPrefixes);
        // 2) the vowel is followed by a consonant and another vowel
        // if followed by another vowel, then the syllable is not open
        for(int i = 0; i < VOWELS_EXT.length(); i++)
            notApply("123"+srcVowel+VOWELS_EXT.charAt(i)+"456");
        for(int i=0; i<CONSONANTS.length(); i++){
            final char consonant = CONSONANTS.charAt(i);
            //if followed by two consonants, the syllable is not open
            for(int j=0; j<CONSONANTS.length(); j++)
                notApply("123"+srcVowel+consonant+CONSONANTS.charAt(j)+"456");
            //if followeb by a consonant and another vowel, the syllable is open
            for(int j=0; j<VOWELS_EXT.length(); j++){
                final char vowel = VOWELS_EXT.charAt(j);
                final String suffix = ""+consonant+vowel+"456";
                final String[] expected = new String[expectedPrefixes.length];
                for(int k=0; k<expectedPrefixes.length; k++)
                    expected[k]=expectedPrefixes[k]+suffix;
                derives("123"+srcVowel+consonant+vowel+"456", expected);
            }
        }
        return this;
    }
}
