package it.unict.gallosiciliani.liph;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.util.OntologyItem;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import lombok.Getter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * An ontology which allows to define linguistic phenomena
 * @author Cristiano Longo
 */
public class LinguisticPhenomena extends OntologyLoader {
    public static final String IRI = "https://gallosiciliani.unict.it/ns/liph";
    public static final String VERSION = "2.0.0";
    public static final String NS = IRI+"#";
    public static final String DERIVES_OBJ_PROPERTY=NS+"derives";
    public static final String LEXICAL_OBJECT_CLASS=NS+"LexicalObject";
    public static final String WRITTEN_REP_DATA_PROPERTY = NS+"writtenRep";
    @Deprecated
    public static final String LINGUISTIC_PHENOMENON_OBJ_PROPERTY =NS+"linguisticPhenomenon";

    //reification
    public static final String LINGUISTIC_PHENOMENON_CLASS=NS+"LinguisticPhenomenon";
    public static final String LINGUISTIC_PHENOMENON_OCCURRENCE_CLASS=NS+"LinguisticPhenomenonOccurrence";
    public static final String OCCURRENCE_OF_OBJ_PROPERTY=NS+"occurrenceOf";
    public static final String SOURCE_OBJ_PROPERTY=NS+"source";
    public static final String TARGET_OBJ_PROPERTY=NS+"target";

    //finite-state
    public static final String FINITE_STATE_LINGUISTIC_PHENOMENON_CLASS=NS+"FiniteStateLinguisticPhenomenon";
    public static final String MATCHING_PATTERN_DATA_PROPERTY=NS+"matchingPattern";
    public static final String REPLACE_WITH_DATA_PROPERTY=NS+"replaceWith";

    @Deprecated
    public static final String REGEX_ANN_PROPERTY=NS+"regex";
    @Deprecated
    public static final String REPLACEMENT_ANN_PROPERTY=NS+"replacement";

    public static final Comparator<LinguisticPhenomenon> COMPARATOR_BY_IRI= Comparator.comparing(LinguisticPhenomenon::getId);
    public static final LinguisticPhenomenonLabelProvider DEFAULT_LABEL_PROVIDER= (linguisticPhenomenon, locale) -> linguisticPhenomenon.getLabel();

    public static final String[] CLASSES={LEXICAL_OBJECT_CLASS, LINGUISTIC_PHENOMENON_CLASS,LINGUISTIC_PHENOMENON_OCCURRENCE_CLASS, FINITE_STATE_LINGUISTIC_PHENOMENON_CLASS};
    public static final String[] OBJ_PROPERTIES={DERIVES_OBJ_PROPERTY, OCCURRENCE_OF_OBJ_PROPERTY, SOURCE_OBJ_PROPERTY, TARGET_OBJ_PROPERTY};
    public static final String[] DATA_PROPERTIES={WRITTEN_REP_DATA_PROPERTY, MATCHING_PATTERN_DATA_PROPERTY, REPLACE_WITH_DATA_PROPERTY};

    @Getter
    public final List<OntologyItem> classes;

    @Getter
    public final List<OntologyItem> objProperties;

    @Getter
    public final List<OntologyItem> dataProperties;

    public LinguisticPhenomena() throws IOException {
        super("liph.ttl", IRI);

        classes=retrieve(CLASSES);
        objProperties=retrieve(OBJ_PROPERTIES);
        dataProperties=retrieve(DATA_PROPERTIES);
    }

}
