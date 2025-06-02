package it.unict.gallosiciliani.webapp.sparql;

import lombok.Getter;

/**
 * An error occurred performing a SPARQL query, may be due to query syntax errors
 */
@Getter
public class SPARQLQueryException extends Throwable {
    private final String query;

    public SPARQLQueryException(final String query, final Exception cause){
        super("Error performing sparql query "+query, cause);
        this.query=query;
    }
}
