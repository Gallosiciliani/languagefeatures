package it.unict.gallosiciliani.liph;

import it.unict.gallosiciliani.util.OntologyLoader;

import java.io.IOException;
import java.util.Comparator;

/**
 * An ontology which allows to define linguistic phenomena
 */
public class LinguisticPhenomena extends OntologyLoader {
    public static final String IRI = "https://gallosiciliani.unict.it/ns/liph";
    public static final String NS = IRI+"#";
    public static final String LINGUISTIC_PHENOMENON_OBJ_PROPERTY =NS+"linguisticPhenomenon";
    public static final String REGEX_ANN_PROPERTY=NS+"regex";
    public static final String REPLACEMENT_ANN_PROPERTY=NS+"replacement";
    public static final String LEXICAL_OBJECT_CLASS=NS+"LexicalObject";

    public static final String ONTOLEX_NS = "http://www.w3.org/ns/lemon/ontolex#";
    public static final String ONTOLEX_WRITTEN_REP_DATA_PROPERTY = ONTOLEX_NS+"writtenRep";

    public static final Comparator<LinguisticPhenomenon> COMPARATOR_BY_IRI=new Comparator<LinguisticPhenomenon>() {
        @Override
        public int compare(final LinguisticPhenomenon x, final LinguisticPhenomenon y) {
            return x.getIRI().compareTo(y.getIRI());
        }
    };

    public LinguisticPhenomena() throws IOException {
        super("liph.ttl");
    }
}
