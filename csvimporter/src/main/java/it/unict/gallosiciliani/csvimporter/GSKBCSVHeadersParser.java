package it.unict.gallosiciliani.csvimporter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

/**
 * Detect coloumn headers
 */
@Getter
@Slf4j
public class GSKBCSVHeadersParser {
    public static final String LEMMA = "Lemma";
    public static final String LATIN_ETYMON = "LATINO modello";
    public static final String PART_OF_SPEECH = "Categoria";

    /**
     * Lemma column number
     */
    private int lemmaCol = -1;

    /**
     * Column for the latin etymon
     */
    private int latinEtymonCol = -1;

    private int partOfSpeechCol = -1;

    /**
     * Parse the header row
     * @param headerRow the CSV headerRow containing the header
     * @throws ExpectedHeaderNotFoundException if one of the required coloums has not been found in the header headerRow
     */
    GSKBCSVHeadersParser(final CSVRecord headerRow){
        for(int pos=0; pos<headerRow.size(); pos++)
            handleHeader(headerRow, pos);
        if (lemmaCol<0)
            throw new ExpectedHeaderNotFoundException(LEMMA);
        if (latinEtymonCol<0)
            throw new ExpectedHeaderNotFoundException(LATIN_ETYMON);
        if (partOfSpeechCol<0)
            throw new ExpectedHeaderNotFoundException(PART_OF_SPEECH);
    }

    /**
     * Handle the header at the specified position in the CSV row
     * @param headerRow row containing the header
     * @param pos position in the row
     */
    private void handleHeader(final CSVRecord headerRow, final int pos){
        final String header = headerRow.get(pos).trim();
        if (lemmaCol<0 && LEMMA.equals(header)) {
            lemmaCol = pos;
            return;
        }
        if (latinEtymonCol<0 && LATIN_ETYMON.equals(header)){
            latinEtymonCol = pos;
            return;
        }
        if (partOfSpeechCol<0 && PART_OF_SPEECH.equals(header))
            partOfSpeechCol = pos;
    }
}
