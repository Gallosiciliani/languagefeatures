package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Entities for test purposes
 */
public class TestUtil {

    public void checkEquals(final Lexicon expected, final Lexicon actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public void checkEquals(final LexicalEntry expected, final LexicalEntry actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPartOfSpeech().getId(), actual.getPartOfSpeech().getId());
        checkEquals(expected.getCanonicalForm(), actual.getCanonicalForm());
    }

    public void checkEquals(final Form expected, final Form actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getWrittenRep(), actual.getWrittenRep());
    }
}
