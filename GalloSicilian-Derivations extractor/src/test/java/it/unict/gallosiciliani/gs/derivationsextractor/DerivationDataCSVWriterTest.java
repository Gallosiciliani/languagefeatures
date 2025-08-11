package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link it.unict.gallosiciliani.gs.derivationsextractor.DerivationDataCSVWriter}
 * @author Cristiano Longo
 */
public class DerivationDataCSVWriterTest {

    final LinguisticPhenomenon p=new LinguisticPhenomenon();
    final LinguisticPhenomenon q=new LinguisticPhenomenon();

    private final CSVFormat format=CSVFormat.Builder.create().setHeader(DerivationDataCSVHeader.getHeaderRow()).build();
    private final DerivationExtData data=createTestData();

    DerivationDataCSVWriterTest(){
        p.setLabel("p");
        q.setLabel("q");
    }

    private DerivationExtData createTestData() {
        final DerivationExtData data=mock(DerivationExtData.class);
        when(data.getLemma()).thenReturn("theLemma");
        when(data.getDerivation()).thenReturn(new DerivationPathNodeImpl("theLemma"));
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
    void shouldWriteRowNumber() throws IOException {
        final CSVRecord actual=produceRow(data);
        assertEquals(1, Integer.parseInt(actual.get(DerivationDataCSVHeader.ID.toString())));
    }

    private CSVRecord produceRow(final DerivationExtData data) throws IOException {
        final StringWriter producedCSV=new StringWriter();
        try(final DerivationDataCSVWriter w=new DerivationDataCSVWriter(producedCSV)){
            w.accept(data);
            w.close();
            System.out.println(producedCSV.getBuffer().toString());
            final CSVParser p=CSVParser.parse(producedCSV.toString(),format);
            //the produced file has just one row, that is the header
            final List<CSVRecord> actualRecords=p.getRecords();
            assertEquals(2, actualRecords.size());
            return actualRecords.get(1);
        }
    }
    @Test
    void shouldWriteLemma() throws IOException {
        final String expectedLemma="expectedLemma";
        when(data.getLemma()).thenReturn(expectedLemma);
        final CSVRecord actual=produceRow(data);
        assertEquals(expectedLemma, actual.get(DerivationDataCSVHeader.LEMMA.toString()));
    }

    @Test
    void shouldWriteNoun() throws IOException{
        when(data.isNoun()).thenReturn(true);
        final CSVRecord actual=produceRow(data);
        assertEquals(DerivationDataCSVWriter.NOUN, actual.get(DerivationDataCSVHeader.TYPE.toString()));

    }

    @Test
    void shouldWriteVerb() throws IOException{
        when(data.isNoun()).thenReturn(false);
        final CSVRecord actual=produceRow(data);
        assertEquals(DerivationDataCSVWriter.VERB, actual.get(DerivationDataCSVHeader.TYPE.toString()));

    }

    @Test
    void shouldWriteDerivation() throws IOException{
        final String expected="x<-p--y<-q--z";
        final DerivationPathNode derivation=new DerivationPathNodeImpl("x", p, new DerivationPathNodeImpl("y", q, new DerivationPathNodeImpl("z")));
        when(data.getDerivation()).thenReturn(derivation);
        final CSVRecord actual=produceRow(data);
        assertEquals(expected, actual.get(DerivationDataCSVHeader.DERIVATION.toString()));
    }


    @Test
    void shouldWriteMissed() throws IOException {
        final SortedSet<LinguisticPhenomenon> missed=new TreeSet<>(LinguisticPhenomena.COMPARATOR_BY_LABEL);
        missed.add(p);
        missed.add(q);
        when(data.getMissed()).thenReturn(missed);
        final CSVRecord actual=produceRow(data);
        assertEquals(p.getLabel()+" "+q.getLabel(), actual.get(DerivationDataCSVHeader.MISSED.toString()));

    }

    @Test
    void shouldWriteGalloitalicityRateRounded() throws IOException {
        final float r=0.123956f;
        when(data.getGalloItalicityRate()).thenReturn(Optional.of(r));
        final CSVRecord actual=produceRow(data);
        assertEquals("0.124", actual.get(DerivationDataCSVHeader.RATE.toString()));

    }

    @Test
    void shouldWriteEmptyGalloitalicityRate() throws IOException {
        when(data.getGalloItalicityRate()).thenReturn(Optional.empty());
        final CSVRecord actual=produceRow(data);
        assertEquals(DerivationDataCSVWriter.NA, actual.get(DerivationDataCSVHeader.RATE.toString()));

    }

}
