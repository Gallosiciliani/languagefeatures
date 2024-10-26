package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import it.unict.gallosiciliani.util.OntologyCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import static it.unict.gallosiciliani.gs.GSFeatures.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link GSFeatures} ontology
 * @author Cristiano Longo
 */
@Slf4j
public class GSFeaturesTest {

    private static final String[] ALL_PROPERTIES = {
            NICOSIA_FEATURE_OBJ_PROPERTY,
            NOVARA_DI_SICILIA_FEATURE_OBJ_PROPERTY,
            SAN_FRATELLO_FEATURE_OBJ_PROPERTY,
            SPERLINGA_FEATURE_OBJ_PROPERTY,
            NORTHERN_FEATURE_OBJ_PROPERTY,
            SOUTHERN_FEATURE_OBJ_PROPERTY
    };

    private void shouldContainBaseProperties(final GSFeatures g) throws Exception {
        shouldContainBaseProperties(g.getModel());
    }

    private void shouldContainBaseProperties(final Model m) throws Exception {
        final OntologyCheckUtils utils = new OntologyCheckUtils(m);
        for(final String expectedProperty : ALL_PROPERTIES)
            utils.objectPropertyExists(expectedProperty);
        assertTrue(utils.check(), utils.getFailureMessage());
    }

    @Test
    @Disabled
    void countProperties() throws IOException {
        final Query q = QueryFactory.create("SELECT (count(distinct ?f) as ?n) WHERE {"+
                " {?f <"+RDFS.subPropertyOf+"> <"+ NICOSIA_FEATURE_OBJ_PROPERTY+">}" +
                " UNION {?f <"+RDFS.subPropertyOf+"> <"+ SPERLINGA_FEATURE_OBJ_PROPERTY+">}" +
                " UNION {?f <"+RDFS.subPropertyOf+"> <"+ SAN_FRATELLO_FEATURE_OBJ_PROPERTY+">}" +
                " UNION {?f <"+RDFS.subPropertyOf+"> <"+ NOVARA_DI_SICILIA_FEATURE_OBJ_PROPERTY+">}" +
                "}");
        try(final GSFeatures gs = GSFeatures.loadOnline(); final QueryExecution ex = QueryExecution.create(q, gs.getModel())){
            final int expectedTotFeatures = GSLanguageFeatureCode.values().length;
            log.info("Features with regex {}", gs.getRegexLinguisticPhenomena().size());
            final int nPhenomena = ex.execSelect().nextSolution().getLiteral("n").getInt();
            log.info("Found {} linguistic phenomena", nPhenomena);
            assertEquals(expectedTotFeatures, nPhenomena);
        }
    }
    @Test
    void shouldLoadBaseLoadBaseProperties() throws Exception {
        shouldContainBaseProperties(GSFeatures.loadBase());
    }

    @Test
    void shouldLoadOnlineLoadBaseProperties() throws Exception {
        shouldContainBaseProperties(GSFeatures.loadLocal());
    }

    @Test
    void testReturningOntologyAsStr() throws Exception {
        try(final GSFeatures g = GSFeatures.loadLocal()) {
            final Model actual = RDFParser.create().fromString(g.getOntologyAsStr())
                    .lang(RDFLanguages.TTL)
                    .toModel();
            shouldContainBaseProperties(actual);
        }
    }

