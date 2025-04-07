package it.unict.gallosiciliani.liph.model.lexinfo;


import lombok.extern.slf4j.Slf4j;

/**
 * IRI of classes, properties and individuals of our interest from the
 * <a href="http://lexinfo.net/ontology/3.0/lexinfo.owl">LexInfo</a> ontology.
 * It is a bean because it has to ensure that some individuals has to
 * be stored in the knowledge base.
 */
@Slf4j
public class LexInfo {
    public static final String NS = "http://www.lexinfo.net/ontology/3.0/lexinfo#";
    public static final String ONTOLOGY = "http://www.lexinfo.net/ontology/3.0/lexinfo";
    public static final String PART_OF_SPEECH_CLASS = NS + "PartOfSpeech";
    public static final String NOUN_INDIVIDUAL = NS + "noun";
    public static final String VERB_INDIVIDUAL = NS + "verb";
    public static final String PART_OF_SPEECH_OBJ_PROPERTY = NS + "partOfSpeech";

    public final PartOfSpeech noun;
    public final PartOfSpeech verb;
    public final PartOfSpeech[] partsOfSpeech;

    public LexInfo(){
        noun=createNoun();
        verb=createVerb();
        partsOfSpeech = new PartOfSpeech[]{noun, verb};
    }

    private static PartOfSpeech createNoun() {
        final PartOfSpeech noun = new PartOfSpeech();
        noun.setId(NOUN_INDIVIDUAL);
        return noun;
    }


    private static PartOfSpeech createVerb() {
        final PartOfSpeech verb = new PartOfSpeech();
        verb.setId(VERB_INDIVIDUAL);
        return verb;
    }

    /**
     * Get the internationalized message ID to be used as label for the corresponding {@link PartOfSpeech}
     * @param pos {@link PartOfSpeech}
     * @return the message ID to retrieve the label corresponding to the pos parameter
     */
    public static String getMessageId(final PartOfSpeech pos){
        if (VERB_INDIVIDUAL.equals(pos.getId()))
            return "galloitailici.kb.lexica.pos.verb";
        return "galloitailici.kb.lexica.pos.noun";
    }

}
