package it.unict.gallosiciliani.projects.model.eurio;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import it.unict.gallosiciliani.liph.model.owl.Thing;

/**
 * Any tangible or intangible output of the project (such as data, knowledge and information, whatever their form or nature, whether or not they can be protected), which are generated in the project.
 * @author Cristiano Longo
 */
@OWLClass(iri=Eurio.RESULT_CLASS)
public class Result extends Thing {
}
