package it.unict.gallosiciliani.importing.persistence;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.importing.partofspeech.POSIndividualProvider;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.lime.Lime;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
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

    static final Comparator<Form> compareByIRI= (f1, f2) -> f1.getId().compareTo(f2.getId());
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

    @Override
    public void accept(final LexicalEntry lexicalEntry) {
        if (lexicalEntry.getCanonicalForm()!=null && forms.add(lexicalEntry.getCanonicalForm()))
                entityManager.persist(lexicalEntry.getCanonicalForm());

        entityManager.persist(lexicalEntry);

        lexicon.getEntry().add(lexicalEntry);
        // Cause concurrent modification exception, see https://github.com/kbss-cvut/jopa/issues/274
        // entityManager.merge(lexicon);
        // the following is a workaround
        entityManager.createNativeQuery("INSERT DATA {<"+lexicon.getId()+"> <"+Lime.ENTRY_OBJ_PROPERTY+"> <"+lexicalEntry.getId()+">}")
                .executeUpdate();
        numEntries++;
    }
}
