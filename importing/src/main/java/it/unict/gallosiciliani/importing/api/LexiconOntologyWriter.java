package it.unict.gallosiciliani.importing.api;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lime;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.owl.Thing;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Utility class to create an ontology describing a lexicon
 *
 * @author Cristiano Longo
 */
@Slf4j
public class LexiconOntologyWriter implements Consumer<LexicalEntry> {

    static final Comparator<Form> compareByIRI= Comparator.comparing(Thing::getId);
    private final EntityManager entityManager;
    private final Lexicon lexicon;

    @Getter
    private final Set<Form> forms=new TreeSet<>(compareByIRI);

    @Getter
    private int numEntries;

    public LexiconOntologyWriter(final EntityManager entityManager, final POSIndividualProvider posProvider){
        this.entityManager=entityManager;
        Lexicon l=entityManager.createNativeQuery("SELECT ?iri WHERE {?iri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <"+ Lime.LEXICON_CLASS+"> }", Lexicon.class)
                .getSingleResult();
        lexicon=entityManager.find(Lexicon.class, l.getId());
        log.info("Found lexicon {}", lexicon);
        //persist PartOfSpeech individuals
        entityManager.getTransaction().begin();
        entityManager.persist(posProvider.getNoun());
        entityManager.persist(posProvider.getVerb());
        entityManager.getTransaction().commit();
    }

    public String getLexiconLanguage(){
        return lexicon.getLanguage();
    }
    @Override
    public void accept(final LexicalEntry lexicalEntry) {
        for(final Form f: getAllForms(lexicalEntry))
            if (forms.add(f))
                entityManager.persist(f);

        entityManager.persist(lexicalEntry);

        lexicon.getEntry().add(lexicalEntry);
        // Cause concurrent modification exception, see https://github.com/kbss-cvut/jopa/issues/274
        // entityManager.merge(lexicon);
        // the following is a workaround
        entityManager.createNativeQuery("INSERT DATA {<"+lexicon.getId()+"> <"+Lime.ENTRY_OBJ_PROPERTY+"> <"+lexicalEntry.getId()+">}")
                .executeUpdate();
        numEntries++;
    }

    /**
     * Get all the forms in a lexical entry
     * @param lexicalEntry the lexical entry
     * @return all forms in the lexical entry
     */
    public Set<Form> getAllForms(final LexicalEntry lexicalEntry){
        final Set<Form> res=new TreeSet<>(compareByIRI);
        if (lexicalEntry.getCanonicalForm()!=null)
            res.add(lexicalEntry.getCanonicalForm());
        if (lexicalEntry.getEtymology()==null)
            return res;
        for(final Etymology etymology: lexicalEntry.getEtymology()){
            if (etymology.getStartingLink()!=null)
                res.addAll(etymology.getStartingLink().getEtySubSource());
        }
        return res;
    }
}
