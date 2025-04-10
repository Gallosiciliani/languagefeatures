package it.unict.gallosiciliani.liph.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Occurrence of a linguistic phenomenon. Individuals of this class are the reified counterpart of assertions involving linguistic phenomena.
 * @author Cristiano Longo
 * NOTE: here we restrict to occurrences connecting {@link LexicalObject} individuals
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri= LinguisticPhenomena.LINGUISTIC_PHENOMENON_OCCURRENCE_CLASS)
@Data
public class LinguisticPhenomenonOccurrence extends Thing {
    @OWLObjectProperty(iri=LinguisticPhenomena.OCCURRENCE_OF_OBJ_PROPERTY)
    LinguisticPhenomenon occurrenceOf;
    @OWLObjectProperty(iri=LinguisticPhenomena.SOURCE_OBJ_PROPERTY)
    LexicalObject source;
    @OWLObjectProperty(iri=LinguisticPhenomena.TARGET_OBJ_PROPERTY)
    LexicalObject target;
}
