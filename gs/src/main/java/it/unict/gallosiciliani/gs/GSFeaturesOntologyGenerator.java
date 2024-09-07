package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFWriter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Generate the ontology of Gallosicilian features from a file containing feature labels
 */
@Slf4j
public class GSFeaturesOntologyGenerator implements Consumer<CSVRecord> {
    private final OntModel ontModel;
    private final ObjectProperty genericFeatureProperty; //imported
    private final ObjectProperty northernFeatureProperty;
    private final ObjectProperty southernFeatureProperty;
    private final ObjectProperty nicosiaFeatureProperty;
    private final ObjectProperty sanFratelloFeatureProperty;
    private final ObjectProperty novaraFeatureProperty;

    public GSFeaturesOntologyGenerator(final Model model) throws IOException {
        OntDocumentManager.getInstance().addModel(LinguisticPhenomena.IRI, new LinguisticPhenomena().getModel());
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, model);
        ontModel.setNsPrefix("", GSFeatures.NS);
        ontModel.createOntology(GSFeatures.IRI).addImport(ontModel.createOntResource(LinguisticPhenomena.IRI));

        genericFeatureProperty = ontModel.createObjectProperty(LinguisticPhenomena.LINGUISTIC_PHENOMENON_OBJ_PROPERTY);
        northernFeatureProperty = ontModel.createObjectProperty(GSFeatures.NORTHERN_FEATURE_OBJ_PROPERTY);
        //northernFeatureProperty.addSuperProperty(genericFeatureProperty);
        southernFeatureProperty = ontModel.createObjectProperty(GSFeatures.SOUTHERN_FEATURE_OBJ_PROPERTY);
        //southernFeatureProperty.addSuperProperty(genericFeatureProperty);

        nicosiaFeatureProperty = ontModel.createObjectProperty(GSFeatures.NICOSIA_FEATURE_OBJ_PROPERTY);
        sanFratelloFeatureProperty = ontModel.createObjectProperty(GSFeatures.SAN_FRATELLO_FEATURE_OBJ_PROPERTY);
        novaraFeatureProperty = ontModel.createObjectProperty(GSFeatures.NOVARA_DI_SICILIA_FEATURE_OBJ_PROPERTY);
    }

    /**
     * Generate the ontology by reading the CSV file passed as parameter
     * and by addinf Default regex and replacements
     * @param args command line arguments
     */
    public static void main(final String[] args) throws IOException {
        final String csvFilePath = "tratti.csv";
        //final String csvFilePath = args[1];
        try(final GSFeatures ontology = GSFeatures.loadBase();
            final FileReader csvReader = new FileReader(csvFilePath)) {
            ontology.loadFeatures(csvReader);

            DefaultRegexLanguageFeatures.add(ontology.getModel());
            RDFWriter.source(ontology.getModel())
                    .format(RDFFormat.TTL)
                    .build()
                    .output("gs-features.ttl");

            ontology.print();
        }
    }
    @Override
    public void accept(final CSVRecord record) {
        final String idAndLabel = record.get(0);
        try(final Scanner s = new Scanner(idAndLabel)){
            final String codeAsStr = s.next();
            final String label = s.nextLine().substring(1);

            final char firstChar = codeAsStr.charAt(0);
            final OntProperty parent = firstChar == 'N' ? northernFeatureProperty :
                    (firstChar == 'S' ? southernFeatureProperty : genericFeatureProperty);
            log.debug("Found feature with code {} and label \"{}\"", codeAsStr, label);

            final OntProperty p = ontModel.createObjectProperty(GSFeatures.NS+codeAsStr);
            p.setLabel(idAndLabel, null);
            p.setSuperProperty(parent);

            if ("nic".equals(record.get(1)))
                p.addSuperProperty(nicosiaFeatureProperty);

            if ("sfr".equals(record.get(2)))
                p.addSuperProperty(sanFratelloFeatureProperty);

            if ("nov".equals(record.get(3)))
                p.addSuperProperty(novaraFeatureProperty);
        }
    }
}
