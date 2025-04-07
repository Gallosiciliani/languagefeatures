package it.unict.gallosiciliani.webapp.ontologies;

import lombok.Getter;

/**
 * Load data in the abox file
 *
 * @author Cristiano Longo
 */
@Getter
public class ABox{
    private final String ontologyAsStr;

    ABox(final String ontologyAsStr) {
        this.ontologyAsStr=ontologyAsStr;
    }
}
