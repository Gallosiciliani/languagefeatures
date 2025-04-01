package it.unict.gallosiciliani.sicilian;

import it.unict.gallosiciliani.derivations.DerivationIOUtil;
import it.unict.gallosiciliani.derivations.DerivationParser;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.gs.GSFeatures;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Extract lemmas from derivations produced by {@link SicilianToNicosiaESperlingaDerivations}
 */
public class DerivationLemmaExtractor {

    private final DerivationParser parser;
    private final Locale locale;
    private final Appendable out;
    private String lastLemmaFound;

    DerivationLemmaExtractor(final DerivationParser parser, final Locale locale, final Appendable out){
        this.out=out;
        this.locale=locale;
        this.parser=parser;
    }

    void handle(final String derivationString) throws IOException {
        handle(parser.parse(derivationString,locale));
    }
    void handle(final DerivationPathNode n) throws IOException {
        final String lemma=n.get();
        if (lemma.equals(lastLemmaFound))
            return;
        lastLemmaFound=lemma;
        out.append(lemma).append('\n');
    }

    public static void main(final String[] args) throws IOException {
        try(final CSVParser sourceParser=CSVParser.parse(new File(args[0]), StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            final GSFeatures gs=GSFeatures.loadLocal();
            final FileWriter out=new FileWriter(args[1])){
            final DerivationParser parser=new DerivationIOUtil(GSFeatures.LABEL_PROVIDER_ID).getParser(gs.getRegexLinguisticPhenomena());
            final DerivationLemmaExtractor extractor=new DerivationLemmaExtractor(parser, Locale.ENGLISH, out);
            for (CSVRecord record : sourceParser) {
                extractor.handle(record.get(0));
            }
        }
    }
}
