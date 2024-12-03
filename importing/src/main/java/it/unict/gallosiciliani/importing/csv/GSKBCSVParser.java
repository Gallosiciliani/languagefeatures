package it.unict.gallosiciliani.importing.csv;

import it.unict.gallosiciliani.importing.latin.LatinFormProvider;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lemon.ontolex.Word;
import it.unict.gallosiciliani.model.lemonety.EtyLink;
import it.unict.gallosiciliani.model.lemonety.Etymology;
import it.unict.gallosiciliani.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.model.lexinfo.PartOfSpeech;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

import java.util.function.Consumer;

/**
 * Parse rows in a CSV file with producing lexical entries corresponding to each row.
 *
 * @author Cristiano Longo
 */
@Slf4j
class GSKBCSVParser implements Consumer<CSVRecord> {
    private static final String NOUN_CATEGORY_LABEL = "nome";
    private static final String VERB_CATEGORY_LABEL = "verbo";
    private static final String HAS_FEATURE_YES = "sì";


    private final GSKBCSVHeadersParser headers;
    private final LatinFormProvider latinFormProvider;
    private final Consumer<LexicalEntry> consumer;
    /**
     * Total number of CSV rows submitted
     */
    @Getter
    private int totalRows;

    /**
     * Number of CSV rows processed with success
     */
    @Getter
    int importedRows;

    /**
     * Number of CSV rows with a latin etymon specified
     */
    @Getter
    private int importedRowsWithLatinEtymon;

    /**
     * Parse rows in a CSV file with producing lexical entries corresponding to each row.
     *
     * @param headers           information about headers in the csv file
     * @param latinFormProvider provide forms for latin etymons
     * @param consumer          which will receive the lexical entries produce by rows parsing
     */
    private GSKBCSVParser(final GSKBCSVHeadersParser headers,
                          final LatinFormProvider latinFormProvider,
                          final Consumer<LexicalEntry> consumer){
        this.headers = headers;
        this.latinFormProvider = latinFormProvider;
        this.consumer = consumer;
    }

    /**
     * Constructor method
     *
     * @param headerRow         header of the CSV file
     * @param latinFormProvider see constructor
     * @param consumer          see constructor
     * @return GIKBCSVParser initialized with the given header
     */
    static GSKBCSVParser withHeaders(final CSVRecord headerRow,
                                     final LatinFormProvider latinFormProvider,
                                     final Consumer<LexicalEntry> consumer){
        final GSKBCSVHeadersParser headers = new GSKBCSVHeadersParser(headerRow);
        return new GSKBCSVParser(headers, latinFormProvider, consumer);
    }

    @Override
    public void accept(final CSVRecord row) {
        totalRows++;
        final LexicalEntry novelEntry = new Word();
        novelEntry.setCanonicalForm(new Form());
        final String lemma = row.get(headers.getLemmaCol());
        if (lemma.isBlank()){
            log.error("Empty lemma found at line {}", totalRows);
            return;
        }
        novelEntry.getCanonicalForm().setWrittenRep(lemma);
        novelEntry.setPartOfSpeech(getPartOfSpeech(row.get(headers.getPartOfSpeechCol())));
        final Etymology etymology = parseLatinEtymology(row, novelEntry);
        novelEntry.getEtymology().add(etymology);

        consumer.accept(novelEntry);
        importedRows++;
    }

    /**
     * Get the part of speech corresponding to the category label in a CSV row
     * @param category category label in the CSV
     * @return the part of speech corresponding to category
     * @throws UnrecognizedPartOfSpeech if the provided category label does not match any Part of speech
     */
    private PartOfSpeech getPartOfSpeech(final String category){
        final PartOfSpeech p = new PartOfSpeech();
        if (NOUN_CATEGORY_LABEL.equals(category))
            p.setId(LexInfo.NOUN_INDIVIDUAL);
        else if (VERB_CATEGORY_LABEL.equals(category))
            p.setId(LexInfo.VERB_INDIVIDUAL);
        else throw new UnrecognizedPartOfSpeech(category);
        return p;
    }

    /**
     * Get the latin etymon from the corresponding colums. Note that this etymon may be a word composed
     * by several subterms (prefixes, suffixes, ...)
     *
     * @param row CSV row
     * @param e   the Lexical entry the etymology refers to
     * @return the etymology object just created
     */
    private Etymology parseLatinEtymology(final CSVRecord row, final LexicalEntry e){
        final String etymStr = row.get(headers.getLatinEtymonCol());
        final Etymology etymology = new Etymology();

        final EtyLink l = new EtyLink();
        etymology.setStartingLink(l);
        l.setEtyTarget(e);
        l.setEtySubTarget(e.getCanonicalForm());

        if (etymStr.isBlank()) {
            return etymology;
        }
        importedRowsWithLatinEtymon++;
        etymology.setName(etymStr);
        for(final String component : etymStr.split("\\+")){
            final Form latinForm = latinFormProvider.getLatinForm(component);
            if (!component.equals(latinForm.getName()))
                log.error("Found latin form with two different representations: {} and {}", latinForm.getName(), component);
            l.getEtySubSource().add(latinFormProvider.getLatinForm(component));
        }
        return etymology;
    }
}
