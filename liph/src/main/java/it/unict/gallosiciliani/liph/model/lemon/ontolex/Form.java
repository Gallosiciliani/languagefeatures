package it.unict.gallosiciliani.liph.model.lemon.ontolex;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A form represents one grammatical realization of a lexical entry.
 * @see <a href="https://www.w3.org/2016/05/ontolex/#form-class">LexicalObject (class)</a>
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri=Ontolex.FORM_CLASS)
@Data
public class Form extends LexicalObject {

    @OWLDataProperty(iri=Ontolex.WRITTEN_REP_DATA_PROPERTY)
    String writtenRep;
}
