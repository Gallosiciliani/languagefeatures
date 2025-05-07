package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import org.apache.jena.ontology.OntDocumentManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Provide the beans corresponding to the OWL ontologies used in the project
 * @author Cristiano Longo
 */
@Configuration
public class OntologiesConfiguration {
    @Bean
    GSFeatures gsFeatures(final LinguisticPhenomena liph) throws IOException {
        OntDocumentManager.getInstance().addModel(LinguisticPhenomena.IRI, liph.getModel());
        return new GSFeatures();
    }

    @Bean
    LinguisticPhenomena liph() throws IOException {
        return new LinguisticPhenomena();
    }

    @Bean
    TBox tBox(LinguisticPhenomena liph, GSFeatures gsFeatures){
        return new TBox(liph, gsFeatures);
    }

    @Bean
    ABox aBox(){
        try {
            final String str= Files.readString(Path.of("nicosiaesperlinga.ttl"));
            return new ABox(str);
        } catch (IOException e) {
            return new ABox("ERROR: "+e.getMessage());
        }
    }
}
