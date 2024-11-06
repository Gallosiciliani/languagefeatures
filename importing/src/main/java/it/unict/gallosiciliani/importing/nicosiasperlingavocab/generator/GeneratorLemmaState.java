package it.unict.gallosiciliani.importing.nicosiasperlingavocab.generator;

import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lexinfo.PartOfSpeech;

import java.util.LinkedList;
import java.util.List;

/**
 * Base state for cases in which a lemma and no relevant Part Of Speech has been processed.
 * @author Cristiano Longo
 */
class GeneratorLemmaState extends GeneratorState{

    protected final List<String> lemmas;

    GeneratorLemmaState(final GeneratorStateParams params, final List<String> lemmas){
        super(params);
        this.lemmas=lemmas;
    }

    @Override
    final GeneratorState noun() {
        return new GeneratorNounState(params, lemmas, generateEntries(params.getPOSIndividualProvider().getNoun()));
    }

    @Override
    final GeneratorState verb() {
        return new GeneratorVerbState(params, lemmas, generateEntries(params.getPOSIndividualProvider().getVerb()));
    }

    /**
     * Generate all entries corresponding to recognized lemmas
     *
     * @param partOfSpeechIndividual@return all the generated entries
     */
    private List<LexicalEntry> generateEntries(final PartOfSpeech partOfSpeechIndividual){
        final List<LexicalEntry> createdEntries=new LinkedList<>();
        for(final String lemma : lemmas)
            if (params.getDuplicatesHandler().handle(lemma)) {
                final LexicalEntry e = new LexicalEntry();
                final IRIProvider.LexicalEntryIRIProvider iris = params.getIRIProvider().getLexicalEntryIRIs();
                e.setId(iris.getLexicalEntryIRI());
                e.setPartOfSpeech(partOfSpeechIndividual);
                e.setCanonicalForm(new Form());
                e.getCanonicalForm().setId(iris.getCanonicalFormIRI());
                e.getCanonicalForm().setWrittenRep(lemma);
                createdEntries.add(e);
                params.getConsumer().accept(e);
            }
        return createdEntries;
    }
}
