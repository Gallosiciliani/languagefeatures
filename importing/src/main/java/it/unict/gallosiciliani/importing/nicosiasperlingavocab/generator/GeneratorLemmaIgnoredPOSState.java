package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import java.util.List;

/**
 * One or more lemmas with just ignored part of speech has been encountered
 */
public class GeneratorLemmaIgnoredPOSState extends GeneratorLemmaState{

    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     * @param lemmas lemmas recognized up to now.
     */
    GeneratorLemmaIgnoredPOSState(final GeneratorStateParams params, final List<String> lemmas) {
        super(params, lemmas);
    }

    @Override
    GeneratorState lemma(final String lemma) {
        return new GeneratorLemmaNoPosState(params, lemma);
    }

    @Override
    public String toString(){
        return "GeneratorLemmaIgnoredPosState("+lemmas+")";
    }

}
