package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.derivations.DerivationBuilder;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPrinter;
import it.unict.gallosiciliani.derivations.NearestShortestDerivation;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexFeatureQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test to verify whether the {@link it.unict.gallosiciliani.derivations.DerivationBuilder} works good
 * when applied to gallo-sicilian features
 * @author Cristiano Longo
 */
public class GSDerivationsTest {

    private static final String NS= GSFeatures.NS;

    @Test
    void testAbento() throws IOException {
        testDerivation("abentö","abbentu", NS+"degem.6",NS+"vocal.5");
    }

    @Test
    @Disabled
    void testBoche() throws IOException{
        testDerivation("böchè","abbuccari", NS+"degem.6", NS+"degem.7", NS+"palat.1");
    }

    @Test
    @Disabled
    void testTrepuodeno() throws IOException{
        testDerivation("trepuòdenö","ṭṛipòdinu","ditt.2","vocal.7","deretr.1");
    }

    private void testDerivation(final String lemma,
                                final String candidateEtymon,
                                final String...expectedFeatures) throws IOException {
        try(final GSFeatures gs=GSFeatures.loadLocal()) {
            final List<? extends LinguisticPhenomenon> gsFeatures = RegexLinguisticPhenomenaReader.read(gs.getModel(),
                    new RegexFeatureQuery().ignoreDeprecated()).getFeatures();
            final DerivationPrinter printer=new DerivationPrinter(GSFeatures.LABEL_PROVIDER_ID);
            final NearestShortestDerivation actualDerivations = new NearestShortestDerivation(lemma);
            new DerivationBuilder(gsFeatures, List.of(actualDerivations)).apply(candidateEtymon);

            for (final DerivationPathNode actual : actualDerivations.getDerivation()) {
                final Set<String> actualFeatureSet=extractPhenomena(actual);
                System.out.println(printer.print(actual, Locale.ENGLISH));
                if (actualFeatureSet.containsAll(Arrays.asList(expectedFeatures)))
                    return;
            }
            fail("Expected derivation not found: "+ Arrays.toString(expectedFeatures));
        }
    }

    private Set<String> extractPhenomena(final DerivationPathNode n){
        final Set<String> res=new TreeSet<>();
        if (n.prev()!=null) {
            res.add(n.getLinguisticPhenomenon().getIRI());
            res.addAll(extractPhenomena(n.prev()));
        }
        return res;
    }
}
