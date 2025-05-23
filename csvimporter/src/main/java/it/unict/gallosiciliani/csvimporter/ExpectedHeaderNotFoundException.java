package it.unict.gallosiciliani.csvimporter;

/**
 * @author Cristiano Longo
 */
public class ExpectedHeaderNotFoundException extends RuntimeException {
    public ExpectedHeaderNotFoundException(final String header){
        super("Expected header not found: "+header);
    }
}
