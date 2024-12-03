package it.unict.gallosiciliani.importing.pdf.generator;

/**
 * One or both lemmas has been recognized with both noun and verb part of speech
 *
 * @author Cristiano Longo
 */
class GeneratorNounVerbState extends GeneratorState{
    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     */
    GeneratorNounVerbState(final GeneratorStateParams params) {
        super(params);
    }

    @Override
    GeneratorState lemma(final String lemma) {
        return new GeneratorLemmaNoPosState(params, lemma);
    }

    @Override
    public String toString(){
        return "GeneratorNounVerbState";
    }
}
