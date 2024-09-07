package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenon;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * A java based representation of some regex language features.
 * Use the description provided in the ontology. The features here has been used
 * to generate initially the {@link GSFeatures} ontology.
 * @author Cristiano Longo
 */
public class DefaultRegexLanguageFeatures {
    public static final String OCCLUSIVE = "bptq";
    public static final String VOCALI = "aàeèéiìíoòóuùúyöëïü";
    public static final String VOCALI_VEL = "aàoòóuùú";

    private final Model model;
    private final Property regexProperty;
    private final Property replacementProperty;

    /**
     * 
     * @param targetModel ontology where the regex feature description will be added
     */
    DefaultRegexLanguageFeatures(final Model targetModel){
        this.model = targetModel;
        this.regexProperty = model.getProperty(LinguisticPhenomena.REGEX_ANN_PROPERTY);
        this.replacementProperty = model.getProperty(LinguisticPhenomena.REPLACEMENT_ANN_PROPERTY);
    }
    
    /**
     * Add a set of {@link RegexLinguisticPhenomenon} to a target model
     * by adding regex and replacements annotations
     * @param targetModel where annotations will be added
     */
    public static void add(final Model targetModel){
        final DefaultRegexLanguageFeatures f = new DefaultRegexLanguageFeatures(targetModel);
        f.createRegexFeature(GSLanguageFeatureCode.N03b, "(\\S)àre", "$1è");
        f.createRegexFeature(GSLanguageFeatureCode.N04b, "(\\S)àrju$", "$1ìa");
        f.createRegexFeature(GSLanguageFeatureCode.N04c, "(\\S)àrju$", "$1àirö");

        f.createRegexFeature(GSLanguageFeatureCode.N01b, "(\\S)àllu$", "$1àö");
        f.createRegexFeature(GSLanguageFeatureCode.N01b, "(\\S)éllu$", "$1èö");
        f.createRegexFeature(GSLanguageFeatureCode.N01b, "(\\S)óllu$", "$1òö");

        f.createRegexFeature(GSLanguageFeatureCode.N22c, "(\\S)([mn])[ue]$", "$1$2");
        f.createRegexFeature(GSLanguageFeatureCode.D04a, "ptj", "zzi", "zzï");
        f.createRegexFeature(GSLanguageFeatureCode.D04b, "ptj", "ci", "cï");
        f.createRegexFeature(GSLanguageFeatureCode.S22a, "tj", "sg");
        f.createRegexFeature(GSLanguageFeatureCode.D02, "cl", "cr");
        f.createRegexFeature(GSLanguageFeatureCode.N47c, "(^c|([^c])c|cc)[ei]", "$2zz");
        f.createRegexFeature(GSLanguageFeatureCode.N47c, "(^c|([^c])c|cc)([èìíé])", "$2zz$3");
        f.createRegexFeature(GSLanguageFeatureCode.N47a, "c[ei]", "sg");
        f.createRegexFeature(GSLanguageFeatureCode.N47a, "c([èéìí])", "sg$1");
        f.createRegexFeature(GSLanguageFeatureCode.X04, "mm", "mb");
        f.createRegexFeature(GSLanguageFeatureCode.X05, "nn", "nd");
        f.openSyllable(GSLanguageFeatureCode.N10g,'à', "èi");
        f.openSyllable(GSLanguageFeatureCode.N10h, 'à', "èö", "èu");
        f.openSyllable(GSLanguageFeatureCode.N11a,'é', "iè");
        f.openSyllable(GSLanguageFeatureCode.N12a, 'è', "éi");
        f.createRegexFeature(GSLanguageFeatureCode.N11b, "é", "iè");
        f.openSyllable(GSLanguageFeatureCode.N16a,'ó', "uò");
        f.createRegexFeature(GSLanguageFeatureCode.N16b, "ó", "uò");
        f.openSyllable(GSLanguageFeatureCode.N17,"[òú]", "óu");
        f.createRegexFeature(GSLanguageFeatureCode.N18a, "[òú]", "ó");
        f.createRegexFeature(GSLanguageFeatureCode.N20b, "ù", "ó");
        f.createRegexFeature(GSLanguageFeatureCode.N45, "spl", "sbr");
        f.createRegexFeature(GSLanguageFeatureCode.N24b, "bl", "br");
        f.createRegexFeature(GSLanguageFeatureCode.S18, "ll", "dd");
        f.createRegexFeature(GSLanguageFeatureCode.X03, "^l", "dd");
        f.createRegexFeature(GSLanguageFeatureCode.N24c, "^bl", "gi", "ge");
        f.createRegexFeature(GSLanguageFeatureCode.N27, "^[cp]l", "ci");
        f.createRegexFeature(GSLanguageFeatureCode.N34, "^gl", "gi", "ge",
                "gì","gè", "gé");
        f.createRegexFeature(GSLanguageFeatureCode.N42b, "^pl", "pr");
        f.inside(GSLanguageFeatureCode.N28b, "[ct]l", "ghj");
        f.inside(GSLanguageFeatureCode.N29a, "ct", "it");
        f.createRegexFeature(GSLanguageFeatureCode.N29b, "ct", "ci");
        f.createRegexFeature(GSLanguageFeatureCode.N30a, "ndc", "nż" );
        f.createRegexFeature(GSLanguageFeatureCode.N30b, "dc", "ż");
        f.createRegexFeature(GSLanguageFeatureCode.N32, "dv", "v");
        f.createRegexFeature(GSLanguageFeatureCode.N33, "fl", "sci", "sce");
        f.createRegexFeature(GSLanguageFeatureCode.N41, "nv", "mb");
        f.inside(GSLanguageFeatureCode.N43, "pl", "gi", "ge",
                "gì", "gè", "gé");
        f.createRegexFeature(GSLanguageFeatureCode.N44, "sj", "sg");
        f.createRegexFeature(GSLanguageFeatureCode.N49, "^g", "gà");
        f.createRegexFeature(GSLanguageFeatureCode.N51a, "g[ei]", "żż","ż");
        f.createRegexFeature(GSLanguageFeatureCode.N51b, "^g[ei]", "g");
        f.inside(GSLanguageFeatureCode.D04c, "(["+VOCALI+"j])pj(\\S)", "pi");
        f.inside(GSLanguageFeatureCode.D04d, "(["+VOCALI+"j])pj(\\S)", "ci");
        f.lenitionFollowedByR(GSLanguageFeatureCode.N54c5, 't', 'i');
        f.lenitionFollowedByR(GSLanguageFeatureCode.N54c6, 't', 'r');
        f.lenitionFollowedByR(GSLanguageFeatureCode.N54a3, 'c', 'g');
        f.lenitionFollowedByR(GSLanguageFeatureCode.N54b5, 'p', 'v');
        f.lenition(GSLanguageFeatureCode.N54a1,"[cq]", 'g');
        f.lenition(GSLanguageFeatureCode.N54b1,"p", 'b');
        f.lenition(GSLanguageFeatureCode.N54b3,"p", 'v');
        f.lenition(GSLanguageFeatureCode.N54c1,"t", 'd');
        f.lenition(GSLanguageFeatureCode.N54c2,"t", 'r');
        f.createRegexFeature(GSLanguageFeatureCode.N55a, "^l", "r");
        f.createRegexFeature(GSLanguageFeatureCode.S01, "én", "èn");
        f.createRegexFeature(GSLanguageFeatureCode.S02, "[èí]", "ì");
        f.createRegexFeature(GSLanguageFeatureCode.S03, "í", "è");
        f.openSyllable(GSLanguageFeatureCode.S04,'ó', "ò");
        f.createRegexFeature(GSLanguageFeatureCode.S05, "(\\S)[òú]", "$1ù");
        f.inside(GSLanguageFeatureCode.S09, "[ct]l", "chj");
        f.createRegexFeature(GSLanguageFeatureCode.S10, "ct", "t");
        f.createRegexFeature(GSLanguageFeatureCode.S13a, "^gl", "gr");
        f.createRegexFeature(GSLanguageFeatureCode.S13b, "^gl", "i");
        f.createRegexFeature(GSLanguageFeatureCode.S14, "gl", "ghj");
        f.createRegexFeature(GSLanguageFeatureCode.S15, "^gr", "r");
        f.createRegexFeature(GSLanguageFeatureCode.S16, "g[eèéiìí]", "i");
        f.createRegexFeature(GSLanguageFeatureCode.S17, "lj", "ghj");
        f.createRegexFeature(GSLanguageFeatureCode.S21, "pl", "chj");
        f.createRegexFeature(GSLanguageFeatureCode.X01, "(\\S)[èí]", "$1é");
        f.inside(GSLanguageFeatureCode.D01a, "cj", "zz");
        f.inside(GSLanguageFeatureCode.D01b, "cj", "ci");
    }

