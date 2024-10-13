package it.unict.gallosiciliani.model.lemonety;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.owl.Thing;

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

    @OWLObjectProperty(iri = LemonEty.NS+"etySource", cascade = CascadeType.PERSIST)
    private LexicalEntry etySource;

    @OWLObjectProperty(iri = LemonEty.NS+"etyTarget", cascade = {})
    private LexicalEntry etyTarget;

    @OWLObjectProperty(iri = LemonEty.NS+"etySubSource", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Form> etySubSource = new HashSet<>();

    @OWLObjectProperty(iri = LemonEty.NS+"etySubTarget", cascade = {})
    private Form etySubTarget;

    @Override
    public String toString(){
        return "EtyLink {<" + id + ">}";
    }
}
