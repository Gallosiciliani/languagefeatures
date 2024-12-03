package it.unict.gallosiciliani.importing.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link GSKBCSVHeadersParser}
 *
 * @author Cristiano Longo
 */
public class GSKBCSVHeadersParserTest {

    @Test
    void shouldReturnMandatoryColumns() throws IOException {
        final String line = "dummycol," + GSKBCSVHeadersParser.LEMMA + ",anotherdummycol," + GSKBCSVHeadersParser.LATIN_ETYMON+","+ GSKBCSVHeadersParser.PART_OF_SPEECH;
        final GSKBCSVHeadersParser headers = getHeaders(line);
        assertEquals(1, headers.getLemmaCol());
        assertEquals(3, headers.getLatinEtymonCol());
        assertEquals(4, headers.getPartOfSpeechCol());
    }

    private GSKBCSVHeadersParser getHeaders(final String csvHeaderLine) throws IOException {
        try(final CSVParser p = CSVParser.parse(csvHeaderLine, CSVFormat.DEFAULT)){
            final CSVRecord r = p.getRecords().get(0);
            return new GSKBCSVHeadersParser(r);
        }
    }

    @Test
    void shouldThrowExceptionOnMissingHeader() {
        final String line = "dummycol,missinglemma,anotherdummycol," + GSKBCSVHeadersParser.LATIN_ETYMON+","+ GSKBCSVHeadersParser.PART_OF_SPEECH;
        assertThrows(ExpectedHeaderNotFoundException.class, ()-> getHeaders(line));
    }
}
