package it.unict.gallosiciliani.webapp.ontologies;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provide linguistic phenomena instances with the specified IRI
 *
 * @author Cristiano Longo
 * TODO move into liph core
 */
public class LinguisticPhenomenaProvider {
    private final List<LinguisticPhenomenon> phenomena;
    private final Map<String, LinguisticPhenomenon> phenomenaById=new TreeMap<>();

    /**
     * Create a provider for the specified phenomena
     * @param phenomena a set of {@link it.unict.gallosiciliani.liph.LinguisticPhenomena}
     */
    public LinguisticPhenomenaProvider(final List<LinguisticPhenomenon> phenomena){
        this.phenomena=phenomena;
        phenomena.forEach((p)-> phenomenaById.put(p.getId(), p));
    }
    /**
     * Get the linguistic phenomenon with the specified IRI
     * @param iri IRI of the linguistic phenomena we are looking for
     * @return the linguistic phenomenon with the specified IRI, if any. Null, otherwise.
     */
    public LinguisticPhenomenon getById(final String iri){
        return phenomenaById.get(iri);
    }

    /**
     * Get all the available linguistic phenomena
     * @return all the available linguistic phenomena
     */
    public List<LinguisticPhenomenon> getAll(){
        return phenomena;
    }

}
