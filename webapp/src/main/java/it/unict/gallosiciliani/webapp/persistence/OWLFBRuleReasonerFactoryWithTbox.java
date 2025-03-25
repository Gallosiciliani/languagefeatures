package it.unict.gallosiciliani.webapp.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerFactory;
import org.apache.jena.reasoner.rulesys.OWLFBRuleReasonerFactory;

@Slf4j
public class OWLFBRuleReasonerFactoryWithTbox implements ReasonerFactory {
    private final ReasonerFactory delegate = OWLFBRuleReasonerFactory.theInstance();
    private static TBox tbox;

    public static void setTBox(final TBox tbox){
        OWLFBRuleReasonerFactoryWithTbox.tbox=tbox;
    }

    @Override
    public Reasoner create(Resource resource) {
        if (tbox==null)
            throw new IllegalStateException("TBox not set");
        final Reasoner r = delegate.create(resource);
        r.bindSchema(tbox.all);
        return r;
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
