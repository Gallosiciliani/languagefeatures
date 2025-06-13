package it.unict.gallosiciliani.webapp.ontologies;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.projects.Projects;
import it.unict.gallosiciliani.projects.model.eurio.Project;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;
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
    ABox aBox(WebAppProperties properties){
        try {
            final String str= Files.readString(Path.of(properties.getFile()));
            return new ABox(str);
        } catch (IOException e) {
            return new ABox("ERROR: "+e.getMessage());
        }
    }

    @Bean
    Projects projects(final LinguisticPhenomena liph, final GSFeatures gs, final EntityManager em) throws IOException {
        final Projects projects=new Projects();
        projects.getLiph().setLabel(retrieveLabel(liph.getModel(), LinguisticPhenomena.IRI));
        projects.getGsFeatures().setLabel(retrieveLabel(gs.getModel(), GSFeatures.IRI));

        final Lexicon nicosiaesperlinga=em.find(Lexicon.class, projects.getNicosiaesperlinga().getId());
        if (nicosiaesperlinga!=null) //is null during tests
            projects.getNicosiaesperlinga().setLabel(nicosiaesperlinga.getLabel());
        else
            projects.getNicosiaesperlinga().setLabel("Missing "+projects.getNicosiaesperlinga().getId());
        return projects;
    }

    /**
     * Retrieve the label of the resource with the specified IRI in the given model
     * @param model where the resource resides
     * @param iri resource IRI
     * @return resource label value
     */
    private String retrieveLabel(final Model model, final String iri){
        final Resource r=model.getResource(iri);
        return r==null ? "no label" : r.getProperty(RDFS.label).getLiteral().getString();
    }

    @Bean
    LinguisticPhenomenaProvider linguisticPhenomenaProvider(final GSFeatures gsFeatures){
        return new LinguisticPhenomenaProvider(gsFeatures.getRegexLinguisticPhenomena());
    }
}
