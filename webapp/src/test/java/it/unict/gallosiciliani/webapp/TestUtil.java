package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.LexicalObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Entities for test purposes
 */
public class TestUtil {

    private int n;

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

    /**
     * Create a {@link Form} object for test purposes
     */
    public Form createForm(){
        final Form x=new Form();
        x.setId("http://test.org/form"+n);
        x.setWrittenRep("writtenRep"+n);
        n++;
        return x;
    }

    /**
     * Create a {@link LexicalObject} instance for test purposes
     */
    public LexicalObject createLexicalObject(){
        final LexicalObject x=new LexicalObject();
        x.setId("http://test.org/lexicalobject"+n);
        x.setWrittenRep("writtenRep"+n);
        n++;
        return x;
    }

    /**
     * Create a linguistic phenomenon for test purposes
     * @return a novel linguistic phenomenon
     */
    public LinguisticPhenomenon createPhenomenon(){
        final LinguisticPhenomenon p=mock(LinguisticPhenomenon.class);
        when(p.getIRI()).thenReturn("http://test.org/phenomenon"+n);
        n++;
        return p;
    }
}
