package it.unict.gallosiciliani.webapp.lexica;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.webapp.persistence.PersistenceTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

/**
 * A lexicon and three lexical entries associated with it.
 *
 * @author Cristiano Longo
 */
public class LexiconWithThreeEntries {

    public enum Entry{
        A,B,C
    }

    /**
     * Performs a http request
     */
    @FunctionalInterface
    interface RetrieveLexiconRequestPerformer{
        ResultActions perform(final LexiconWithThreeEntries l3e) throws Exception;
    }

    //Identifiers of lexical entries in the scope of the lexicon namespace
    public static final String ENTRY_A_ID = "y";
    private static final String ENTRY_B_ID = "z";
    private static final String ENTRY_C_ID = "x";

    public final LexInfo lexInfo = new LexInfo();
    public final Lexicon lexicon = new Lexicon();
    public final LexicalEntry entryA = new LexicalEntry();
    public final LexicalEntry entryB = new LexicalEntry();
    public final LexicalEntry entryC = new LexicalEntry();

    public final LexiconPageSelector[] pageSelectors={new LexiconPageSelector("A-B","^[ab].*"),
            new LexiconPageSelector("C-Z","^[^ab].*")};

    public final String[] pageLabels={"A-B", "C-Z"};
    /**
     * All entries, alphabetically sorted
     */
    public final List<LexicalEntry> entriesSorted = new ArrayList<>(3);

    /**
     * Entries on the first page, alphabetically sorted
     */
    public final List<LexicalEntry> pageABEntries = new ArrayList<>(3);

    /**
     *
     * @param lexiconIRI IRI of the lexicon
     * @param entriesNS name space for entries in this lexicon
     */
    public LexiconWithThreeEntries(final String lexiconIRI, final String entriesNS){
        lexicon.setId(lexiconIRI);
        lexicon.setTitle("A lexicon for test purposes");

        setLexicalEntry(entryC, entriesNS+ENTRY_C_ID, "carota");
        entryC.setPartOfSpeech(lexInfo.noun);
        setLexicalEntry(entryA, entriesNS+ENTRY_A_ID, "arancia");
        entryA.setPartOfSpeech(lexInfo.noun);
        setLexicalEntry(entryB,entriesNS+ENTRY_B_ID, "bandire");
        entryB.setPartOfSpeech(lexInfo.verb);

        lexicon.getEntry().add(entryC);
        lexicon.getEntry().add(entryA);
        lexicon.getEntry().add(entryB);

        entriesSorted.add(entryA);
        entriesSorted.add(entryB);
        entriesSorted.add(entryC);

        pageABEntries.add(entryA);
        pageABEntries.add(entryB);
    }

    public LexiconWithThreeEntries(){
        this("http://test.org/lexica/lexicon#lexicon", "http://test.org/lexica/lexicon");
    }

    public LexicalEntry getEntry(final Entry e){
        return switch (e) {
            case A -> entryA;
            case B -> entryB;
            case C -> entryC;
        };
    }

    /**
     * Create and persist a lexical entry with a specified canonical form
     *
     * @param iri entry IRI
     * @param lemma canonical form
     */
    private static void setLexicalEntry(final LexicalEntry entry, final String iri, final String lemma){
        entry.setId(iri);
        final Form form = new Form();
        form.setId(iri+"-form");
        entry.setCanonicalForm(form);
        form.setWrittenRepUndLang(lemma);
        final Etymology etymology = new Etymology();
        etymology.setId(iri+"-etymology");
        entry.getEtymology().add(etymology);
        final EtyLink etyLink = new EtyLink();
        etymology.setStartingLink(etyLink);
        etyLink.setId(iri+"-etyLink");
        etyLink.setEtyTarget(entry);
        etyLink.setEtySubTarget(form);

    }

    /**
     * Persist lexicon and lexical entries using the specified entity manager
     *
     * @param entityManager the entity manager
     *                      <p>
     *                      This method must be run inside a transaction
     */
    public void persist(final EntityManager entityManager){
        new PersistenceTestUtils().persist(lexInfo.noun)
                .persist(lexInfo.verb)
                        .add(persist(entryC))
                                .add(persist(entryA))
                                        .add(persist(entryB))
                                                .persist(lexicon)
                                                    .execute(entityManager);
    }

    private PersistenceTestUtils persist(final LexicalEntry e){
        return PersistenceTestUtils.build().persist(e.getCanonicalForm()).persist(e);
    }

    /**
     * Remove the lexicon from the knowledge base using the specified entity manager
     *
     * @param entityManager the entity manager
     *                      <p>
     *                      This method must be run inside a transaction
     */
    public void cleanup(final EntityManager entityManager){
        new PersistenceTestUtils()
                .remove(lexicon)
                .remove(entryB)
                .remove(entryA)
                .remove(entryC)
                .remove(lexInfo.verb)
                .remove(lexInfo.noun)
                .execute(entityManager);
    }

    /**
     * Create subcomponents for the etymon of the specified entry
     *
     * @param e entry
     * @param fullEtymon etymon as string
     * @param components etymon components
     * @param componentsNormalized same etymon components, but normalized (removed accent, lowercased, ...)
     * @return the generated forms representing the etymon components
     */
    public Form[] setEtymonSubcomponents(final Entry e, final String fullEtymon, final String[] components, final String[] componentsNormalized){
        final LexicalEntry entry = getEntry(e);
        final Etymology etyA = entry.getEtymology().iterator().next();
        etyA.setLabel(fullEtymon);
        final EtyLink linkA = etyA.getStartingLink();
        linkA.setEtySource(null);
        int i =0 ;
        final Form[] res = new Form[components.length];
        for(final String component : components){
            final Form etymonForm = new Form();
            etymonForm.setId("http://www.example.org/latinForm"+i);
            etymonForm.setLabel(component);
            etymonForm.setWrittenRep(new MultilingualString().set(componentsNormalized[i]));
            linkA.getEtySubSource().add(etymonForm);
            res[i++]=etymonForm;
        }
        return res;
    }
}
