package it.unict.gallosiciliani.gs.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import lombok.EqualsAndHashCode;

/**
 * This class represents linguistic phenomena occurred in borrowing of Sicilian expression in Gallo-Sicilian varieties.
 *
 * @author Cristiano Longo
 */
@EqualsAndHashCode(callSuper = true)
@OWLClass(iri= GSFeatures.GALLOSICILIAN_FEATURE_CLASS)
public class GalloSicilianFeature extends LinguisticPhenomenon {
}
