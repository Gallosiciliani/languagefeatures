package it.unict.gallosiciliani.webapp;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.LexicalObject;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public void checkEquals(final LinguisticPhenomenonOccurrence expected, final LinguisticPhenomenonOccurrence actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getOccurrenceOf().getId(), actual.getOccurrenceOf().getId());
        assertEquals(expected.getOccurrenceOf().getLabel(), actual.getOccurrenceOf().getLabel());
        assertEquals(expected.getSource().getId(), actual.getSource().getId());
    }

    /**
     * Create a {@link Form} object for test purposes
     */
    public Form createForm(){
        final Form x=new Form();
        x.setId("http://test.org/form"+n);
        x.setWrittenRep(new MultilingualString().set(LexicalObject.UNDETERMINED_LANGUAGE_TAG, "writtenRep"+n));
        n++;
        return x;
    }

    /**
     * Create a {@link LexicalObject} instance for test purposes
     */
    public LexicalObject createLexicalObject(){
        final LexicalObject x=new LexicalObject();
        x.setId("http://test.org/lexicalobject"+n);
        x.setWrittenRep(new MultilingualString().set(LexicalObject.UNDETERMINED_LANGUAGE_TAG, "writtenRep"+n));
        n++;
        return x;
    }

    /**
     * Create a linguistic phenomenon for test purposes
     * @return a novel linguistic phenomenon
     */
    public LinguisticPhenomenon createPhenomenon(){
        final LinguisticPhenomenon p=new LinguisticPhenomenon();
        p.setId("http://test.org/phenomenon"+n);
        p.setLabel("phenomenon"+n);
        n++;
        return p;
    }

    /**
     * Create a Linguistic Phenomenon occurrence in OWL
     * @param p the linguistic phenomenon
     * @param source the occurrence source
     * @param target the occurrence target
     * @param addDerivesExplicitiy if true, the derives edge between source and target is added explicitly (it should be inferred)
     * @return the occurrence
     */
    public LinguisticPhenomenonOccurrence createPhenomenonOccurrence(final LinguisticPhenomenon p, final LexicalObject source,
                                                                     final LexicalObject target, boolean addDerivesExplicitiy){
        final LinguisticPhenomenonOccurrence o=new LinguisticPhenomenonOccurrence();
        o.setId("http://test.org/occurrence"+(n++));
        o.setOccurrenceOf(p);
        o.setSource(source);
        o.setTarget(target);
        if (addDerivesExplicitiy){
            if (source.getDerives()==null)
                source.setDerives(new HashSet<>());
            source.getDerives().add(target);
        }
        return o;
    }
}
