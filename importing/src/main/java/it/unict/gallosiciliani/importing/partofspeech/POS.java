package it.unict.gallosiciliani.importing.partofspeech;

/**
 * Allowed parts of speech and some utility functions
 */
public enum POS {
    NOUN,
    VERB,
    IGNORED;


    @Deprecated
    public final String[] posStr=null;


    /**
     * Create a POS from a POS string (see {@link it.unict.gallosiciliani.importing.pdf.parser.ParsingDataConsumer})
     * @param posString a POS string
     * @return the corresponding POS
     */
    public static POS get(final String posString) throws UnexpectedPOSStringException {
        if (hasPrefix(posString, "sost.","titolo","solo vocativo"))
            return POS.NOUN;
        //TODO pag.337 lemma="compensatö" other="(Cam.2) sost.masch." pos="massa", pos should be "sost.masch.massa"
        if (hasPrefix(posString,"verbo","impers."))
            return POS.VERB;
        if (hasPrefix(posString, "agg.", "avv.", "pre-avv.", "congiunz.", "det.", "intens.",
                "negazione", "paraverbo", "particella", "prep.", "proforma", "pron.", "quantif.",
                "art.", "confisso", "quant. inv.", "impers.", "solo sing.", "inv.", "non det.",
                "dichiar.", "temp.", "escl. inv.", "monoval.", "pers.", "solo terza pers.", "pl.", "pro-",
                "solo vocativo", "pronom.", "iuss.", "escl.", "alloc.", "postnom.", "locat.", "masch.","prenom.",
                "sing.", "titolo"))
                return POS.IGNORED;
        //TODO pag 277 "sost.masch. solo sing.": "sost.masch." e "solo sing." are recognized as two different pos
        //TODO pag.169 POS="agg.distr.", plain="(® a3)", and then another POS="inv."
        //TODO pag.174 POS="agg.ceter. det.", plain="e", and then another  POS="non det."
        throw new UnexpectedPOSStringException(posString);
    }

    private static boolean hasPrefix(final String posString, final String...prefixes){
        for(final String prefix: prefixes) {
            if (posString.startsWith(prefix) || posString.startsWith("e "+prefix))
                return true;
            if (!prefix.contains("-") && posString.contains("-")){
                final String withoutHyphens = posString.replace("-","");
                if (withoutHyphens.startsWith(prefix) || withoutHyphens.startsWith("e "+prefix))
                    return true;
            }
        }
        return false;
    }
}
