package it.unict.gallosiciliani.webapp.lexica;

import it.unict.gallosiciliani.model.lemon.lime.Lexicon;
import it.unict.gallosiciliani.webapp.WebAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * Provide and manage all the Lexica in the knowledge base
 */
@Controller
@RequestMapping("/lexica")
@Slf4j
public class LexicaHTMLController {

    @Autowired
    WebAppProperties props;

    @Autowired
    LexicaService lexicaService;

    @Autowired
    ObjectProvider<EntrySummarizer> entrySummarizerObjectProvider;

    /**
     * Show the list of all the lexica in the knowledge base
     */
    @GetMapping(value={"/"})
    public String viewAll(Model model){
        final List<Lexicon> lexica = lexicaService.findAllLexica();
        for(final Lexicon l : lexica){
            final String internalIRI = UriComponentsBuilder.fromPath("lexicon").queryParam("id", URI.create(l.getId())).toUriString();
            l.setId(internalIRI);
        }
        model.addAttribute("lexica", lexica);
        return "lexica/viewAll";
    }

    @GetMapping(value={"/lexicon"})
    public String viewLexicon(final @RequestParam URI id,
                              final @RequestParam(defaultValue = "0") int page,
                              final Model model,
                              final Locale locale){
        return viewLexiconInternal(id.toString(), EntrySelector.ALL, page, model, locale);
    }

    public String viewLexiconInternal(final String lexicon,
                              final @ModelAttribute EntrySelector selector,
                              final int page,
                              final Model model,
                              final Locale locale) {
        final Lexicon l = lexicaService.findLexiconByIRI(lexicon);
        model.addAttribute("lexicontitle", l.getTitle());
        model.addAttribute("lexicon", lexicon);
        model.addAttribute("selector", selector);
//        model.addAttribute("gskb", new GSKB(webAppProperties));

        final EntrySummarizer summarizer = entrySummarizerObjectProvider.getObject(locale);
        final String regexLemmaFilter=getRegexLemmaFilter(page);
        final List<EntrySummary> entries = summarizer.summarize(lexicaService.findEntries(l, selector, regexLemmaFilter));
        model.addAttribute("entries", entries);
        model.addAttribute("size", entries.size());

        return "lexica/viewLexicon";
    }

    /**
     * Get the regular expression to filter lemmas corresponding to the specified page
     * @param page page number
     * @return regular expression
     */
    private String getRegexLemmaFilter(final int page) {
        if (page<0 || page>=props.getPaging().getPages().length){
            log.warn("Invalid page requester {}, using 0", page);
            return props.getPaging().getPages()[0].getSelector();
        }
        return props.getPaging().getPages()[page].getSelector();
    }

    @PostMapping(value={"/lexicon"})
    public String viewLexicon(final @RequestParam URI id,
                              final @ModelAttribute EntrySelector selector,
                              final @RequestParam int page,
                              final Model model,
                              final Locale locale){
        return viewLexiconInternal(id.toString(), selector, page, model, locale);
    }
}
