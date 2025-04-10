package it.unict.gallosiciliani.liph.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * A generic object representing one or more strings, and which may be involved in lexical processes is some way.
 * @author Cristiano Longo
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri=LinguisticPhenomena.LEXICAL_OBJECT_CLASS)
@Data
public class LexicalObject extends Thing {

    @OWLDataProperty(iri=LinguisticPhenomena.WRITTEN_REP_DATA_PROPERTY)
    String writtenRep;

    @OWLObjectProperty(iri=LinguisticPhenomena.DERIVES_OBJ_PROPERTY)
    Set<LexicalObject> derives;
}