    @Test
    void testLoadingFeaturesFromCSV() throws IOException {
        final String csv = "N01b –(voc)LLU > voc+ö,nic,,,\"caö, beö, moö, ëö, gaö, vs. S18 \"\n"+
        "N02 -ANU/-ANE > [ˈɛã],,sfr,,mèan ‘mano’\n"+
        "N03a -ĀRE > à,,,nov,mangià ‘mangiare’\n"+
        "N03b -ĀRE > [ɛ(r(o̝))],nic,sfr,,\"nic. mangè, sfr. abrusger ‘bruciare’\"\n"+
                "S01 Ĕ(N) tonica > [ɛ(n)],nic,sfr,,\"nic. centö ‘cento’, capela ‘cappella’, sfr. fenestra, tèner ‘tenero’ \"\n"+
                "X01 Ē/Ĭ tonica > [e̝],nic,,,\"sëda ‘seta’, césgerö (vs. céisgerö) ‘cecio’\"";

        try(final GSFeatures g = GSFeatures.loadBase(); final Reader csvReader = new StringReader(csv)) {
            g.loadFeatures(csvReader);
            System.out.println(g.print());

            checkFeature(g.getModel(), GSLanguageFeatureCode.N01b, "N01b –(voc)LLU > voc+ö",
                    NORTHERN_FEATURE_OBJ_PROPERTY,
                    NICOSIA_FEATURE_OBJ_PROPERTY);
            checkFeature(g.getModel(), GSLanguageFeatureCode.N02, "N02 -ANU/-ANE > [ˈɛã]",
                    NORTHERN_FEATURE_OBJ_PROPERTY, SAN_FRATELLO_FEATURE_OBJ_PROPERTY);
            checkFeature(g.getModel(), GSLanguageFeatureCode.N03a, "N03a -ĀRE > à", NORTHERN_FEATURE_OBJ_PROPERTY,
                    NOVARA_DI_SICILIA_FEATURE_OBJ_PROPERTY);
            checkFeature(g.getModel(), GSLanguageFeatureCode.N03b, "N03b -ĀRE > [ɛ(r(o̝))]", NORTHERN_FEATURE_OBJ_PROPERTY,
                    NICOSIA_FEATURE_OBJ_PROPERTY, SAN_FRATELLO_FEATURE_OBJ_PROPERTY);
            checkFeature(g.getModel(), GSLanguageFeatureCode.S01, "S01 Ĕ(N) tonica > [ɛ(n)]", SOUTHERN_FEATURE_OBJ_PROPERTY,
                    NICOSIA_FEATURE_OBJ_PROPERTY, SAN_FRATELLO_FEATURE_OBJ_PROPERTY);
            checkFeature(g.getModel(), GSLanguageFeatureCode.X01, "X01 Ē/Ĭ tonica > [e̝]", NICOSIA_FEATURE_OBJ_PROPERTY, LinguisticPhenomena.LINGUISTIC_PHENOMENON_OBJ_PROPERTY);
        }
    }

    /**
     * Check a GS feature is reported in the ontology as expected
     * @param model the ontology
     * @param featureCode code of the feature
     * @param expectedLabel expected label
     * @param expectedSuperproperties expected set of super-properties
     */
    private void checkFeature(final Model model,
                              final GSLanguageFeatureCode featureCode,
                              final String expectedLabel,
                              final String...expectedSuperproperties){
        final String featureIRI = GSFeatures.NS+featureCode;
        final StringBuilder s = new StringBuilder("ASK {<"+featureIRI+"> <"+ RDFS.label.getURI() +"> \""+expectedLabel+"\" ;\n");
        for(int i = 0; i<expectedSuperproperties.length; i++){
            final char patternConjuctor = i==expectedSuperproperties.length - 1 ? '.' : ';';
            s.append("<").append(RDFS.subPropertyOf.getURI()).append("> <").append(expectedSuperproperties[i])
                    .append("> ").append(patternConjuctor).append("\n");
        }

        final Set<String> expectedNonSuperproperties = new TreeSet<>(List.of(ALL_PROPERTIES));
        expectedNonSuperproperties.removeAll(List.of(expectedSuperproperties));
        for(final String nonSuperproperty : expectedNonSuperproperties)
            s.append("FILTER NOT EXISTS {<").append(featureIRI).append("> <").append(RDFS.subPropertyOf.getURI()).append("> <")
                    .append(nonSuperproperty).append("> }.\n");
        s.append("}");

        try(final QueryExecution queryEx = QueryExecutionFactory.create(s.toString(), model)){
            assertTrue(queryEx.execAsk());
        }
    }

    @Test
    void shouldProvideLabelByCodeForGSPropertiesEN() throws IOException {
        shouldProvideLabelByCodeForGSProperties(Locale.ENGLISH);
    }

    @Test
    void shouldProvideLabelByCodeForGSPropertiesIT() throws IOException {
        shouldProvideLabelByCodeForGSProperties(Locale.ITALIAN);
    }

    void shouldProvideLabelByCodeForGSProperties(final Locale locale) throws IOException {
        try(final GSFeatures languageFeatureLabelProvider = GSFeatures.loadLocal()) {
            for (final GSLanguageFeatureCode code : GSLanguageFeatureCode.values()) {
                final String label = languageFeatureLabelProvider.getLabel(code, locale);
                assertNotNull(label, "label for " + code + " not found");
            }
        }
    }

