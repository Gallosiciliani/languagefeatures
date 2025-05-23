package it.unict.gallosiciliani.pdfimporter;

import it.unict.gallosiciliani.importing.api.OntologyBuilder;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.FileEntityManagerFactoryHelper;

import java.io.IOException;

/**
 * @author Cristiano Longo
 */
public class Main {

    public static final String NS="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#";

    public static void main(final String[] args) throws IOException {
        final String nicosiaSperlingaVocabFile=args[0];
        final String ontologyFile=args[1];
        System.out.println("Writing entries parsed from "+nicosiaSperlingaVocabFile+" into "+ontologyFile);
        try(final EntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper(ontologyFile);
            final OntologyBuilder o=new OntologyBuilder(emf, PDFLexiconConverter.FACTORY, NS)){
            o.read(nicosiaSperlingaVocabFile);
            System.out.println("Added "+o.getWriter().getNumEntries()+" entries and "+o.getWriter().getForms().size()+" forms to the ontology.");
        }
    }
}
