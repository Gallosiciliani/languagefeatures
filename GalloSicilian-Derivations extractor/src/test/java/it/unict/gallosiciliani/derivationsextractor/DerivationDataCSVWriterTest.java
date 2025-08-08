package it.unict.gallosiciliani.derivationsextractor;

import it.unict.gallosiciliani.gs.derivationsextractor.DerivationData;
import it.unict.gallosiciliani.gs.derivationsextractor.DerivationDataCSVHeader;
import it.unict.gallosiciliani.gs.derivationsextractor.DerivationDataCSVWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link it.unict.gallosiciliani.gs.derivationsextractor.DerivationDataCSVWriter}
 * @author Cristiano Longo
 */
public class DerivationDataCSVWriterTest {

    private final DerivationData data=createTestData();

    private DerivationData createTestData() {
        final DerivationData data=mock(DerivationData.class);
        when(data.getRowNumber()).thenReturn(123);
        
        return data;
    }

    @Test
    void shouldWriteHeaders() throws IOException {
        final StringWriter producedCSV=new StringWriter();
        final DerivationDataCSVWriter w=new DerivationDataCSVWriter(producedCSV);
        w.close();
        System.out.println(producedCSV.getBuffer().toString());
        final CSVParser p=CSVParser.parse(producedCSV.toString(), CSVFormat.DEFAULT);
        //the produced file has just one row, that is the header
        final List<CSVRecord> actualRecords=p.getRecords();
        assertEquals(1, actualRecords.size());
        final CSVRecord actual=actualRecords.get(0);
        for(int i = 0; i< DerivationDataCSVHeader.values().length; i++)
            assertEquals(DerivationDataCSVHeader.values()[i].toString(), actual.get(i));
    }

    @Test
    void shouldWriteId() throws IOException {
        final StringWriter producedCSV=new StringWriter();
        final DerivationDataCSVWriter w=new DerivationDataCSVWriter(producedCSV);
        w.accept(data);
        w.close();
        System.out.println(producedCSV.getBuffer().toString());
        final CSVParser p=CSVParser.parse(producedCSV.toString(),
                CSVFormat.Builder.create().setHeader(DerivationDataCSVHeader.getHeaderRow()).build());
        //the produced file has just one row, that is the header
        final List<CSVRecord> actualRecords=p.getRecords();
        assertEquals(2, actualRecords.size());
        final CSVRecord actual=actualRecords.get(1);
        assertEquals(data.getRowNumber(), Integer.parseInt(actual.get(DerivationDataCSVHeader.ID.toString())));
    }

}
