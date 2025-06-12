package it.unict.gallosiciliani.pdfimporter;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

/**
 * Test for {@link FilterOutPolyrematicConsumer}
 *
 * @author Cristiano Longo
 */
public class FilterOutPolyrematicConsumerTest {

    private final Consumer<LexicalEntry> delegate=mock(LexicalEntryConsumer.class);
    private final FilterOutPolyrematicConsumer consumer=new FilterOutPolyrematicConsumer(delegate);

    @Test
    void shouldDiscardPolyrematic(){
        final LexicalEntry e=new LexicalEntry();
        e.setCanonicalForm(new Form());
        e.getCanonicalForm().setWrittenRep(new MultilingualString().set("en", "polyrematic are expressions containing spaces"));

        consumer.accept(e);
        verifyNoInteractions(delegate);
    }

    @Test
    void shouldAcceptNonPolyrematic(){
        final LexicalEntry e=new LexicalEntry();
        e.setCanonicalForm(new Form());
        e.getCanonicalForm().setWrittenRep(new MultilingualString().set("mis", "nonpolyrematic"));

        consumer.accept(e);
        verify(delegate).accept(e);
    }
}
