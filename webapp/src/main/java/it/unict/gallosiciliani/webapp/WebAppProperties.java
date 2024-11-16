package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.webapp.persistence.PersistenceProperties;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix="it.unict.gallosiciliani")
@Data
public class WebAppProperties {
    private URI ns;
    private PersistenceProperties persistence;

    /**
     * Get the namespace where {@link Lexicon} instances are defined
     * @return lexica namespace
     */
    public String getLexicaNS(){
        return getNs()+"lexica/";
    }
}
