package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lexinfo.PartOfSpeech;

import java.util.List;

/**
 * One or more lemmas and a single POS has been encountered.
 */
class GeneratorPOSState extends GeneratorState{
    //parsed lemmas
    protected final List<String> lemmas;

    //entries created for each lemma
    protected final List<LexicalEntry> createdEntries;

    /**
     * Create a generator state referring to the specified consumer
     *
     * @param params construction parameters
     * @param lemmas parsed lemmas
     * @param createdEntries entries created for each lemma
     */
    GeneratorPOSState(final GeneratorStateParams params,
                      final List<String> lemmas, final List<LexicalEntry> createdEntries) {
        super(params);
        this.lemmas=lemmas;
        this.createdEntries=createdEntries;
    }

    /**
     * The parser returned a lemma
     * @param lemma the recognized lemma
     * @return destination state
     */
    final GeneratorState lemma(final String lemma){
        return new GeneratorLemmaNoPosState(params, lemma);
    }

    /**
     * Generate a novel entry for each one in createdEntries, with the same canonical form but with the specified Part Of Speech
     *
     * @param posIndividual individual for the Part Of Speech
     */
    protected final void generateAdditionalEntries(final PartOfSpeech posIndividual){
        for(final LexicalEntry createdEntry : createdEntries){
            IRIProvider.LexicalEntryIRIProvider iris = params.getIRIProvider().getLexicalEntryIRIs();
            final LexicalEntry newEntry = new LexicalEntry();
            newEntry.setId(iris.getLexicalEntryIRI());
            newEntry.setPartOfSpeech(posIndividual);
            newEntry.setCanonicalForm(createdEntry.getCanonicalForm());
            params.getConsumer().accept(newEntry);
        }
    }
}
