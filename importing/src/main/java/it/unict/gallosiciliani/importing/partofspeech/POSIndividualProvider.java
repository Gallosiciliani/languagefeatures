package it.unict.gallosiciliani.importing.partofspeech;

import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.liph.model.lexinfo.PartOfSpeech;
import lombok.Getter;

/**
 * Provide individuals that has to be used for Part Of Speech
 * @author Cristiano Longo
 */
@Getter
public class POSIndividualProvider {
    private final PartOfSpeech noun;
    private final PartOfSpeech verb;

    public POSIndividualProvider(){
        noun=new PartOfSpeech();
        noun.setId(LexInfo.NOUN_INDIVIDUAL);
        verb=new PartOfSpeech();
        verb.setId(LexInfo.VERB_INDIVIDUAL);
    }
}
