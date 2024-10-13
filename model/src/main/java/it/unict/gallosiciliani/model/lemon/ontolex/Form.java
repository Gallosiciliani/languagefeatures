package it.unict.gallosiciliani.model.lemon.ontolex;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import it.unict.gallosiciliani.model.owl.Thing;

/**
 * A form represents one grammatical realization of a lexical entry.
 * @see <a href="https://www.w3.org/2016/05/ontolex/#form-class">Form (class)</a>
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri=Ontolex.FORM_CLASS)
@Data
public class Form extends Thing {

    @OWLDataProperty(iri=Ontolex.WRITTEN_REP_DATA_PROPERTY)
    private String writtenRep;
}
