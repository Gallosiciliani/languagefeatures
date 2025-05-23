package it.unict.gallosiciliani.importing.api;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.SequentialIRIProvider;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import lombok.Getter;

import java.io.IOException;

/**
 * Coordination between all the objects and classes involved in the ontology generation
 */
public class OntologyBuilder implements LexiconConverter, AutoCloseable {

    private final EntityManager em;

    @Getter
    private final LexiconOntologyWriter writer;
    private final LexiconConverter delegate;

    public OntologyBuilder(final EntityManagerFactoryHelper emf, final LexiconConverterFactory lexiconConverterFactory, final String namespace) {
        this.em = emf.createEntityManager();
        final POSIndividualProvider posProvider = new POSIndividualProvider();
        final IRIProvider iris=new SequentialIRIProvider(namespace);
        writer = new LexiconOntologyWriter(em, posProvider);
        delegate=lexiconConverterFactory.build(writer, iris, posProvider);
    }


    @Override
    public void read(String sourceFile) throws IOException {
        em.getTransaction().begin();
        delegate.read(sourceFile);
        em.getTransaction().commit();
    }

    @Override
    public void close(){
        em.close();
    }

}
