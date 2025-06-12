package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.liph.model.lemon.lime.Lexicon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * Provide and manage all the Lexica in the knowledge base
 *
 * @author Cristiano Longo
 */
@Controller
@RequestMapping("/ns/lexica")
@Slf4j
public class LexicaHTMLController {

    @Autowired
    LexicaService lexicaService;

    @Autowired
    ObjectProvider<EntrySummarizer> entrySummarizerObjectProvider;

    /**
     * Show the list of all the lexica in the knowledge base
     */
    @GetMapping(value={"","/"})
    String viewAll(Model model){
        final List<Lexicon> lexica = lexicaService.findAllLexica();
        model.addAttribute("lexica", lexica);
        return "lexica/viewAll";
    }

    @GetMapping(value={"/lexicon"})
    @Deprecated
    String viewLexicon(final @RequestParam URI id,
                              final Model model,
                              final Locale locale){
        return viewLexiconInternal(id.toString(), EntrySelector.ALL, model, locale);
    }

    /**
     * Assuming that the specified lexicon is in the knowledge base
     */
    @GetMapping(value={"/nicosiaesperlinga"})
    String viewNicosiaESperlinga(final Model model,
                       final Locale locale){
        return viewLexiconInternal("https://gallosiciliani.unict.it/ns/lexica/nicosiaesperlinga#lexicon", EntrySelector.ALL, model, locale);
    }

    private String viewLexiconInternal(final String lexicon,
                              final @ModelAttribute EntrySelector selector,
                                      final Model model,
                              final Locale locale) {
        final Lexicon l = lexicaService.findLexiconByIRI(lexicon);
        model.addAttribute("lexicontitle", l.getTitle());
        model.addAttribute("lexicon", lexicon);
        model.addAttribute("selector", selector);
        model.addAttribute("pages", lexicaService.getPageLabels());

        final EntrySummarizer summarizer = entrySummarizerObjectProvider.getObject(locale);
        final List<EntrySummary> entries = summarizer.summarize(lexicaService.findEntries(l, selector));
        model.addAttribute("entries", entries);
        model.addAttribute("size", entries.size());

        return "lexica/viewLexicon";
    }

    @PostMapping(value={"/lexicon"})
    String viewLexicon(final @RequestParam URI id,
                              final @ModelAttribute EntrySelector selector,
                              final Model model,
                              final Locale locale){
        log.info("Selected page {}", selector.getPage());
        return viewLexiconInternal(id.toString(), selector, model, locale);
    }
}
