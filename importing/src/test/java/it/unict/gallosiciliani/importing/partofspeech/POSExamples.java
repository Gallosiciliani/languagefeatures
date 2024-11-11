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
