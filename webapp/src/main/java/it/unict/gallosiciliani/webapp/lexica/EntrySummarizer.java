package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.model.lemonety.Etymology;
import it.unict.gallosiciliani.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Create summaries of entries for the View
 *
 * @author Cristiano Longo
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class EntrySummarizer {

    @Autowired
    private MessageSource messageSource;

    private final Locale locale;

    /**
     * Create a summarizer for entries in the specified parent lexicon,
     * using the specified locale.
     *
     * @param locale locale used to generate labels
     *               EntrySummary
     */
    EntrySummarizer(final Locale locale){
        this.locale=locale;
    }

    public EntrySummary summarize(final LexicalEntry src){
        final String writtenRep = src.getCanonicalForm().getWrittenRep();
        final String partOfSpeechMessageId = LexInfo.getMessageId(src.getPartOfSpeech());
        final String partOfSpeech = messageSource.getMessage(partOfSpeechMessageId, new Object[0], locale);
        final SortedSet<Form> etyComponents = getEtymonComponents(src);
        final SortedSet<String> featureLabels = new TreeSet<>();
        return new EntrySummary() {

            @Override
            public String getWrittenRep() {
                return writtenRep;
            }

            @Override
            public String getPartOfSpeech() {
                return partOfSpeech;
            }

            @Override
            public SortedSet<Form> getLatinEtymon() {
                return etyComponents;
            }

            @Override
            public SortedSet<String> getPhoneticFeatureLabels() {
                return featureLabels;
            }
        };
    }

    /**
     * The string to be shown as etymon, split parts if necessary
     *
     * @return a string
     */
    private SortedSet<Form> getEtymonComponents(final LexicalEntry src){
        if (src.getEtymology()==null || src.getEtymology().isEmpty())
            return Collections.emptySortedSet();

        final Etymology etymology=src.getEtymology().iterator().next();
        if (etymology.getName()==null)
            return Collections.emptySortedSet();
        /*
         * Here we assume that all the subcomponents of the etymon are reported in
         * etymology.getName, separated by '+'. So, the ordering of subcomponents is derived by
         * the position of the subcomponent in etymology.getName
         */
        final Comparator<Form> subtermsComparator = (f1, f2) -> {
            final int p1 = etymology.getName().indexOf(f1.getName());
            final int p2 = etymology.getName().indexOf(f2.getName());
            return p1 - p2;
        };
        final SortedSet<Form> components = new TreeSet<>(subtermsComparator);
        final Set<Form> etySubSource = etymology.getStartingLink().getEtySubSource();
        components.addAll(etySubSource);
        final  StringBuffer componentStrings = new StringBuffer();
        for(final Form f : components)
            componentStrings.append("(").append(f.getWrittenRep()).append(":").append(f.getName()).append(")");
        log.info("Etymon {} - components {}", etymology.getName(), componentStrings);
        return components;
    }

    /**
     * Get the language features associated to the given etymology
     * @param etymology an etymology
     * @param locale preferred locale for the feature labels
     * @return labels for the language features associated to the etymology
     */
    private SortedSet<String> getFeatures(final Etymology etymology, final Locale locale){
        return Collections.emptySortedSet();
        //        final GSKB gskb = new GSKB(webAppProperties);
//        final SortedSet<String> featureLabels = new TreeSet<>();
//        final Consumer<GSLanguageFeatureCode> addToLabels = featureCode -> featureLabels.add(languageFeatureLabelProvider.getLabel(featureCode, locale));
//        etymology.getTypes().forEach(iri -> gskb.getFeatureByHasFeatureClass(iri).ifPresent(addToLabels));
//        return featureLabels;
    }
    /**
     * Convert a list of {@link LexicalEntry} to the corresponding {@link EntrySummary} instances.
     *
     * @param src a list of {@link LexicalEntry}
     * @return summaries of the entries
     */
    public List<EntrySummary> summarize(final List<LexicalEntry> src){
        final List<EntrySummary> res = new LinkedList<>();
        for(final LexicalEntry e : src)
            res.add(summarize(e));
        return res;
    }
}