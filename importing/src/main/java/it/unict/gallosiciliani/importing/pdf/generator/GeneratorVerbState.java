package it.unict.gallosiciliani.importing.pdf.generator;

import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;

import java.util.List;

/**
 * The entry has been recognized as verb
 */
class GeneratorVerbState extends GeneratorPOSState{
    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params       construction parameters
     * @param lemmas         parsed lemmas
     * @param createdEntries entries created for each lemma
     */
    GeneratorVerbState(final GeneratorStateParams params, List<String> lemmas, List<LexicalEntry> createdEntries) {
        super(params, lemmas, createdEntries);
    }

    @Override
    GeneratorState noun() {
        generateAdditionalEntries(params.getPOSIndividualProvider().getNoun());
        return new GeneratorNounVerbState(params);
    }

    @Override
    public String toString(){
        return "GeneratorVerbState("+lemmas+")";
    }
}
