package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import java.util.LinkedList;
import java.util.List;

/**
 * State for {@link LexicalEntriesGenerator} in which the generator just processed a lemma.
 */
class GeneratorLemmaNoPosState extends GeneratorLemmaState{

    GeneratorLemmaNoPosState(final GeneratorStateParams params, final String lemma){
        super(params, new LinkedList<>());
        lemmas.add(lemma);
    }

    GeneratorLemmaNoPosState(final GeneratorStateParams params, final List<String> lemmas){
        super(params, lemmas);
    }

    @Override
    GeneratorState lemma(final String lemma) {
        return new GeneratorLemmaNoPosState(params, lemma);
    }

    /**
     * Encountered a token indicating a lemma conjunctor
     * @return destination state
     */
    GeneratorState conjunction(){
        return new GeneratorLemmaConjunctionState(params, lemmas);
    }

    @Override
    GeneratorState ignoredPOS(){
        return new GeneratorLemmaIgnoredPOSState(params, lemmas);
    }

    @Override
    public String toString(){
        return "GeneratorLemmaNoPosState("+lemmas+")";
    }

}
