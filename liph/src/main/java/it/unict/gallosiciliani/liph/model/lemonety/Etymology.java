package it.unict.gallosiciliani.liph.model.lemonety;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import it.unict.gallosiciliani.liph.model.owl.Thing;

/**
 * 'Etymology' is a class that consists of individuals that describe the history of a Lexical Entry.
 */
@OWLClass(iri=LemonEty.NS+"Etymology")
@Data
@EqualsAndHashCode(callSuper = false)
public class Etymology extends Thing {

    @OWLObjectProperty(iri=LemonEty.NS+"startingLink", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EtyLink startingLink;
}
