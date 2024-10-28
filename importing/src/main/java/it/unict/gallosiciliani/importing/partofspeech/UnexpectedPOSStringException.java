package it.unict.gallosiciliani.importing.partofspeech;

import lombok.Getter;

/**
 * Encountered an unexpected Part of Speech.
 * This fact may be due to either a parsing error or a typo error
 */
@Getter
public class UnexpectedPOSStringException extends Exception{
    public static final String[] suggestedNouns={"massa", //see page 337 compensatö (Cam.2) sost.masch.massa
            "ost.masch.", //see page 431
            "ost.femm."}; //see page 478
    public static String[] suggestedVerbs={"verbö", "verbö pronom. procompl.", "impers"};
    public static String[] suggestedIgnored= {"agg", "avv", "congiuz.", "paraverbö", "paraverbö iuss.", "non ref.",
            "pro",
            "prep", //see page 680 nintra da ePOL prep. monoval.
            ". pers. pl.", //parser error: see page 684 "agg. e pron. poss. 1a. pers. pl."
            "congiuz.sub.nonfin.", //see page 715
            ".", //see page 779 "pösà agg. (f. -àda, pl. -àë) ." the last dot is magenta
            "impers"
    };
    private final String posString;

    UnexpectedPOSStringException(final String posString){
        this.posString=posString;
    }

    /**
     * Use some euristics to attempt to get the POS corresponding to posStr
     * @return the corresponding POS
     * @throws IllegalArgumentException if has been impossible to get a suggestion for this POS
     */
    public POS getSuggestedPOS() throws IllegalArgumentException{
        for(final String suggestedNoun : suggestedNouns)
            if (suggestedNoun.equals(posString)) return POS.NOUN;
        for(final String suggestedVerb : suggestedVerbs)
            if (suggestedVerb.equals(posString)) return POS.VERB;
        for(final String suggestedIgnore : suggestedIgnored)
            if (suggestedIgnore.equals(posString)) return POS.IGNORED;
        throw new IllegalArgumentException("Unrecognized Part Of Speech "+posString);
    }
}