    private void createRegexFeature(final GSLanguageFeatureCode featureCode, final String regex, final String...replacements){
        final Resource feature = model.getResource(GSFeatures.NS+featureCode);
        model.add(feature, regexProperty, regex);
        for(final String replacement : replacements)
            model.add(feature, replacementProperty, replacement);
    }

    /**
     * The pattern is matched only inside the string, not in the boundaries
     *
     * @param featureCode code of the feature related to this tranformation
     * @param regex regular expression for the pattern
     * @param replacements how matched parts will be replaced
     */
    private void inside(final GSLanguageFeatureCode featureCode, final String regex, final String...replacements){
        if (regex.contains("$1")) throw new UnsupportedOperationException();
        final String[] newReplacements = new String[replacements.length];
        for(int i = 0; i < replacements.length; i++)
            newReplacements[i]="$1"+replacements[i]+"$2";
        createRegexFeature(featureCode, "(\\S)"+regex+"(\\S)", newReplacements);
    }

    /**
     * A tranformation representing lenition, where the pattern is followed by 'r'
     *
     * @param featureCode code characterizing the feature transformation
     * @param srcChar character in the source string (must be different from X)
     * @param destChar corresponding character in the replacement string
     */
    private void lenitionFollowedByR(final GSLanguageFeatureCode featureCode,
                                                                       final char srcChar,
                                                                       final char destChar){
        if (srcChar!='c' && !OCCLUSIVE.contains(srcChar+""))
            throw new IllegalArgumentException();
        createRegexFeature(featureCode, "(["+VOCALI+"])"+srcChar+"r", "$1"+destChar+"r");
    }


