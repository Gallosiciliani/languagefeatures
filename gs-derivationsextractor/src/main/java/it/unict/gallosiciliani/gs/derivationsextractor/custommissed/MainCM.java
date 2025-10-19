package it.unict.gallosiciliani.gs.derivationsextractor.custommissed;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.jena.JenaDataSource;
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties;
import it.unict.gallosiciliani.gs.derivationsextractor.*;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.util.ReasonerFactoryWithTbox;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Extract all the derivations in a knowledge base, provided as ttl file, and write them to a CSV file
 */
@Slf4j
public class MainCM {

    public static void main(final String[] args) throws IOException {
        if (args.length<3){
            System.err.println("Usage <lemmasTTLFile> <derivationsCSVFile> <outputCSVFile>");
            return;
        }
        final String inputTTLFile=args[0];
        final String inputCSVFile=args[1];
        final String outputCSVFile=args[2];
        System.out.println("Extracting derivations from "+inputTTLFile+" to "+outputCSVFile);
        try(final LinguisticPhenomena liph=new LinguisticPhenomena()) {
            ReasonerFactoryWithTbox.theInstance().setTBox(liph.getModel());
            final Map<String, String> persistenceConfig=Map.of(
                    JOPAPersistenceProperties.SCAN_PACKAGE, "it.unict.gallosiciliani.liph.model",
                    JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName(),
                    JOPAPersistenceProperties.DATA_SOURCE_CLASS, JenaDataSource.class.getName(),
                    JenaOntoDriverProperties.JENA_STORAGE_TYPE, JenaOntoDriverProperties.FILE,
                    JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, inputTTLFile,
                    OntoDriverProperties.REASONER_FACTORY_CLASS, ReasonerFactoryWithTbox.class.getName());
            try(final EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("pu", persistenceConfig);
                final EntityManager entityManager=emFactory.createEntityManager();
                final ExtendedGSPhenomena gsExt=new ExtendedGSPhenomena();
                final FileWriter out=new FileWriter(outputCSVFile);
                final DerivationDataCSVWriter writer=new DerivationDataCSVWriter(out, gsExt.getCategories(), gsExt.getLpProvider().getAll())){
                final MissedPhenomenaProvider missedProvider=new MissedPhenomenaProviderFromCSVFactory().build(inputCSVFile, gsExt);
                final DerivationDataReader reader=new DerivationDataReader(entityManager, gsExt.getLpProvider());
                int n=0;
                while (reader.hasNext()){
                    final DerivationRawData rawData=reader.next();
                    final DerivationExtData extData=new DerivationExtData(rawData, missedProvider);
                    if (extData.getMissed()==null)
                        System.err.println("Null missed phenomena for "+extData.getLemma());
                    writer.accept(extData);
                    log.debug("Extracting derivation {}", (n++));
                }
                System.out.println("Extracted "+n+" derivations");
            }
        }
    }
}
