package it.unict.gallosiciliani.importing.iri;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link SequentialIRIProvider}
 */
public class SequentialIRIProviderTest {

    private static final String NS="http://test.org/lexicon#";

    private final Set<String> produced=new TreeSet<>();

    @Test
    void shouldProduceDifferentIRIS(){
        //just produce a lot of IRIs anche check that all are right
        final IRIProvider p=new SequentialIRIProvider(NS);
        final LexicalEntryIRIProvider e1=p.getLexicalEntryIRIs();
        checkProduced(e1.getLexicalEntryIRI());
        checkProduced(e1.getCanonicalFormIRI());

        final EtymologyIRIProvider e1ety1=e1.getEtymologyIRIs();
        checkProduced(e1ety1.getEtymolgyIRI());
        checkProduced(e1ety1.getEtyLinkIRI());
        checkProduced(e1ety1.getEtySourceIRI());
        checkProduced(e1ety1.getEtySourceIRI());

        final EtymologyIRIProvider e1ety2=e1.getEtymologyIRIs();
        checkProduced(e1ety2.getEtymolgyIRI());
        checkProduced(e1ety2.getEtyLinkIRI());
        checkProduced(e1ety2.getEtySourceIRI());

        final LexicalEntryIRIProvider e2=p.getLexicalEntryIRIs();
        checkProduced(e2.getLexicalEntryIRI());
        checkProduced(e2.getCanonicalFormIRI());

        final EtymologyIRIProvider e2ety1=e2.getEtymologyIRIs();
        checkProduced(e2ety1.getEtymolgyIRI());
        checkProduced(e2ety1.getEtyLinkIRI());
        checkProduced(e2ety1.getEtySourceIRI());
    }

    void checkProduced(final String iri){
        assertNotNull(iri);
        try {
            URI.create(iri);
        } catch(final IllegalArgumentException e){
            fail("Illegal IRI "+iri);
        }
        assertTrue(iri.startsWith(NS));
        assertTrue(produced.add(iri),"Duplicate "+iri);
        System.out.println(iri);
    }
}
