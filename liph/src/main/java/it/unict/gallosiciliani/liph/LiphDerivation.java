package it.unict.gallosiciliani.liph;

/**
 * A derivation step from a specific written representation
 * of a source Lexical Object to a specific written representation of
 * a target Lexical Object, performed by a linguistic phenomenon.
 *
 * @author Cristiano Longo
 */
public interface LiphDerivation {

    /**
     * IRI (or anonymous identifier) of the source Lexical Object
     * @return an IRI indicating an instance of LexicalObject
     */
    String getSourceIndividual();

    /**
     * Written representation of the source individual
     * @return written representation of the source individual
     */
    String getSourceWrittenRep();

    /**
     * The phenomenon connecting source and target
     * @return IRI of the linguistic phenomenon
     */
    LinguisticPhenomenon getPhenomenon();

    /**
     * IRI (or anonymous identifier) of the target Lexical Object
     * @return an IRI indicating an instance of LexicalObject
     */
    String getTargetIndividual();

    /**
     * Written representation of the target individual
     * @return written representation of the target individual
     */
    String getTargetWrittenRep();
}
