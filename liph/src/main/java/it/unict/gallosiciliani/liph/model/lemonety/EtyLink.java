package it.unict.gallosiciliani.liph.model.lemonety;

import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.owl.Thing;

import java.util.HashSet;
import java.util.Set;

/**
 * 'Etymological Link' is a class that consists of individuals that represent the etymological relationship between two
 * linguistic entities.
 */
@OWLClass(iri = LemonEty.NS+"EtyLink")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"etySource", "etyTarget"})
public class EtyLink extends Thing {

    @OWLObjectProperty(iri = LemonEty.NS+"etySource")
    private LexicalEntry etySource;

    @OWLObjectProperty(iri = LemonEty.NS+"etyTarget")
    private LexicalEntry etyTarget;

    @OWLObjectProperty(iri = LemonEty.NS+"etySubSource", fetch = FetchType.EAGER)
    private Set<Form> etySubSource = new HashSet<>();

    @OWLObjectProperty(iri = LemonEty.NS+"etySubTarget")
    private Form etySubTarget;

    @Override
    public String toString(){
        return "EtyLink {<" + id + ">}";
    }
}
