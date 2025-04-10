package it.unict.gallosiciliani.importing.csv;

import it.unict.gallosiciliani.importing.iri.EtymologyIRIProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.LexicalEntryIRIProvider;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Word;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.PartOfSpeech;
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


    private final GSKBCSVHeadersParser headers;
    private final LatinFormProvider latinFormProvider;
    private final Consumer<LexicalEntry> consumer;
    private final POSIndividualProvider posProvider;
    private final IRIProvider iriProvider;
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
     * @param consumer          which will receive the lexical entries produce by rows parsing
     * @param headers           information about headers in the csv file
     * @param latinFormProvider provide forms for latin etymons
     * @param posProvider individuals for Part Of Speech
     * @param iriProvider provide IRIs for the generated entries
     */
    private GSKBCSVParser(final Consumer<LexicalEntry> consumer, final GSKBCSVHeadersParser headers,
                          final LatinFormProvider latinFormProvider,
                          final POSIndividualProvider posProvider,
                          final IRIProvider iriProvider){
        this.headers = headers;
        this.latinFormProvider = latinFormProvider;
        this.consumer = consumer;
        this.posProvider=posProvider;
        this.iriProvider=iriProvider;
    }

    /**
     * Constructor method
     *
     * @param consumer          see constructor
     * @param headerRow         header of the CSV file
     * @param latinFormProvider see constructor
     * @param posProvider       individuals representing Part Of Speech
     * @param iriProvider provide IRIs for the generated entries
     * @return GIKBCSVParser initialized with the given header
     */
    static GSKBCSVParser withHeaders(final Consumer<LexicalEntry> consumer, final CSVRecord headerRow,
                                     final LatinFormProvider latinFormProvider, final POSIndividualProvider posProvider, IRIProvider iriProvider){
        final GSKBCSVHeadersParser headers = new GSKBCSVHeadersParser(headerRow);
        return new GSKBCSVParser(consumer, headers, latinFormProvider, posProvider, iriProvider);
    }

    @Override
    public void accept(final CSVRecord row) {
        totalRows++;
        final LexicalEntryIRIProvider entryIRIs=iriProvider.getLexicalEntryIRIs();
        final LexicalEntry novelEntry = new Word();
        novelEntry.setId(entryIRIs.getLexicalEntryIRI());
        novelEntry.setCanonicalForm(new Form());
        final String lemma = row.get(headers.getLemmaCol());
        if (lemma.isBlank()){
            log.error("Empty lemma found at line {}", totalRows);
            return;
        }
        novelEntry.getCanonicalForm().setWrittenRep(lemma);
        novelEntry.getCanonicalForm().setId(entryIRIs.getCanonicalFormIRI());
        novelEntry.setPartOfSpeech(getPartOfSpeech(row.get(headers.getPartOfSpeechCol())));
        final Etymology etymology = parseLatinEtymology(row, novelEntry, entryIRIs.getEtymologyIRIs());
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
        if (NOUN_CATEGORY_LABEL.equals(category))
            return posProvider.getNoun();
        if (VERB_CATEGORY_LABEL.equals(category))
            return posProvider.getVerb();
        throw new UnrecognizedPartOfSpeech(category);
    }

    /**
     * Get the latin etymon from the corresponding colums. Note that this etymon may be a word composed
     * by several subterms (prefixes, suffixes, ...)
     *
     * @param row           CSV row
     * @param e             the Lexical entry the etymology refers to
     * @param etymologyIRIs IRIs for etymology individuals
     * @return the etymology object just created
     */
    private Etymology parseLatinEtymology(final CSVRecord row,
                                          final LexicalEntry e,
                                          final EtymologyIRIProvider etymologyIRIs){
        final String etymStr = row.get(headers.getLatinEtymonCol());
        final Etymology etymology = new Etymology();
        etymology.setId(etymologyIRIs.getEtymolgyIRI());

        final EtyLink l = new EtyLink();
        l.setId(etymologyIRIs.getEtyLinkIRI());
        etymology.setStartingLink(l);
        l.setEtyTarget(e);
        l.setEtySubTarget(e.getCanonicalForm());

        if (etymStr.isBlank()) {
            return etymology;
        }
        importedRowsWithLatinEtymon++;
        etymology.setLabel(etymStr);
        for(final String component : etymStr.split("\\+")){
            final Form latinForm = latinFormProvider.getLatinForm(component, etymologyIRIs.getEtySourceIRI());
            if (!component.equals(latinForm.getLabel()))
                log.error("Found latin form with two different representations: {} and {}", latinForm.getLabel(), component);
            l.getEtySubSource().add(latinForm);
        }
        return etymology;
    }
}
