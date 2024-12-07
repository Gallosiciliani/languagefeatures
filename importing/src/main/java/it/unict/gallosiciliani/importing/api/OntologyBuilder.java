package it.unict.gallosiciliani.importing.api;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.csv.CSVLexiconConverter;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.importing.iri.SequentialIRIProvider;
import it.unict.gallosiciliani.importing.pdf.PDFLexiconConverter;
import it.unict.gallosiciliani.importing.pdf.writing.NicosiaSperlingaEntityManagerFactory;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.persistence.EntityManagerFactoryHelper;
import lombok.Getter;

import java.io.IOException;

/**
 * Coordination between all the objects and classes involved in the ontology generation
 */
public class OntologyBuilder implements LexiconConverter, AutoCloseable {

    private final EntityManagerFactoryHelper emf;
    private final EntityManager em;

    @Getter
    private final LexiconOntologyWriter writer;
    private final LexiconConverter delegate;

    OntologyBuilder(final String ontologyFilePath, final LexiconConverterFactory lexiconConverterFactory) throws IOException {
        this.emf = new NicosiaSperlingaEntityManagerFactory(ontologyFilePath);
        this.em = emf.createEntityManager();
        final POSIndividualProvider posProvider = new POSIndividualProvider();
        final IRIProvider iris=new SequentialIRIProvider(NicosiaSperlingaEntityManagerFactory.NS);
        writer = new LexiconOntologyWriter(em, posProvider);
        delegate=lexiconConverterFactory.build(writer, iris, posProvider);
    }

    public static void main(final String[] args) throws Exception {
        final String nicosiaSperlingaVocabFile=args[0];
        final String ontologyFilePath=args[1];
        final LexiconConverterFactory converterFactory=nicosiaSperlingaVocabFile.endsWith(".pdf") ?
                PDFLexiconConverter.FACTORY : CSVLexiconConverter.FACTORY;
        try(final OntologyBuilder o=new OntologyBuilder(ontologyFilePath, converterFactory)){
            o.read(nicosiaSperlingaVocabFile);
            System.out.println("Generated ontology with "+o.getWriter().getNumEntries()+" entries and "+o.getWriter().getForms().size()+" forms.");
        }
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
        emf.close();
    }

}
