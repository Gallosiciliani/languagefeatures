package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

/**
 * Consume tokens returned by the {@link Parser}
 */
public interface ParsingDataConsumer {
    /**
     * The parser returned a lemma
     * @param lemma the recognized lemma
     */
    void lemma(final String lemma);

    /**
     * The parsed returned a part of speech
     * @param pos string representing the Part Of Speech
     */
    void pos(final String pos);

    /**
     * The parser encountered a conjunction between two lemmas
     */
    void conjunction();
}
