package it.unict.gallosiciliani.webapp.sparql;

import cz.cvut.kbss.jopa.model.query.Parameter;
import cz.cvut.kbss.jopa.model.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * See also <a href="https://www.baeldung.com/apache-commons-csv">Apache Commons CSV</a>
 */
@Slf4j
public class SPARQLResultToCSVConverter implements Consumer<Object> {
    /**
     * Object delegate to process result set rows
     */
    private interface RowProcessor{
        void accept(Object row) throws IOException;
    }

    private final StringBuffer csv;
    private final RowProcessor delegateRowProcessor;

    /**
     *
     * @param headers query unbound parameter names
     * @throws IOException if some error occurs generating the CSV
     */
    public SPARQLResultToCSVConverter(final String[] headers) throws IOException {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(headers);
//                .setHeader(headers)
//                .build();
        csv = new StringBuffer();
        final CSVPrinter printer = new CSVPrinter(csv, csvFormat);
        delegateRowProcessor = getRowProcessor(headers.length, printer);
    }

    /**
     * Convenience method
     * @param query query object
     * @return the result set converted as CSV
     * @throws IOException if CSV generation fails
     */
    public static String getResultAsCSV(final Query query) throws IOException {
        final SPARQLResultToCSVConverter c = new SPARQLResultToCSVConverter(getHeaders(query));
        final Stream<?> resultStream = query.getResultStream();
        resultStream.forEach(c);
        return c.getCSV();
    }

    /**
     * Create headers from query parameters
     *
     * @param query the query
     * @return String[] names of unbound query parameters
     */
    private static String[] getHeaders(final Query query){
        final Set<Parameter<?>> params = query.getParameters();
        final List<String> unboundParamNames = new LinkedList<>();
        for(final Parameter<?> p : params)
            if (!query.isBound(p)) unboundParamNames.add(p.getName());
        return unboundParamNames.toArray(new String[0]);
    }

    /**
     *
     * @param numCols number of columns in the result set
     * @param printer devoted to add the row to the CSV
     * @return RowProcessor suitable for result set rows with the specified number of columns
     */
    private static RowProcessor getRowProcessor(final int numCols, final CSVPrinter printer){
        if (numCols==1) return (Object row) -> printer.printRecord(row.toString());

        final String[] values = new String[numCols];
        return (Object row) -> {
            try {
                final Object[] cols = (Object[]) row;
                for (int i = 0; i < numCols; i++)
                    values[i] = cols[i].toString();
                printer.printRecord((Object[]) values);
            } catch(final ClassCastException e){
                log.error("Unable to cast "+row+" into Object[]",e);
            }
        };
    }
    @Override
    public void accept(Object o) {
        try {
            delegateRowProcessor.accept(o);
        } catch (IOException e) {
            log.error("Unable to convert result set row "+o.toString());
        }
    }

    /**
     * Get the result set converted to CSV
     * @return a string in CSV format
     */
    public String getCSV(){
        return csv.toString();
    }
}
