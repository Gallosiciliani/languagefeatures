package it.unict.gallosiciliani.model.lemon.ontolex;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import it.unict.gallosiciliani.model.liph.LexicalObject;
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

}
