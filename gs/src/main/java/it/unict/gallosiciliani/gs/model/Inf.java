package it.unict.gallosiciliani.gs.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.EqualsAndHashCode;

/**
 * @author Cristiano Longo
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri= GSFeatures.INF_CLASS)
public class Inf extends LinguisticPhenomenon {
}
