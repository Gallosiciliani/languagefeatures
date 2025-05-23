package it.unict.gallosiciliani.pdfimporter.generator;

import java.util.List;

/**
 * State for {@link LexicalEntriesGenerator}: a lemma followed by a conjunction has been just received.
 *
 * @author Cristiano Longo
 */
public class GeneratorLemmaConjunctionState extends GeneratorLemmaState{

    GeneratorLemmaConjunctionState(GeneratorStateParams params, List<String> lemmas) {
        super(params, lemmas);
    }

    @Override
    GeneratorState lemma(final String lemma) {
        lemmas.add(lemma);
        return new GeneratorLemmaNoPosState(params, lemmas);
    }

    /**
     * Encountered a token indicating a lemma conjunctor
     * @return destination state
     */
    GeneratorState conjunction(){
        throw new IllegalStateException("Unexpected double consecutive conjunctions");
    }


}
