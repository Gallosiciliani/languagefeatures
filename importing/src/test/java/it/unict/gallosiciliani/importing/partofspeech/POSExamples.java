package it.unict.gallosiciliani.importing.partofspeech;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class POSExamples {

    private final Collection<String> nouns=new LinkedList<>();
    private final Collection<String> verbs=new LinkedList<>();
    private final Collection<String> ignored=new LinkedList<>();

    private final Collection<String> unexpectedNouns=new LinkedList<>();
    private final Collection<String> unexpectedVerbs=new LinkedList<>();
    private final Collection<String> unexpectedIgnored=new LinkedList<>();

    private static final POSExamples IN=new POSExamples();

    private POSExamples(){
        final ClassLoader classloader=Thread.currentThread().getContextClassLoader();
        try(final InputStream s= Objects.requireNonNull(classloader.getResourceAsStream("pos.csv"));
            CSVParser p=CSVParser.parse(s, StandardCharsets.UTF_8, CSVFormat.DEFAULT)){
            p.stream().forEach(row -> {
                final String posString=row.get(0);
                final POS pos=POS.valueOf(row.get(1));
                final boolean unexpected="unexpected".equals(row.get(2));
                final Collection<String> dest=unexpected ?
                        switch (pos) {
                            case NOUN -> unexpectedNouns;
                            case VERB -> unexpectedVerbs;
                            default -> unexpectedIgnored;
                        } :
                        switch (pos) {
                            case NOUN -> nouns;
                            case VERB -> verbs;
                            default -> ignored;
                        };
                dest.add(posString);
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to read pos.csv");
        }
    }

    @Deprecated
    public static final String[] NOUN = {"sost.femm.", "sost.femm. temp.", "sost.femm.det.", "sost.femm.massa",
            "sost.masch.", "sost.masch", "sost.masch. solo vocativo", "sost.masch. temp.", "sost.masch.det.", "sost.masch.massa",
            "sost.masch.massa [-N]", "sost. non ref.", "sost.pl.",
            "sost.femm.inv.", "sost.masch. solo sing.", "sost.femm. solo sing. det.", "sost.masch. inv.", "sost.masch.inv.",
            "sost.masch.massa.", "sost.femm. solo sing.", "sost.masch.det. solo sing.", "sost.masch.massa locat.",
            "sost.femm. inv."};


    @Deprecated
    public static final String[] VERB = {"verbo", "verbo ausiliare", "verbo procompl.", "verbo pronom.",
            "verbo pronom. procompl.", "verbo pronom. pro-compl."};

    @Deprecated
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
            case NOUN -> IN.nouns.toArray(new String[0]);
            case VERB -> IN.verbs.toArray(new String[0]);
            default -> IN.ignored.toArray(new String[0]);
        };
    }

    public static String[] getUnexpectedExamples(final POS pos){
        return switch (pos) {
            case NOUN -> IN.unexpectedNouns.toArray(new String[0]);
            case VERB -> IN.unexpectedVerbs.toArray(new String[0]);
            default -> IN.unexpectedIgnored.toArray(new String[0]);
        };
    }

}
