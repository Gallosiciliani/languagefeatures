package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Provide the beans corresponding to the OWL ontologies used in the project
 * @author Cristiano Longo
 */
@Configuration
public class OntologiesConfiguration {
    @Bean
    GSFeatures gsFeatures() throws IOException {
        return new GSFeatures();
    }

    @Bean
    LinguisticPhenomena liph() throws IOException {
        return new LinguisticPhenomena();
    }

}
