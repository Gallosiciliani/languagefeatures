package it.unict.gallosiciliani.importing;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator.LexicalEntriesGenerator;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.Parser;
import it.unict.gallosiciliani.importing.nicosiasperlingavocab.writing.NicosiaSperlingaEntityManagerFactory;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.importing.persistence.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.importing.persistence.LexiconOntologyWriter;
import lombok.Getter;

import java.io.IOException;

/**
 * Coordination between all the objects and classes involved in the ontology generation
 */
public class OntologyBuilder implements AutoCloseable {

    private final EntityManagerFactoryHelper emf;
    private final EntityManager em;

    @Getter
    private final LexiconOntologyWriter writer;
    private final LexicalEntriesGenerator entriesGenerator;

    OntologyBuilder(final String ontologyFilePath) throws IOException {
        this.emf = new NicosiaSperlingaEntityManagerFactory(ontologyFilePath);
        this.em = emf.createEntityManager();
        final POSIndividualProvider posProvider = new POSIndividualProvider();
        writer = new LexiconOntologyWriter(em, posProvider);
        entriesGenerator = new LexicalEntriesGenerator(writer, NicosiaSperlingaEntityManagerFactory.NS, posProvider);
    }

    public void parse(final String nicosiaSperlingaVocabPDFFile,
                                                             final int startPage,
                                                             final int endPage) throws IOException {
        em.getTransaction().begin();
        try(final Parser p=new Parser(entriesGenerator, nicosiaSperlingaVocabPDFFile)){
            p.parsePages(startPage, endPage);
        }
        em.getTransaction().commit();
    }

    @Override
    public void close(){
        em.close();
        emf.close();
    }

    public static void main(final String[] args) throws Exception {
        final String nicosiaSperlingaVocabPDFFile=args[0];
        final int startPage=Integer.parseInt(args[1]);
        final int endPage=Integer.parseInt(args[2]);
        final String ontologyFilePath=args[3];
        try(final OntologyBuilder o=new OntologyBuilder(ontologyFilePath)){
            o.parse(nicosiaSperlingaVocabPDFFile, startPage, endPage);
            System.out.println("Generated ontology with "+o.getWriter().getNumEntries()+" entries and "+o.getWriter().getForms().size()+" forms.");
        }
    }
}