    @Test
    void shouldProvideLabelForPhenomenaEN() throws IOException {
        final Locale locale = Locale.ENGLISH;
        try(final GSFeatures gs = GSFeatures.loadLocal()) {
            for(final LinguisticPhenomenon p : gs.getRegexLinguisticPhenomena()){
                final GSLanguageFeatureCode code = GSLanguageFeatureCode.valueOf(p.getIRI().substring(NS.length()));
                final String expected = gs.getLabel(code, locale);
                assertEquals(expected, gs.getLabel(p, locale));
            }
        }
        shouldProvideLabelByCodeForGSProperties(Locale.ENGLISH);
    }

    @Test
    void checkDuplicateRegex() throws IOException {
        try(final GSFeatures gs = GSFeatures.loadLocal()) {
            final RegexLinguisticPhenomenaReader reader = RegexLinguisticPhenomenaReader.read(gs.getModel());
            assertTrue(reader.getExceptions().isEmpty());
        }
    }

    /**
     * Get an helper to test the feature with the given code.
     * @param featureCode final feature code
     * @return helper to test the specified feature
     */
    GSFeaturesTestHelper getTestHelper(final GSLanguageFeatureCode featureCode) throws IOException {
        try(final GSFeatures ont = GSFeatures.loadLocal()) {
            final List<RegexLinguisticPhenomenon> allRegexFeatures = ont.getRegexLinguisticPhenomena();
            assertFalse(allRegexFeatures.isEmpty());
            final String featureIRI = GSFeatures.NS + featureCode;
            for (final LinguisticPhenomenon f : allRegexFeatures)
                if (featureIRI.equals(f.getIRI()))
                    return new GSFeaturesTestHelper(f);
            throw new IllegalArgumentException("Unable to get feature " + featureCode);
        }
    }

