package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.webapp.lexica.PagingProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="it.unict.gallosiciliani")
@Data
public class WebAppProperties {
    private boolean loadData=true;
    private String file;
    private PagingProperties paging;
    // Available storage types using the {@link cz.cvut.kbss.ontodriver.jena.JenaDataSource}
    private String jenaStorageType;
}
