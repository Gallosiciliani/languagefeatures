package it.unict.gallosiciliani.sicilian;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Provide all lemmas of the Sicilian Vocabulary
 *
 * @author Cristiano Longo
 */
@Slf4j
public class SicilianVocabulary implements Consumer<String>{

    private final Consumer<String> consumer;
    public static final Pattern toBeRemoved=Pattern.compile("[-!?\uF024§.]|\\(.*[)9]|[0-9]$");

    private String lastLemma=null;
    /**
     * Package private constructor, use the visit method
     *
     * @param consumer the consumer that will receive the Sicilian vocabulary lemmas
     */
    SicilianVocabulary(final Consumer<String> consumer) {
                this.consumer=consumer;
    }

    /**
     * Send all the entries of the vocabulary to the given consumer
     * @param consumer consumer that will receive entries
     */
    public static void visit(final Consumer<String> consumer) throws IOException {
        final ClassLoader classloader=Thread.currentThread().getContextClassLoader();
        try(final InputStream s= Objects.requireNonNull(classloader.getResourceAsStream("VS.txt"));
            final Scanner entriesScanner=new Scanner(s, StandardCharsets.UTF_8)){
            final SicilianVocabulary v=new SicilianVocabulary(consumer);
            if (entriesScanner.hasNextLine())
                //remove BOM, if any. See https://stackoverflow.com/questions/4897876/reading-utf-8-bom-marker
                v.accept(entriesScanner.nextLine().replace("\uFEFF", ""));

            while(entriesScanner.hasNextLine()){
                v.accept(entriesScanner.nextLine());
            }
        }
    }

    @Override
    public void accept(final String s) {
        for(final String part: s.split(",")) {
            final String lemma = toBeRemoved.matcher(part).replaceAll("").trim().toLowerCase();
            if (lemma.isEmpty())
                log.warn("Empty lemma recognized from string \"{}\"", s);
            else if (lastLemma == null || !lastLemma.equals(lemma)) {
                consumer.accept(lemma);
                lastLemma = lemma;
            }
        }
    }
}