    /**
     * A tranformation representing lenition
     *
     * @param featureCode code characterizing the feature transformation
     * @param regex regular expression pattern
     * @param destChar corresponding character in the replacement string
     */
    private void lenition(final GSLanguageFeatureCode featureCode,
                                                            final String regex,
                                                            final char destChar){
        final String followings = "c".equals(regex) ? VOCALI_VEL+'r' : VOCALI+'r';
        createRegexFeature(featureCode, "(["+VOCALI+"])"+regex+"(["+followings+"])", "$1"+destChar+"$2");
    }


    /**
     * Create a trasformation, relative to the specified feature, to replace the given vowel when it occurs in a open syllable.
     * @param featureCode feature
     * @param vowel vowel to be replaced
     * @param replacements how to replace the vowel
     */
    private void openSyllable(final GSLanguageFeatureCode featureCode, char vowel, final String...replacements){
        openSyllable(featureCode, ""+vowel, replacements);
    }

    /**
     * Create a trasformation, relative to the specified feature, to replace the given vowel when it occurs in a open syllable.
     * @param featureCode feature
     * @param regex vowels to be replaced
     * @param replacements how to replace the vowel
     */
    private void openSyllable(final GSLanguageFeatureCode featureCode, final String regex, final String...replacements){
        final String[] replacementsWithGroup = new String[replacements.length];
        for(int i=0; i<replacements.length;i++)
            replacementsWithGroup[i]=replacements[i]+"$1";
        createRegexFeature(featureCode, regex+"$|"+regex+"([^"+VOCALI+"j]["+VOCALI+"j])", replacementsWithGroup);
    }
}
