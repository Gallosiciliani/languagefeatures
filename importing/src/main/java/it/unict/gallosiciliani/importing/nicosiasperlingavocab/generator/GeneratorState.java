package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser.ParsingDataConsumer;

/**
 * State of {@link LexicalEntriesGenerator}, receive same events as {@link ParsingDataConsumer}
 *
 * @author Cristiano Longo
 */
abstract class GeneratorState{

    protected final GeneratorStateParams params;

    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     */
    GeneratorState(final GeneratorStateParams params){
        this.params=params;
    }

    /**
     * The parser returned a lemma
     * @param lemma the recognized lemma
     * @return destination state
     */
    GeneratorState lemma(final String lemma){
        return this;
    }

    /**
     * Encountered a token indicating that the current lemma(s) is a noun
     * @return destination state
     */
    GeneratorState noun(){
        return this;
    }

    /**
     * Encountered a token indicating that the current lemma(s) is a verb
     * @return destination state
     */
    GeneratorState verb(){
        return this;
    }

    /**
     * Encountered a token indicating a POS that has to be ignored
     * @return destination state
     */
    GeneratorState ignoredPOS(){
        return this;
    }

    /**
     * Encountered a token indicating a lemma conjunctor
     * @return destination state
     */
    GeneratorState conjunction(){
        return this;
    }
}