    /**
     * N03b -Āre>è
     */
    @Test
    void testN03b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N03b).notApply("xxxarjuyyy")
                .derives("123àre456", "123è456")
                .derives("123àre", "123è");
    }

    /**
     * N04b -ARIU > ìa
     */
    @Test
    void testN04b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N04b).notApply("xxxarjuyyy")
                .derives("bördönàrju", "bördönìa");
    }

    /**
     * N04c -ARIU > àiru
     */
    @Test
    void testN04c() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N04c)
                .notApply("xxxàrjuyyy")
                .derives("pàrju", "pàirö");
    }

    /**
     * N01b –aLLU > a+ö
     */
    @Test
    void testN01b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N01b_1)
                .notApply("xxxàlluyyy")
                .notApply("àllu")
                .derives("càllu", "càö");
        getTestHelper(GSLanguageFeatureCode.N01b_2)
                .notApply("éllu")
                .derives("béllu", "bèö");
        getTestHelper(GSLanguageFeatureCode.N01b_3)
                .notApply("xxxólluyyy")
                .notApply("óllu")
                .derives("móllu", "mòö");
    }

    /**
     *  –ALU > àö
     */
    @Test
    void testN56b() throws IOException{
        getTestHelper(GSLanguageFeatureCode.N56b)
                .notApply("123àlu456")
                .notApply("àlu456")
                .notApply("123àle456")
                .notApply("àle456")
                .derives("123àlu", "123àö")
                .derives("123àle", "123àö");

    }

    /**
     * N22c troncamento
     */
    @Test
    void testN22c() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N22c)
                .notApply("xxxmuyyy").notApply("mu").derives("xyzmu", "xyzm")
                .notApply("xxxnuyyy").notApply("nu").derives("xyznu","xyzn")
                .notApply("xxxneyyy").notApply("ne").derives("xyzne", "xyzn")
                .notApply("xxxmeyyy").notApply("me").derives("xyzme", "xyzm");
    }

    /**
     * D04a D04a PTJ > [tts]
     */
    @Test
    void testD04a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D04a).derives("xxxptjyyy",
                "xxxzziyyy", "xxxzzïyyy");
    }


    /**
     * D04b PTJ > [tʃ]
     */
    @Test
    void testD04b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D04b).derives("xxxptjyyy",
                "xxxciyyy", "xxxcïyyy");
    }

    /**
     * S22a TJ > [ʒ]
     */
    @Test
    void testS22a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S22a).derives("xxxtjyyy", "xxxsgyyy");
    }

    // TODO ['S22b KS > [ʒ]', 'sg', 'xx'],
    /**
     * D02  CL > cr
     */
    @Test
    void testD02() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D02).derives("xxxclyyy", "xxxcryyy");
    }


    /**
     * N47c C-+E/I>ts
     */
    @Test
    void testN47c() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N47c_1)
                .derives("123cce456","123zz456")
                .derives("123cci456","123zz456")
                .derives("123ce456","123zz456")
                .derives("123ci456","123zz456");
        getTestHelper(GSLanguageFeatureCode.N47c_2)
                .derives("ccè456", "zzè456")
                .derives("123ccè456", "123zzè456")
                .derives("ccì456", "zzì456")
                .derives("123ccì456", "123zzì456")
                .derives("ccí456", "zzí456")
                .derives("123ccí456", "123zzí456")
                .derives("ccé456", "zzé456")
                .derives("123ccé456", "123zzé456")
                .derives("cè456", "zzè456")
                .derives("123cè456", "123zzè456")
                .derives("cì456", "zzì456")
                .derives("123cì456", "123zzì456")
                .derives("cí456", "zzí456")
                .derives("123cí456", "123zzí456");
    }

    /**
     * N47a C-+E/I>ʒ
     */
    @Test
    void testN47a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N47a_1)
                .derives("123cexyz","123sgxyz")
                .derives("123cixyz","123sgxyz");
        getTestHelper(GSLanguageFeatureCode.N47a_2)
                .derives("123cè456","123sgè456")
                .derives("123cé456","123sgé456")
                .derives("123cì456","123sgì456")
                .derives("123cí456","123sgí456");
    }

    /**
     * X04 mm > mb
     */
    @Test
    void testX04() throws IOException {
        getTestHelper(GSLanguageFeatureCode.X04)
                .derives("123mm456", "123mb456");
    }

    /**
     * X05 nn > nd
     */
    @Test
    void testX05() throws IOException {
        getTestHelper(GSLanguageFeatureCode.X05)
                .derives("123nn456", "123nd456");
    }

    /**
     * N10g À > ei
     */
    @Test
    void testN10g() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N10g).inOpenSyllable('à', "èi");
    }

    /**
     * N10h À > eö/eu
     */
    @Test
    void testN10h() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N10h)
                .inOpenSyllable('à', "èö", "èu");
    }

    /**
     * N11a Ĕ > jɛ aperta sill
     */
    @Test
    void testN11a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N11a)
                .inOpenSyllable('é', "iè");
    }

    /**
     * N12a Ĭ > ëi', 'éi', 'í'
     */
    @Test
    void testN12a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N12a)
                .inOpenSyllable('è', "éi");
    }

    /**
     * N11b Ĕ > jɛ chiusa sill
     */
    @Test
    void testN11b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N11b)
                .derives("123é456", "123iè456");
    }

    // TODO  non lo capisco            ['N16d Ŏ (+ N) tonico > ön', 'ón', 'ó.n', 'ó.'],
    // TODO  non lo capisco           ['N16d Ŏ (+ N) tonico > ön', 'ón', 'ón', 'ó'],

    /**
     * N16a Ŏ tonico > wɔ aperta sillaba
     */
    @Test
    void testN16a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N16a)
                .inOpenSyllable('ó', "uò");
    }

    /**
     * N16b Ŏ tonico > wɔ chiusa sillaba
     */
    @Test
    void testN16b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N16b)
                .derives("123ó456", "123uò456");
    }

    /**
     * N17 Ō/Ŭ tonico > öu
     */
    @Test
    void testN17() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N17)
                .inOpenSyllable('ò', "óu")
                .inOpenSyllable('ú', "óu");
    }

    /**
     * N18 Ō tonico > [o̝]
     */
    @Test
    void testN18a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N18a)
                .derives("123ò456", "123ó456")
                .derives("123ú456", "123ó456");

    }

    /**
     * N20b Ū tonico > ö analog.
     */
    @Test
    void testN20B() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N20b)
                .derives("123ù456", "123ó456");
    }

    /**
     * N45 SPL>sbr
     */
    @Test
    void testN45() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N45)
                .derives("123spl456", "123sbr456");
    }

    /**
     * N24b BL > br
     */
    @Test
    void testN24b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N24b)
                .notApply("123bl456")
                .derives("bl456", "br456");
    }

    /**
     * S18 LL > ḍḍ
     */
    @Test
    void testS18() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S18)
                .derives("123ll456", "123dd456");
    }

    /**
     * X03 L- > ḍḍ
     */
    @Test
    void textX03() throws IOException {
        getTestHelper(GSLanguageFeatureCode.X03)
                .notApply("123l456")
                .derives("l456", "dd456");

    }

    /**
     * N24c BL->dʒ
     */
    @Test
    void testN24c() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N24c)
                .notApply("123bl456")
                .derives("bl456", "gi456", "ge456");
    }

    // TODO non lo capisco ['N26 BR->br', 'br', '#br'],
    /**
     * CL-/PL- > [tʃ]
     */
    @Test
    void testN27() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N27)
                .notApply("123cl456")
                .derives("cl456", "ci456")
                .notApply("123pl456")
                .derives("pl456", "ci456");
    }

    /**
     * N34 GL->dʒ
     */
    @Test
    void testN34() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N34)
                .notApply("123gl456")
                .derives("gl456", "gi456",
                        "ge456",
                        "gì456",
                        "gè456",
                        "gé456");
    }

    // TODO non lo capisco              ['N35 GR->gr-', 'gr', '#gr'],
    /**
     * N42b PL->pr-
     */
    @Test
    void testN42b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N42b)
                .notApply("123pl456")
                .derives("pl456", "pr456");
    }

    /**
     * N28b -CL-/-TL->ghj
     */
    @Test
    void testN28b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N28b)
                .notApply("123cl").notApply("cl456").derives("123cl456", "123ghj456")
                .notApply("123tl").notApply("tl456").derives("123tl456", "123ghj456");
    }

    /**
     * N29a -CT->it
     */
    @Test
    void testN29a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N29a)
                .notApply("123ct").notApply("ct456").derives("123ct456", "123it456");
    }

    /**
     * N29b -CT->tʃ
     */
    @Test
    void testN29b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N29b)
                .derives("123ct456", "123ci456");
    }

    // TODO non lo capisco              ['N31 DR>dr', 'dr', 'dr'],
    /**
     * N30a NDC>ndz
     */
    @Test
    void testN30a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N30a)
                .derives("123ndc456", "123nż456");
    }

    /**
     * N30b DC>dz
     */
    @Test
    void testN30b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N30b)
                .derives("123dc456", "123ż456");
    }

    /**
     * N32 DV>v
     */
    @Test
    void testN32() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N32)
                .derives("123dv456", "123v456");
    }

    /**
     * N33 FL>ʃ
     */
    @Test
    void testN33() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N33)
                .derives("123fl456", "123sce456", "123sci456");
    }

    // TODO non lo capisco              ['N37 MB->mb', 'mb', 'mb'],
    // TODO non lo capisco             ['N38 ND->nd', 'nd', 'nd'],
    // TODO non lo capisco             ['N39 NS->ns', 'ns', 'ns'],

    /**
     * N41 NV->mb
     */
    @Test
    void testN41() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N41)
                .derives("123nv456", "123mb456");
    }
    // TODO non lo capisco ['N41 NV->nv', 'nv', 'nv'],

    /**
     * N43 -PL->dʒ-
     */
    @Test
    void testN43() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N43)
                .notApply("123pl").notApply("pl456")
                .notApply("123ge").notApply("ge456")
                .notApply("123gi").notApply("gi456")
                .notApply("123gì").notApply("gì456")
                .notApply("123gè").notApply("gè456")
                .notApply("123gé").notApply("gé456")
                .derives("123pl456", "123ge456", "123gi456",
                        "123gì456", "123gè456", "123gé456");
    }

    /**
     * N44 SJ>ʒ
     */
    @Test
    void testN44() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N44)
                .derives("123sj456", "123sg456");
    }

    //TODO non lo capisco              ['N46a B->b', 'b', '#b'],
    //TODO non lo capisco              ['N46c -B->-b-', 'b', '-b-'],

    /**
     * N49 G- +A/O/U > [g]
     */
    @Test
    void testN49() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N49)
                .notApply("123g456")
                .derives("g456", "gà456");
    }

    /**
     * N51a G-+E/I > [dz]
     */
    @Test
    void testN51a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N51a_1)
                .derives("123ge456", "123żż456", "123ż456")
                .derives("ge456", "żż456", "ż456")
                .derives("123gi456", "123żż456", "123ż456")
                .derives("gi456", "żż456", "ż456");
        getTestHelper(GSLanguageFeatureCode.N51a_2)
                .derives("gè456", "żżè456", "żè456")
                .derives("123gè456", "123żżè456", "123żè456")
                .derives("gé456", "żżé456", "żé456")
                .derives("123gé456", "123żżé456", "123żé456")
                .derives("gì456", "żżì456", "żì456")
                .derives("123gì456", "123żżì456", "123żì456")
                .derives("gí456", "żżí456", "żí456")
                .derives("123gí456", "123żżí456", "123żí456");
    }
