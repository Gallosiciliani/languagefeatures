package it.unict.gallosiciliani.webapp.persistence;

import it.unict.gallosiciliani.webapp.ontologies.TBox;
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
    private static TBox tbox;
    private static Reasoner reasoner;

    /**
     * This method is required for all {@link ReasonerFactory} implementations
     *
     * @return the singleton instances
     */
    public static ReasonerFactoryWithTbox theInstance(){
        return INSTANCE;
    }

    public void setTBox(final TBox tbox){
        ReasonerFactoryWithTbox.tbox=tbox;
    }

    @Override
    public Reasoner create(Resource resource) {
        if (tbox==null)
            throw new IllegalStateException("TBox not set");
        if (reasoner!=null){
            log.warn("Reasoner already created");
            return reasoner;
            //throw new IllegalStateException("Reasoner already set");
        }
        final PelletReasoner r=PelletReasonerFactory.theInstance().create(resource);
        reasoner = r.bindFixedSchema(tbox.all);
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
