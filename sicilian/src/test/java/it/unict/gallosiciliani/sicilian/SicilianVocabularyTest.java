package it.unict.gallosiciliani.sicilian;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Tests for {@link SicilianVocabulary}
 */
public class SicilianVocabularyTest {

    private interface StringConsumer extends Consumer<String> {

    }

    private static final Pattern LOWERCASE_ALPHABETIC_PATTERN = Pattern.compile("[a-zà-ṭż ’]+");

    private static class VocabularyEntriesChecker implements StringConsumer {
        int acceptedEntries;

        @Override
        public void accept(final String s) {
            assertTrue(LOWERCASE_ALPHABETIC_PATTERN.matcher(s).matches(), "\"" + s + "\" is not lowercase and alphabetical");
            acceptedEntries++;
        }
    }

    private final Consumer<String> consumer;
    private final SicilianVocabulary vocabulary;

    SicilianVocabularyTest() {
        consumer = mock();
        vocabulary = new SicilianVocabulary(consumer);
    }

    @Test
    void shouldReturnOnlyAlphabeticalStrings() throws IOException {
        final VocabularyEntriesChecker c = new VocabularyEntriesChecker();
        SicilianVocabulary.visit(c);
        System.out.println("Found " + c.acceptedEntries + " entries");
    }

    @Test
    void shouldRemovePartsBetweenParentheses() {
        assertEquals("ammolafòffici eccuteddi", SicilianVocabulary.toBeRemoved.matcher("ammolafòffici eccutedd(r)i").replaceAll(""));
        vocabulary.accept("(5) a");
        verify(consumer).accept("a");
        vocabulary.accept("(3) abbacari");
        verify(consumer).accept("abbacari");
        vocabulary.accept("(di Judèu) agricchji");
        verify(consumer).accept("agricchji");
        vocabulary.accept("ammolafòffici eccutedd(r)i");
        verify(consumer).accept("ammolafòffici eccuteddi");

        verifyNoMoreInteractions(consumer);
    }

    @Test
    void shouldRemoveHyphens() {
        vocabulary.accept("a-");
        verify(consumer).accept("a");
        vocabulary.accept("a-bbinciperdi");
        verify(consumer).accept("abbinciperdi");
        vocabulary.accept("accullà-accullà");
        vocabulary.accept("accullàaccullà");
    }

    @Test
    void shouldLowercase() {
        vocabulary.accept("abbaDDatu");
        verify(consumer).accept("abbaddatu");
    }

    @Test
    void shouldIgnoreEmptyLines() {
        vocabulary.accept("");
        verifyNoInteractions(consumer);
        vocabulary.accept("-");
        verifyNoInteractions(consumer);
        vocabulary.accept("(di MuncibbeDDu)");
        verifyNoInteractions(consumer);
    }

    @Test
    void shouldRemoveExlamationMark() {
        vocabulary.accept("! adà");
        verify(consumer).accept("adà");
    }

    @Test
    void shouldRemoveQuestionMark() {
        vocabulary.accept("? bba");
        verify(consumer).accept("bba");
    }

    @Test
    void shouldIgnoreContiguousDuplicates(){
        final InOrder o = inOrder(consumer);
        vocabulary.accept("a");
        o.verify(consumer).accept("a");
        vocabulary.accept("a");
        o.verifyNoMoreInteractions();
        vocabulary.accept("â");
        o.verify(consumer).accept("â");
        vocabulary.accept("â");
        o.verifyNoMoreInteractions();
    }

    @Test
    void shouldRemoveThreeHorizontalLinesSymbol(){
        vocabulary.accept("linniniari");
        verify(consumer).accept("linniniari");
    }

    @Test
    void shouldRemoveFlagSymbol(){
        vocabulary.accept("\uF024 manciddïari");
        verify(consumer).accept("manciddïari");
    }

    @Test
    void shouldRemoveSectionSign(){
        vocabulary.accept("ntalamari§ntalatu");
        verify(consumer).accept("ntalamarintalatu");
    }

    @Test
    void shouldRemovePartsStartingWithOpenParenthesesWithNineInsteadOfClosedOne(){
        vocabulary.accept("(29 pìzzich’effùi");
        verify(consumer).accept("pìzzich’effùi");
    }

    @Test
    void shouldRemoveSuperscriptAtTheEndOfTheLemma(){
        vocabulary.accept("rràficu1");
        verify(consumer).accept("rràficu");
        vocabulary.accept("rràficu2");
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void shouldRemoveDotMark(){
        vocabulary.accept("sfizz.aturi");
        verify(consumer).accept("sfizzaturi");
    }

    @Test
    void shouldAcceptCommaSeparatedStrings(){
        vocabulary.accept("sussilluni, sussulluni");
        verify(consumer).accept("sussilluni");
        verify(consumer).accept("sussulluni");
        verifyNoMoreInteractions(consumer);
    }

}