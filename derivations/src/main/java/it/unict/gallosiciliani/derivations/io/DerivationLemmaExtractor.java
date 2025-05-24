package it.unict.gallosiciliani.derivations.io;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.TonicVowelAccentExplicitor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Extract lemmas from derivations
 */
public class DerivationLemmaExtractor {

    private final DerivationParser parser;
    private final Locale locale;
    private final Appendable out;
    private final TonicVowelAccentExplicitor accentExplicitor=new TonicVowelAccentExplicitor();
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
        final String lemma=accentExplicitor.addGraveAccent(n.get());
        if (lemma.equals(lastLemmaFound))
            return;
        lastLemmaFound=lemma;
        out.append(lemma).append('\n');
    }

    public static void main(final String[] args) throws IOException {
        try(final CSVParser sourceParser=CSVParser.parse(new File(args[0]), StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            final FileWriter out=new FileWriter(args[1])){
            final DerivationParser parser=new DerivationIOUtil((f, l)->f.getLabel()).getParser((label, locale) -> null);
            final DerivationLemmaExtractor extractor=new DerivationLemmaExtractor(parser, Locale.ENGLISH, out);
            for (CSVRecord record : sourceParser) {
                extractor.handle(record.get(0));
            }
        }
    }
}
