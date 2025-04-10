package it.unict.gallosiciliani.webapp.persistence;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.Query;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.PartOfSpeech;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.owl.Thing;

import java.util.LinkedList;
import java.util.List;

/**
 * Utilities to persist and remove entities
 *
 * @author Cristiano Longo
 */
public class PersistenceTestUtils {


    private interface Action{
        void execute(EntityManager entityManager);
    }

    private final List<Action> actions = new LinkedList<>();

    public static PersistenceTestUtils build(){
        return new PersistenceTestUtils();
    }

    public PersistenceTestUtils updateQuery(final String sparqlUpdateQuery) {
        actions.add((final EntityManager entityManager) -> {
            final Query q = entityManager.createNativeQuery(sparqlUpdateQuery);
            q.executeUpdate();
        });
        return this;
    }

    public PersistenceTestUtils persist(final PartOfSpeech pos) {
        actions.add((final EntityManager entityManager) -> entityManager.persist(pos));
        return this;
    }

    public PersistenceTestUtils persist(final Lexicon lexicon) {
        actions.add((final EntityManager entityManager) -> entityManager.persist(lexicon));
        return this;
    }

    public PersistenceTestUtils persist(final LexicalEntry entry) {
        actions.add((final EntityManager entityManager) -> {
            if (entry.getCanonicalForm()!=null)
                entityManager.persist(entry.getCanonicalForm());
            entityManager.persist(entry);
        });
        return this;
    }

    public PersistenceTestUtils persist(final LexicalObject form){
        actions.add((final EntityManager entityManager) -> entityManager.persist(form));
        return this;
    }

    public PersistenceTestUtils persist(final Etymology etymology){
        actions.add((final EntityManager entityManager) -> entityManager.persist(etymology));
        return this;
    }

    public PersistenceTestUtils persist(final LinguisticPhenomenon p){
        actions.add((final EntityManager entityManager) -> entityManager.persist(p));
        return this;
    }

    /**
     * Persist an assertion
     *
     * @param subject statement subject
     * @param popertyIRI statement property
     * @param object statement object
     * @return this object
     */
    public PersistenceTestUtils persist(final Thing subject, final String popertyIRI, final Thing object){
        return updateQuery("INSERT DATA {<"+subject.getId()+"> <"+popertyIRI+"> <"+object.getId()+">}");
    }

    /**
     * Persist an assertion
     *
     * @param src assertion subject
     * @param p asserted property
     * @param dst assertion object
     * @return this object
     */
    public PersistenceTestUtils persist(final LexicalObject src, final LinguisticPhenomenon p, final LexicalObject dst){
        return persist(src, p.getId(), dst);
    }

    public PersistenceTestUtils remove(final PartOfSpeech pos) {
        actions.add((final EntityManager entityManager) -> {
            final PartOfSpeech p = entityManager.merge(pos);
            entityManager.remove(p);
        });
        return this;
    }

    public PersistenceTestUtils remove(final Lexicon lexicon) {
        actions.add((final EntityManager entityManager) -> {
            final Lexicon l = entityManager.merge(lexicon);
            entityManager.remove(l);
        });
        return this;
    }

    public PersistenceTestUtils remove(final LexicalEntry entry) {
        actions.add((final EntityManager entityManager) -> {
            final LexicalEntry e = entityManager.merge(entry);
            entityManager.remove(e);
            if (entry.getCanonicalForm()!=null && entry.getCanonicalForm().getId()!=null) {
                final Form f = entityManager.merge(entry.getCanonicalForm());
                entityManager.remove(f);
            }
        });
        return this;
    }

    public PersistenceTestUtils remove(final Form actual) {
        actions.add((final EntityManager entityManager) -> {
            final Form f = entityManager.merge(actual);
            entityManager.remove(f);
        });
        return this;
    }

    public PersistenceTestUtils remove(final Etymology etymology) {
        actions.add((final EntityManager entityManager) -> {
            final Etymology e = entityManager.merge(etymology);
            entityManager.remove(e);
        });
        return this;
    }

    public PersistenceTestUtils refresh(final Object o) {
        actions.add((final EntityManager entityManager) -> entityManager.refresh(o));
        return this;
    }

    /**
     * Add all the action of the builder parameter to this builder
     * @param builder a builder
     * @return this builder
     */
    public PersistenceTestUtils add(final PersistenceTestUtils builder){
        actions.addAll(builder.actions);
        return this;
    }

    /**
     * Execute all actions in a dedicated transaction and using the specified entity manager
     */
    public void execute(final EntityManager entityManager) {
        entityManager.getTransaction().begin();
        for(final Action action : actions)
            action.execute(entityManager);
        entityManager.getTransaction().commit();
    }
}
