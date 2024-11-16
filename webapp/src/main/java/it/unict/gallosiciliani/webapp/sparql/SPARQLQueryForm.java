package it.unict.gallosiciliani.webapp.sparql;

import lombok.Data;

/**
 * Model object to submit a sparql query
 */
@Data
public class SPARQLQueryForm {
    private String query;
}
