package it.unict.gallosiciliani.webapp.sparql;

import lombok.Getter;

/**
 * The SPARQL query is not supported by our endpoint. Used for update queries.
 */
@Getter
public class UnsupportedSPARQLQueryException extends Throwable {
    private final String query;

    public UnsupportedSPARQLQueryException(final String query){
        super("Error performing sparql query "+query);
        this.query=query;
    }
}
