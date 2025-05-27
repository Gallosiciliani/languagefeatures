package it.unict.gallosiciliani.sicilian.derivationbuilder;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import cz.cvut.kbss.ontodriver.model.LangString;
import it.unict.gallosiciliani.derivations.BruteForceDerivationBuilder;
import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.util.TonicVowelAccentExplicitor;
import it.unict.gallosiciliani.gs.GSFeatures;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Ontolex;
import it.unict.gallosiciliani.liph.util.EntityManagerFactoryHelper;
import it.unict.gallosiciliani.liph.util.FileEntityManagerFactoryHelper;
import it.unict.gallosiciliani.sicilian.SicilianVocabulary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
/**
 * Attempt to find the Sicilian etymons for lemmas in given ontology file
 *
 * @author Cristiano Longo
 */

public class Main implements Consumer<String> {

    private final TonicVowelAccentExplicitor accentExplicitor=new TonicVowelAccentExplicitor();
    private final BruteForceDerivationBuilder derivationBuilder;
    private int processedEntries=0;
    private long totalProcessingTime=0;

    private Main(final String ontologyFile) throws IOException {
        final List<String> lemmas=retrieveLemmas(ontologyFile);
        System.out.println("Writing derivations from lemmas in "+ontologyFile);
        System.out.println("Found "+lemmas.size()+" lemmas");
        try(final GSFeatures gs=new GSFeatures()) {
            derivationBuilder=new BruteForceDerivationBuilder(gs.getRegexLinguisticPhenomena(), lemmas);
        }
    }

    /**
     * Retrieve all lemmas in the given OWL File.
     *
     * @param ontologyFile the OWL file, using OntoLex-lemon
     * @return all written representations of canonical forms
     */
    private List<String> retrieveLemmas(String ontologyFile){
        try(final EntityManagerFactoryHelper emf=new FileEntityManagerFactoryHelper(ontologyFile)){
            final EntityManager em=emf.createEntityManager();
            final TypedQuery<LangString> allEntriesQuery=em.createNativeQuery("SELECT ?w WHERE{\n"+
                    "\t?e <"+ Ontolex.CANONICAL_FORM_OBJ_PROPERTY+"> ?f.\n"+
                    "\t?f <"+Ontolex.WRITTEN_REP_DATA_PROPERTY+"> ?w\n"+
                    "}", LangString.class);
            return allEntriesQuery.getResultStream().map(LangString::getValue).toList();
        }
    }

    @Override
    public void accept(final String sicilianVocabularyEntry) {
        final int numwords=sicilianVocabularyEntry.split("\\s").length;
        if (numwords>1){
            System.err.println("IGNORED "+sicilianVocabularyEntry);
            return;
        }

        final long startTime = System.currentTimeMillis();
        final String sicilianVocabularyEntryWithAccent = accentExplicitor.addGraveAccent(sicilianVocabularyEntry);
        derivationBuilder.apply(sicilianVocabularyEntryWithAccent);
        final long endTime = System.currentTimeMillis();
        final long elapsedTime = endTime - startTime;
        totalProcessingTime += elapsedTime;
        System.out.println((processedEntries++) + " " + sicilianVocabularyEntry + "(" + sicilianVocabularyEntryWithAccent + "): elapsed time " + elapsedTime + ", total time " + totalProcessingTime + ".");
    }

    /**
     * write a set of rows representing the current derivations for the Nicosia e Sperilnga's lemmas.
     *
     * @param out the output stream
     * @throws IOException if unable to write to the output stream
     */
    public int writeNearestShortestDerivations(final Appendable out) throws IOException {
        return derivationBuilder.write(out, LinguisticPhenomena.DEFAULT_LABEL_PROVIDER, Locale.ENGLISH);
    }


    public static void main(final String[] args) throws IOException {
        final String ontologyFile=args[0];
        final String outputFile=args[1];
        System.out.println("Writing derivations from Sicilian of lemmas in "+ontologyFile+" to "+outputFile);
        final Main m=new Main(ontologyFile);

        SicilianVocabulary.visit(m);
        try(final FileWriter w=new FileWriter(outputFile)){
            final int n=m.writeNearestShortestDerivations(w);
            System.out.println("Processed "+m.processedEntries+" entries");
            System.out.println("Total processing time "+m.totalProcessingTime);
            System.out.println("Found etymon for "+n+" lemmas");
        }
    }
}
