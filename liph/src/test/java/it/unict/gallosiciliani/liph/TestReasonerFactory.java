package it.unict.gallosiciliani.liph;

import openllet.jena.PelletReasonerFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerFactory;

import java.io.IOException;

/**
 * {@link ReasonerFactory} implementation for test purposes
 */
public class TestReasonerFactory implements ReasonerFactory {

    public static final TestReasonerFactory INSTANCE=new TestReasonerFactory();
    //private static ReasonerFactory delegate=OWLFBRuleReasonerFactory.theInstance();
    private static PelletReasonerFactory delegate=PelletReasonerFactory.theInstance();
    private final Model liphModel;

    TestReasonerFactory(){
        try {
            liphModel=new LinguisticPhenomena().getModel();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load LiPh");
        }
    }
    /**
     * This method is required for all {@link ReasonerFactory} implementations
     *
     * @return the singleton instances
     */
    public static TestReasonerFactory theInstance(){
        return INSTANCE;
    }

    @Override
    public Reasoner create(Resource resource) {
        return delegate.create(resource).bindFixedSchema(liphModel);
    }

    @Override
    public Model getCapabilities() {
        return delegate.getCapabilities();
    }

    @Override
    public String getURI() {
        return "https://gallosiciliani.unict.it/testreasonerfactory";
    }
}
