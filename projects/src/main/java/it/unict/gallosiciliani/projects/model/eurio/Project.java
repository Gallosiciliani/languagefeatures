package it.unict.gallosiciliani.projects.model.eurio;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * A planned research work that has one or more objectives (divided or not into tasks) and is conducted by one or more organisations. In CORDIS, it represents a project funded by an EU programme.
 * @author Cristiano Longo
 */
@OWLClass(iri=Eurio.PROJECT_CLASS)
public class Project extends Thing {
    @Getter
    @Setter
    @OWLObjectProperty(iri=Eurio.HAS_RESULT_OBJECT_PROPERTY)
    Set<Result> hasResult;
}
