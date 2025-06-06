package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.webapp.derivation.DerivationService;
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
    MessageSource messageSource;

    @Autowired
    DerivationService derivationService;

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
        final String writtenRep = src.getCanonicalForm().getWrittenRep().get();
        final String partOfSpeechMessageId = LexInfo.getMessageId(src.getPartOfSpeech());
        final String partOfSpeech = messageSource.getMessage(partOfSpeechMessageId, new Object[0], locale);
        final SortedSet<Form> etyComponents = getEtymonComponents(src);
        final SortedSet<String> featureLabels = new TreeSet<>();
        final List<LinguisticPhenomenonOccurrence> derivation=getDerivation(src);
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

            @Override
            public List<LinguisticPhenomenonOccurrence> getDerivation() {
                return derivation;
            }
        };
    }

    private Etymology getEtymology(final LexicalEntry src){
        if (src.getEtymology()==null || src.getEtymology().isEmpty())
            return null;
        return src.getEtymology().iterator().next();
    }

    private List<LinguisticPhenomenonOccurrence> getDerivation(final LexicalEntry src) {
        final Etymology etymology=getEtymology(src);
        if (etymology==null) return Collections.emptyList();
        final Set<Form> etySubSource = etymology.getStartingLink().getEtySubSource();
        if (etySubSource==null || etySubSource.isEmpty()) return Collections.emptyList();
        final Form lemma=src.getCanonicalForm();
        final Form etymon=etySubSource.iterator().next();
        return derivationService.getDerivationChain(lemma, etymon);
    }

    /**
     * The string to be shown as etymon, split parts if necessary
     *
     * @return a string
     */
    private SortedSet<Form> getEtymonComponents(final LexicalEntry src){
        final Etymology etymology=getEtymology(src);
        if (etymology==null || etymology.getLabel()==null)
            return Collections.emptySortedSet();
        /*
         * Here we assume that all the subcomponents of the etymon are reported in
         * etymology.getName, separated by '+'. So, the ordering of subcomponents is derived by
         * the position of the subcomponent in etymology.getName
         */
        final Comparator<Form> subtermsComparator = (f1, f2) -> {
            final int p1 = etymology.getLabel().indexOf(f1.getLabel());
            final int p2 = etymology.getLabel().indexOf(f2.getLabel());
            return p1 - p2;
        };
        final SortedSet<Form> components = new TreeSet<>(subtermsComparator);
        final Set<Form> etySubSource = etymology.getStartingLink().getEtySubSource();
        for (Form form : etySubSource) {
            if (form.getLabel() != null) {
                components.add(form);
            }
        }
        final  StringBuffer componentStrings = new StringBuffer();
        for(final Form f : components)
            componentStrings.append("(").append(f.getWrittenRep()).append(":").append(f.getLabel()).append(")");
        log.info("Etymon {} - components {}", etymology.getLabel(), componentStrings);
        return components;
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
