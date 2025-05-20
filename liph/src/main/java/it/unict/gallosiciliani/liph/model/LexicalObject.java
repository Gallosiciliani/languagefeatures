package it.unict.gallosiciliani.liph.model;

import cz.cvut.kbss.jopa.model.MultilingualString;
import cz.cvut.kbss.jopa.model.annotations.*;
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

    public static final String UNDETERMINED_LANGUAGE_TAG="und";

    @OWLDataProperty(iri=LinguisticPhenomena.WRITTEN_REP_DATA_PROPERTY)
    MultilingualString writtenRep;

    @OWLObjectProperty(iri=LinguisticPhenomena.DERIVES_OBJ_PROPERTY)
    Set<LexicalObject> derives;

    /**
     * Set the written representation as a value with language tag corresponding to undetermined
     * @param s written representation
     */
    public void setWrittenRepUndLang(final String s){
        setWrittenRep(new MultilingualString().set(UNDETERMINED_LANGUAGE_TAG,s));
    }

}