// TODO non lo capisco                  ['N51b G-+E/I > [dʒ]', 'g', '#ge', ],

    /**
     * N51b G-+E/I > [dʒ]
     */
    @Test
    void testN51b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N51b)
                .notApply("123gi456")
                .notApply("123ge456")
                .derives("gi456", "g456")
                .derives("ge456", "g456");
    }

    /**
     * D04c PJ > pj
     */
    @Test
    void testD04c() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D04c)
                .precededByVowelExt('p','j',"pi");
    }

    /**
     * D04d PJ > tʃ
     */
    @Test
    void testD04d() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D04d)
                .precededByVowelExt('p','j',"ci");
    }

    /**
     * N54c5 Leniz TR>[ir]
     */
    @Test
    void testN54c5() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54c5).lenitionFollowedByR( 't', 'i');
    }

    /**
     * N54c6 Leniz TR>[rr]
     */
    @Test
    void testN54c6() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54c6).lenitionFollowedByR('t', 'r');
    }

    /**
     * N54a3 Leniz CR>gr
     */
    @Test
    void testN54a3() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54a3).lenitionFollowedByR('c', 'g');
    }

    /**
     * N54b5 Leniz PR>vr
     */
    @Test
    void testN54b5() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54b5).lenitionFollowedByR('p', 'v');
    }

    /**
     * N54a1 leniz -C- > [g]
     */
    @Test
    void testN54a1() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54a1)
                .lenitionFollowedByR('c', 'g')
                .lenitionFollowedByR('q', 'g');
    }

    /**
     * N54b1 leniz -P- > [b]
     */
    @Test
    void testN54b1() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54b1).lenition('p', 'b');
    }

    /**
     * N54b3 leniz -P- > [v]
     */
    @Test
    void testN54b3() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54b3).lenition('p', 'v');
    }

    /**
     * N54c1 leniz -T- > [d]
     */
    @Test
    void testN54c1() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54c1)
                .lenition('t', 'd');
    }

    /**
     * N54c2 leniz -T- > [r]
     */
    @Test
    void testN54c2() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N54c2).lenition('t', 'r');
    }

    /**
     * N55a L- > [r]
     */
    @Test
    void testN55a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N55a)
                .notApply("123l456")
                .derives("l456", "r456");
    }

    /**
     * S01 Ĕ(N) tonica > [ɛ(n)]
     */
    @Test
    void testS01() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S01)
                .derives("123én456", "123èn456");
    }

    /**
     * S02 Ē Ĭ toniche > i
     */
    @Test
    void testS02() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S02)
                .derives("123è456", "123ì456")
                .derives("123í456", "123ì456");
    }

    /**
     * S03 Ē toniche > [ɛ]
     */
    @Test
    void testS03() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S03)
                .derives("123í456", "123è456");
    }
