package it.unict.gallosiciliani.importing.iri;

/**
 * Provide IRIs for an etymology of a specified {@link it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry}
 */
public interface EtymologyIRIProvider {
    /**
     * IRI of the novel etymology
     *
     * @return IRI of the etymology
     */
    String getEtymolgyIRI();

    /**
     * IRI of the first {@link it.unict.gallosiciliani.model.lemonety.EtyLink} of this
     * etymology.
     *
     * @return IRI of first EtyLink
     */
    String getEtyLinkIRI();

    /**
     * Create an IRI for a novel etySource for the first link of the etymology
     * @return IRI for a novel etySource
     */
    String getEtySourceIRI();
}
