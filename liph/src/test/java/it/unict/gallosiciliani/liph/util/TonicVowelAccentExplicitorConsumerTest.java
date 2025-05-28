package it.unict.gallosiciliani.liph.util;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;

/**
 * Test for {@link TonicVowelAccentExplicitorConsumer}
 * @author Cristiano Longo
 */
public class TonicVowelAccentExplicitorConsumerTest extends AbstractAccentExplicitorTest{

    private interface LexicalEntryConsumer extends Consumer<LexicalEntry>{}
    private final Consumer<LexicalEntry> consumerTarget=mock(LexicalEntryConsumer.class);
    private final InOrder o=inOrder(consumerTarget);
    private final TonicVowelAccentExplicitorConsumer mainConsumer=new TonicVowelAccentExplicitorConsumer(consumerTarget);

    @Override
    protected String getAccented(String expr) {
        final String lang="lang";
        final LexicalEntry e=new LexicalEntry();
        e.setCanonicalForm(new Form());
        e.getCanonicalForm().setWrittenRep(new MultilingualString().set(lang, expr));

        mainConsumer.accept(e);

        final ArgumentCaptor<LexicalEntry> actualEntryCaptor = ArgumentCaptor.forClass(LexicalEntry.class);
        o.verify(consumerTarget).accept(actualEntryCaptor.capture());
        return actualEntryCaptor.getValue().getCanonicalForm().getWrittenRep().get(lang);
    }
}
