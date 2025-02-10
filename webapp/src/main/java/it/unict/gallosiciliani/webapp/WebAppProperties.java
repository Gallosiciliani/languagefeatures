package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.webapp.lexica.PagingProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix="it.unict.gallosiciliani")
@Data
public class WebAppProperties {
    private boolean loadData=true;
    private PagingProperties paging;
}