// TODO non lo capisco                 ['S03 Ē toniche > [ɛ]', 'è', 'è'],
// TODO non lo capisco                   ['S03 Ē toniche > [ɛ]', 'è', 'è.'],

    /**
     * S04 Ŏ tonica in aperta sill> [ɔ]
     */
    @Test
    void testS04() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S04)
                .inOpenSyllable('ó', "ò");
    }

    /**
     * S05 Ō tonici > ù
     */
    @Test
    void testS05() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S05)
                .notApply("ò456")
                .derives("123ò456", "123ù456")
                .derives("123ò", "123ù")
                .notApply("ú456")
                .derives("123ú456", "123ù456")
                .derives("123ú", "123ù");
    }

    // TODO non lo capisco                ['S06 Ō tonico > [ɔ]', 'ò', 'ò.'],
    // TODO non lo capisco                 ['S06 Ō tonico > [ɔ]', 'ò', 'ò'],

    /**
     * S09 -CL-/-TL->chj
     */
    @Test
    void testS09() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S09)
                .notApply("123cl").notApply("cl456").derives("123cl456", "123chj456")
                .notApply("123tl").notApply("tl456").derives("123tl456", "123chj456");
    }

    /**
     * S10 CT>t
     */
    @Test
    void testS10() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S10)
                .derives("123ct456", "123t456");
    }

    /**
     * S13a GL- > gr
     */
    @Test
    void testS13a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S13a)
                .notApply("123gl456")
                .derives("gl456", "gr456");
    }

    /**
     * S13b GL- > [j]
     */
    @Test
    void testS13b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S13b)
                .notApply("123gl456")
                .derives("gl456", "i456");
    }

    /**
     * S14 -GL- > ghj
     */
    @Test
    void testS14() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S14)
                .derives("123gl456", "123ghj456");
    }

    /**
     * S15 GR- > r-
     */
    @Test
    void testS15() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S15)
                .notApply("123gr456")
                .derives("gr456", "r456");
    }

    /**
     * S16 G+E/I > [j]
     */
    @Test
    void testS16() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S16)
                .derives("123ge456", "123i456")
                .derives("123gè456", "123i456")
                .derives("123gé456", "123i456")
                .derives("123gi456", "123i456")
                .derives("123gì456", "123i456")
                .derives("123gí456", "123i456");
    }

    /**
     * S17 LJ > ghj
     */
    @Test
    void testS17() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S17)
                .derives("123lj456", "123ghj456");
    }

    /**
     * S21 PL > chj
     */
    @Test
    void testS21() throws IOException {
        getTestHelper(GSLanguageFeatureCode.S21)
                .derives("123pl456", "123chj456");
    }

    /**
     * X01 Ē/Ĭ tonica> [e̝]
     */
    @Test
    void testX01() throws IOException {
        getTestHelper(GSLanguageFeatureCode.X01)
                .notApply("è456")
                .derives("123è456", "123é456")
                .derives("123è", "123é")
                .notApply("í456")
                .derives("123í456", "123é456")
                .derives("123í", "123é");
    }

    /**
     * D01a -CJ- > [tts]
     */
    @Test
    void testD01a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D01a)
                .notApply("123cj").notApply("cj456")
                .derives("123cj456", "123zz456");
    }

    /**
     * D01b -CJ- > [tʃ]
     */
    @Test
    void testD01b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.D01b)
                .notApply("123cj").notApply("cj456")
                .derives("123cj456", "123ci456");
    }


    /**
     * N64 -e > -ö/a metapl tipo forta",
     */
    @Test
    void testN64() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N64)
                .notApply("123e456")
                .notApply("e456")
                .derives("123e", "123ö", "123a");

    }


    /**
     * N13a * > -e -ə finale
     */
    @Test
    void testN13a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N13a)
                .derives("1234", "123e");
    }

    /**
     * N13b -ə- mediano
     */
    @Test
    void testN13b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N13b)
                .derives("1234", "12e4", "1e34");
    }

    /**
     * N19a -ö- atono interno
     */
    @Test
    void testN19a() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N19a)
                .derives("1234", "12ö4", "1ö34");
    }

    /**
     * N19b -ö finale
     */
    @Test
    void testN19b() throws IOException {
        getTestHelper(GSLanguageFeatureCode.N19b)
                .derives("1234", "123ö");
    }

    @Test
    void shouldSelectOnlyNorthernFeatures() throws IOException {
        final String northernFeaturesQuery = "SELECT ?f WHERE{?f <"+RDFS.subPropertyOf.getURI()+"> <"
                + NORTHERN_FEATURE_OBJ_PROPERTY+"> ;\n"
                + "\t<"+LinguisticPhenomena.REGEX_ANN_PROPERTY+"> ?regex}";
        try(final GSFeatures g=GSFeatures.loadLocal()){
            try(final QueryExecution e = QueryExecutionFactory.create(northernFeaturesQuery, g.getModel())){
                final SortedSet<String> expected=new TreeSet<>();
                e.execSelect().forEachRemaining(querySolution -> expected.add(querySolution.getResource("f").getURI()));

                final SortedSet<String> actual=new TreeSet<>();
                g.getRegexNorthernItalyFeatures().forEach(phenomenon -> actual.add(phenomenon.getIRI()));

                assertEquals(expected, actual);
                System.out.println("Found the followings Northern Italy features "+actual);
            }
        }
        //GSFeatures g=new GSFeatures("gstext.ttl");
    }
}
