package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All the information of interest concerning a single derivation
 *
 * @author Cristiano Longo
 */
public class DerivationData {

    private static final Pattern rowNumPattern=Pattern.compile("[0-9]+$");
    public final int rowNum;
    public DerivationData(final LexicalEntry entry){
        rowNum=getRowNum(entry);
    }

    private static int getRowNum(final LexicalEntry entry){
        final Matcher m=rowNumPattern.matcher(entry.getId());
        if (!m.find()) throw new IllegalArgumentException("Unexpected IRI pattern");
        return Integer.parseInt(m.group());
    }

    public int getRowNumber(){
        return 0;
    }

    public String getLemma(){
        return null;
    }


    public List<LinguisticPhenomenonOccurrence> getDerivation(){
        return null;
    }

}
