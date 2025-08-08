package it.unict.gallosiciliani.liph.util;

import lombok.extern.slf4j.Slf4j;
import openllet.jena.PelletReasoner;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerFactory;

@Slf4j
public class ReasonerFactoryWithTbox implements ReasonerFactory {
    private final ReasonerFactory delegate = PelletReasonerFactory.theInstance();
// NO private final ReasonerFactory delegate = GenericRuleReasonerFactory.theInstance();
//private final ReasonerFactory delegate = OWLMicroReasonerFactory.theInstance();
//private final ReasonerFactory delegate = OWLMiniReasonerFactory.theInstance();
// NO private final ReasonerFactory delegate = TransitiveReasonerFactory.theInstance();
    private static final ReasonerFactoryWithTbox INSTANCE=new ReasonerFactoryWithTbox();
    private static Reasoner reasoner;
    private static Model tboxModel;

    /**
     * This method is required for all {@link ReasonerFactory} implementations
     *
     * @return the singleton instances
     */
    public static ReasonerFactoryWithTbox theInstance(){
        return INSTANCE;
    }


    public void setTBox(final Model tboxModel){
        ReasonerFactoryWithTbox.tboxModel=tboxModel;
        reasoner=null;
    }

    @Override
    public Reasoner create(Resource resource) {
        if (tboxModel==null)
            throw new IllegalStateException("TBox not set");
        if (reasoner!=null){
            log.warn("Reasoner already created");
            return reasoner;
            //throw new IllegalStateException("Reasoner already set");
        }
        final PelletReasoner r=PelletReasonerFactory.theInstance().create(resource);
        reasoner = r.bindFixedSchema(tboxModel);
        return reasoner;
    }

    @Override
    public Model getCapabilities() {
        return delegate.getCapabilities();
    }

    @Override
    public String getURI() {
        return "https://gallosiciliani.unict.it/reasonerFactory";
    }

}
