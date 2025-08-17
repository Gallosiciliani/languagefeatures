package it.unict.gallosiciliani.pdfimporter;

import it.unict.gallosiciliani.importing.api.LexiconConverterFactory;
import it.unict.gallosiciliani.importing.api.LexiconConverterFactoryWithAccentExplicitor;
import it.unict.gallosiciliani.importing.api.OntologyBuilder;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.FileEntityManagerFactoryHelper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Cristiano Longo
 */
public class Main {

    public static final String NS="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#";

    public static void main(final String[] args) throws IOException {
        if (args.length<1){
            System.err.println("usage: java -jar pdfimporter nicosiaesperlinga.pdf [nicosiaesperlinga-base.tt]");
            return;
        }
        final String nicosiaSperlingaVocabFile=args[0];
        final String ontologyFile=args.length<2 ? copyNicosiaESperlingaBase() : args[1];
        System.out.println("Writing entries parsed from "+nicosiaSperlingaVocabFile+" into "+ontologyFile);
        final LexiconConverterFactory lexiconConverterFactory=new LexiconConverterFactoryWithAccentExplicitor(PDFLexiconConverter.FACTORY);
        try(final EntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper(ontologyFile);
            final OntologyBuilder o=new OntologyBuilder(emf, lexiconConverterFactory, NS)){
            o.read(nicosiaSperlingaVocabFile);
            System.out.println("Added "+o.getWriter().getNumEntries()+" entries and "+o.getWriter().getForms().size()+" forms to the ontology.");
        }
    }

    private static String copyNicosiaESperlingaBase() throws IOException {
        IOUtils.copy(new URL("file:nicosiaesperlinga-base.ttl"), new File("nicosiaesperlinga-lemmas.ttl"));
        return "nicosiaesperlinga-lemmas.ttl";
    }
}
