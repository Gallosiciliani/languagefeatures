package it.unict.gallosiciliani.gs.derivationsimporter;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.io.DerivationIOUtil;
import it.unict.gallosiciliani.derivations.io.DerivationParser;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.importing.etym.EtymologyImporter;
import it.unict.gallosiciliani.importing.iri.SequentialIRIProvider;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.FileEntityManagerFactoryHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parse a derivation file and add the derivations listed in it to an OWL ontology containing the
 * derivations' lemmas.
 * The first argument is the path of the file containing the derivations. The second is the destination OWL file.
 * Finally, the third indicates a language tag that will be attached to the etymons in the OWL file.
 *
 * @author Cristiano Longo
 */
@Slf4j
public class Main {
    public static final String NS="https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#";

    public static void main(final String[] args) throws IOException {
        final String derivationFile=args[0];
        final String ontologyFile=args[1];
        final String sourceLanguageTag=args[2];
        int n=0;

        final DerivationIOUtil derivationIO=new DerivationIOUtil();
        log.info("Importing derivations from {} to {}",derivationFile,ontologyFile);
        try(final CSVParser sourceParser=CSVParser.parse(new File(derivationFile), StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            final EntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper(ontologyFile)){
            final EntityManager em=emf.createEntityManager();
            final LinguisticPhenomenonByLabelRetriever phenomenonByLabelRetriever=getGSPhenomenaRetriever(em);
            final DerivationParser parser=derivationIO.getParser(phenomenonByLabelRetriever);
            final EtymologyImporter importer=new EtymologyImporter(em,
                    new SequentialIRIProvider(NS), sourceLanguageTag);
            em.getTransaction().begin();
            for (CSVRecord record : sourceParser) {
                final DerivationPathNode d=parser.parse(record.get(0), Locale.getDefault());
                System.out.println("Importing "+derivationIO.print(d, Locale.getDefault()));
                log.info("Importing {}", derivationIO.print(d, Locale.getDefault()));
                importer.accept(d);
                n++;
            }
            em.getTransaction().commit();
        }
        log.info("Imported {} derivations",n);
    }

    private static LinguisticPhenomenonByLabelRetriever getGSPhenomenaRetriever(final EntityManager em) throws IOException {
        final Map<String, LinguisticPhenomenon> label2Lp=new TreeMap<>();
        try(final GSFeatures gs=new GSFeatures()){
            em.getTransaction().begin();
            //TODO JOPA does not perform import closure
            gs.getRegexLinguisticPhenomena().forEach((p)->{
                final LinguisticPhenomenon p0=new LinguisticPhenomenon();
                p0.setId(p.getId());
                //em.persist(p0);
                label2Lp.put(p.getLabel(), p0);
            });
            em.getTransaction().commit();
            return (label, locale) -> label2Lp.get(label);
        }
    }

}
