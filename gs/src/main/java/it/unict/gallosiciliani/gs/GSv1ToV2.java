package it.unict.gallosiciliani.gs;

import com.github.ledsoft.jopa.loader.BootAwareClasspathScanner;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.liph.model.FiniteStateLinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuerySolution;
import it.unict.gallosiciliani.liph.regex.RegexLiph1FeatureQuery;
import org.apache.jena.rdf.model.Model;

import java.io.IOException;
import java.util.Map;

/**
 * Convert feature specified using gs-features v1 to gs-features v2 formulation
 */
public class GSv1ToV2 {

    private final EntityManager entityManager;

    GSv1ToV2(final EntityManager destination){
        this.entityManager=destination;
    }

    public void convert(final Model source){
        final RegexLiph1FeatureQuery q=new RegexLiph1FeatureQuery().ignoreDeprecated();
        q.exec(source).forEach(this::add);
    }

    private void add(final RegexFeatureQuerySolution s){
        System.out.println("Converted "+s.getFeatureIRI()+" with label "+s.getFeatureLabel());
        final FiniteStateLinguisticPhenomenon p=new FiniteStateLinguisticPhenomenon();
        p.setId(s.getFeatureIRI());
        p.getTypes().add(GSFeatures.GALLOSICILIAN_FEATURE_CLASS);
        p.setLabel(s.getFeatureIRI().substring(GSFeatures.NS.length()));
        p.setComment(s.getFeatureLabel());
        p.setMatchingPattern(s.getRegex());
        p.setReplaceWith(s.getReplacement());
        entityManager.persist(p);
    }

    public static void main(final String[] args) throws IOException {
        final String destinationFilePath=args[0];
        try(final EntityManagerFactory emf=createEmf(destinationFilePath)){
            try(final EntityManager em=emf.createEntityManager()) {
                final GSv1ToV2 converter=new GSv1ToV2(em);
                try(final GSFeatures gsV1=new GSFeatures()){
                    em.getTransaction().begin();
                    converter.convert(gsV1.getModel());
                    em.getTransaction().commit();
                }
            }
        }
    }

    private static EntityManagerFactory createEmf(final String destinationFilePath){
        return Persistence.createEntityManagerFactory("pu", Map.of(
                JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.liph.model, it.unict.gallosiciliani.gs.model",
                JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName(),
                JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName(),
                JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY,destinationFilePath,
                JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.FILE,
                JOPAPersistenceProperties.CLASSPATH_SCANNER_CLASS, BootAwareClasspathScanner.class.getName()));
    }
}
