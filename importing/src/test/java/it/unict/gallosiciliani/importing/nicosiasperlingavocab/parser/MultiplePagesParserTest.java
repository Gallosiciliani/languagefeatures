package it.unict.gallosiciliani.importing.nicosiasperlingavocab.parser;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

import java.io.IOException;

/**
 * Test for {@link MultiplePagesParser}
 */
public class MultiplePagesParserTest {

    @Test
    void testParsingMultiplePages() throws IOException {
        try(final Parser p=mock(Parser.class)){
            final MultiplePagesParser m=new MultiplePagesParser(p);
            final InOrder o=inOrder(p);
            m.parsePages(1,3);
            o.verify(p).parsePage(1);
            o.verify(p).parsePage(2);
            o.verify(p).parsePage(3);
            o.verifyNoMoreInteractions();
        }
    }
}
