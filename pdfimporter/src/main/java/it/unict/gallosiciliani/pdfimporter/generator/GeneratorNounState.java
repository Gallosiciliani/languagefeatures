package it.unict.gallosiciliani.pdfimporter.generator;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.List;

/**
 * The entry has been recognized as noun
 */
class GeneratorNounState extends GeneratorPOSState{
    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     * @param lemmas         parsed lemmas
     * @param createdEntries entries created for each lemma
     */
    GeneratorNounState(final GeneratorStateParams params, final List<String> lemmas, final List<LexicalEntry> createdEntries) {
        super(params, lemmas, createdEntries);
    }

    @Override
    GeneratorState verb() {
        generateAdditionalEntries(params.getPOSIndividualProvider().getVerb());
        return new GeneratorNounVerbState(params);
    }

    @Override
    public String toString(){
        return "GeneratorNounState("+lemmas+","+createdEntries+")";
    }
}
