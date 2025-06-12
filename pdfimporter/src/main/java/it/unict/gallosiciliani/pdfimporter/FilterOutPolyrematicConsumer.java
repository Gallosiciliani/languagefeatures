package it.unict.gallosiciliani.pdfimporter;

import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;

import java.util.function.Consumer;

/**
 * Only lexical entries with a written representation of a single world
 * will be passed to the delegate consumer.
 *
 * @author Cristiano Longo
 */
public class FilterOutPolyrematicConsumer implements Consumer<LexicalEntry> {

    private final Consumer<LexicalEntry> delegate;

    /**
     *
     * @param delegate the consumer which will receive selected entries
     */
    FilterOutPolyrematicConsumer(final Consumer<LexicalEntry> delegate){
        this.delegate=delegate;
    }

    @Override
    public void accept(final LexicalEntry lexicalEntry) {
        if (!lexicalEntry.getCanonicalForm().getWrittenRep().get().contains(" "))
            delegate.accept(lexicalEntry);
    }
}
