package it.unict.gallosiciliani.liph;

import openllet.jena.PelletReasoner;
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
        final PelletReasoner r=PelletReasonerFactory.theInstance().create(resource);
        return r.bindFixedSchema(liphModel);
    }

    @Override
    public Model getCapabilities() {
        return PelletReasonerFactory.theInstance().getCapabilities();
    }

    @Override
    public String getURI() {
        return "https://gallosiciliani.unict.it/testreasonerfactory";
    }
}
