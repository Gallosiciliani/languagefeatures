package it.unict.gallosiciliani.importing.partofspeech;

public class POSExamples {

    public static final String[] NOUN = {"sost.femm.", "sost.femm. temp.", "sost.femm.det.", "sost.femm.massa",
            "sost.masch.", "sost.masch", "sost.masch. solo vocativo", "sost.masch. temp.", "sost.masch.det.", "sost.masch.massa",
            "sost.masch.massa [-N]", "sost. non ref.", "sost.pl.",
            "sost.femm.inv.", "sost.masch. solo sing.", "sost.femm. solo sing. det.", "sost.masch. inv.", "sost.masch.inv.",
            "sost.masch.massa.", "sost.femm. solo sing.", "sost.masch.det. solo sing.", "sost.masch.massa locat.",
            "sost.femm. inv."};


    public static final String[] VERB = {"verbo", "verbo ausiliare", "verbo procompl.", "verbo pronom.",
            "verbo pronom. procompl.", "verbo pronom. pro-compl."};

    public static final String[] IGNORED = {"agg.", "agg.ceter.", "agg.ceter. det.", "agg./avv.distr.",
            "agg.indef.", "agg.indef. det.", "agg. inv.", "agg.num.ord.", "agg.pre-num.", "agg.prenom.",
            "agg.prenom.det. non ref.", "avv.", "avv.det.", "avv.grad.", "avv.locat.", "avv.locat. grad.",
            "avv.locat.det.", "avv.post-intens.", "avv.post-neg.", "avv.post-quant.", "avv.pre-comp.",
            "avv.pre-intens.", "avv.pre-part. var.", "avv.pre-prep.", "pre-avv.", "avv.pre-quant.",
            "avv.pre-quant.univ.", "avv.prenom.", "avv.preverb.", "avv.var.", "congiunz.coord.", "congiunz.correl.",
            "congiunz.sub.fin.", "congiunz.sub.fin", "congiunz.sub. nonfin.", "congiunz.sub.nonfin.", "det.",
            "det. dimostr.", "det. escl.", "det.poss.", "intens.",
            "intens.postagg.", "intens.var.", "negazione", "paraverbo", "particella discorsiva", "prep.",
            "prep. locat.", "prep.distr.", "pro-agg. interr.", "pro-avv.", "pro-avv. interr.", "pro-det.",
            "pro-intens.", "pro-pred.", "pro-pred. escl.", "pro-quantif. interr.", "pro-quantif. interr. e escl.",
            "pro-verbo", "proforma di frase", "pron.dimostr.", "pron.indef.", "pron.indef. non det.",
            "pron.indef.accusativo", "pron.indef.correl.", "pron.misto", "quantif.", "quantif. num.card.",
            "quantif.univ.", "art.det. femm. sing.", "pron.clitico. accusativo femm. sing.", "prep. temp.",
            "congiunz.sub.nonfin.", "congiunz.sub.nonfin", "prep.art.", "paraverbo ottat.", "paraverbo sociale",
            "paraverbo dichiar.", "agg. solo masch.", "paraverbo iuss.", "paraverbo escl.", "agg.inv.",
            "para-verbo dichiar.","agg. solo pl.", "avv.locat", "confisso sost.", "avv.prequant.", "agg.distr. inv.",
            "avv.locat. distr.", "avv.postintens.", "agg.distr", "agg.distr.", "titolo femm. solo sing. det.",
            "titolo femm. solo sing. non det.", "titolo masch. solo sing. det.", "titolo masch. solo sing. non det."};

    public static String[] getExamples(final POS pos){
        return switch (pos) {
            case NOUN -> NOUN;
            case VERB -> VERB;
            default -> IGNORED;
        };
    }
}
