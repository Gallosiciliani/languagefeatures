package it.unict.gallosiciliani.importing.pdf.generator;

/**
 * Initial state for the {@link LexicalEntriesGenerator}
 *
 * @author Cristiano Longo
 */
class GeneratorInitialState extends GeneratorState{
    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     */
    GeneratorInitialState(final GeneratorStateParams params) {
        super(params);
    }

    @Override
    public GeneratorState lemma(final String lemma) {
        return new GeneratorLemmaNoPosState(params, lemma);
    }

    @Override
    GeneratorState noun() {
        throw new IllegalArgumentException("Part Of Speech noun not allowed at the beginning.");
    }

    @Override
    GeneratorState verb() {
        throw new IllegalArgumentException("Part Of Speech verb not allowed at the beginning.");
    }

    @Override
    GeneratorState ignoredPOS() {
        throw new IllegalArgumentException("Part Of Speech not allowed at the beginning.");
    }

    @Override
    public String toString(){
        return "GeneratorInitialState";
    }
}
