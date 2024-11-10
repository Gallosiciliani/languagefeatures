package it.unict.gallosiciliani.model.lemon.lime;

import cz.cvut.kbss.jopa.model.annotations.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.jena.vocabulary.DCTerms;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.owl.Thing;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@OWLClass(iri = Lime.LEXICON_CLASS)
@Data
public class Lexicon extends Thing implements Serializable {
    public Lexicon(){
        super();
    }

    @OWLDataProperty(iri= DCTerms.NS+"title",  simpleLiteral = true)
    private String title;

    @OWLObjectProperty(iri=Lime.ENTRY_OBJ_PROPERTY)
    private Set<LexicalEntry> entry = new HashSet<>();

    @OWLObjectProperty(iri=Lime.LINGUISTIC_CATALOG_OBJ_PROPERTY)
    private String linguisticCatalog;
}
