package it.unict.gallosiciliani.webapp.lexica;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.query.TypedQuery;
import cz.cvut.kbss.jopa.vocabulary.RDF;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.model.lemon.lime.Lime;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lemon.ontolex.Ontolex;
import it.unict.gallosiciliani.model.lemonety.LemonEty;
import it.unict.gallosiciliani.model.lexinfo.LexInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

/**
 * Provide access to the lexica in the knowledge base
 */
@Slf4j
@Service
public class LexicaService {

    @Autowired
    EntityManager entityManager;

    /**
     * Get all the lexica in the knowledge base
     * @return all the individuals of class {@link Lexicon} in the knowledge base
     */
    public List<Lexicon> findAllLexica(){
        return entityManager.createNativeQuery("SELECT ?x WHERE { ?x a <"+ Lime.LEXICON_CLASS+"> . } ORDER BY ?x",
                        Lexicon.class).getResultList();
    }

    /**
     * Find the lexicon with the specified IRI, if any.
     * @param iri lexicon IRI
     * @return the lexicon with the specified IRI, if any. Null, otherwise.
     */
    @Transactional
    public Lexicon findLexiconByIRI(final String iri){
        return entityManager.find(Lexicon.class, iri);
    }

    /**
     * Retrieve all the entries of a lexicon sorted by lemma in alphabetic order and
     * filtered using the specified selector and regular expressions
     *
     * @param persistedLexicon a lexicon in the knowledge base
     * @param selector selection criteria
     * @param regexLemmaFilter a regular expression to filter on lemmas written representation
     *
     * @return entries of the lexicon
     */
    @Transactional
    public List<LexicalEntry> findEntries(final Lexicon persistedLexicon,
                                          final EntrySelector selector,
                                          final String regexLemmaFilter){
        final String selectByPOSPattern = EntrySelector.ALL.getPos().equals(selector.getPos()) ? "" : "?x <"+LexInfo.PART_OF_SPEECH_OBJ_PROPERTY+"> ?pos .";
        final String selectByFeatureTypePattern = EntrySelector.ALL.getFeatureType().equals(selector.getFeatureType()) ? "" :
                "?x <" + LemonEty.ETYMOLOGY_OBJ_PROPERTY + "> ?etymology ."+
                        "?etymology <" + RDF.TYPE + "> ?etymologytype ."+
                        "?etymologytype <" + RDFS.SUB_CLASS_OF + "> ?featuretype .";

        final TypedQuery<LexicalEntry> query = entityManager.createNativeQuery("SELECT DISTINCT ?x WHERE {"+
                        "?lexicon <"+Lime.ENTRY_OBJ_PROPERTY+"> ?x ."+
                        selectByPOSPattern + selectByFeatureTypePattern +
                        "?x <"+ Ontolex.CANONICAL_FORM_OBJ_PROPERTY+"> ?f ."+
                        "?f <"+Ontolex.WRITTEN_REP_DATA_PROPERTY+"> ?r . "+
                        "FILTER regex(?r, \""+regexLemmaFilter+"\", \"i\") }"+
                        "ORDER BY ?r",
                LexicalEntry.class);
        query.setParameter("lexicon", persistedLexicon);
        if (!selectByPOSPattern.isEmpty())
            query.setParameter("pos", URI.create(selector.getPos()));
        if (!selectByFeatureTypePattern.isEmpty())
            query.setParameter("featuretype", URI.create(selector.getFeatureType()));
        return query.getResultList();
    }

}
