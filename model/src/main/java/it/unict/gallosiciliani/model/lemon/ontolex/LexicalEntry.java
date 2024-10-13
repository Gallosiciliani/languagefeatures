package it.unict.gallosiciliani.model.lemon.ontolex;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import it.unict.gallosiciliani.model.lemonety.Etymology;
import it.unict.gallosiciliani.model.lemonety.LemonEty;
import lombok.Data;
import it.unict.gallosiciliani.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.model.lexinfo.PartOfSpeech;
import it.unict.gallosiciliani.model.owl.Thing;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * A lexical entry represents a unit of analysis of the lexicon that consists of a set of forms that are grammatically related and a set of base meanings that are associated with all of these forms. Thus, a lexical entry is a word, multiword expression or affix with a single part-of-speech, morphological pattern, etymology and set of senses.
 * @see <a href="https://www.w3.org/2016/05/ontolex/#lexical-entries">Lexical Entries</a>
 */

@OWLClass(iri=Ontolex.LEXICAL_ENTRY_CLASS)
@Data
@EqualsAndHashCode(callSuper = true)
public class LexicalEntry extends Thing {

    /**
     * The canonical form property relates a lexical entry to its canonical or dictionary form. This usually indicates
     * the "lemma" form of a lexical entry.
     */
    @OWLObjectProperty(iri = Ontolex.CANONICAL_FORM_OBJ_PROPERTY, fetch = FetchType.EAGER)
    private Form canonicalForm;

    @OWLObjectProperty(iri = LexInfo.PART_OF_SPEECH_OBJ_PROPERTY, fetch = FetchType.EAGER)
    private PartOfSpeech partOfSpeech;

    @OWLObjectProperty(iri = LemonEty.ETYMOLOGY_OBJ_PROPERTY, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Etymology> etymology = new HashSet<>();
}
