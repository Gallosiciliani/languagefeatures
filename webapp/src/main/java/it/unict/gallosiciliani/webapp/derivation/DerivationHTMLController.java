package it.unict.gallosiciliani.webapp.derivation;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPrinter;
import it.unict.gallosiciliani.gs.GSFeatures;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * Utility for derivations based on {@link it.unict.gallosiciliani.gs.GSFeatures}
 * @author Cristiano Longo
 */
@Controller
@RequestMapping("/derivation")
@Slf4j
public class DerivationHTMLController {

    @Autowired
    DerivationService derivationService;

    /**
     * Show the derivation form
     */
    @GetMapping(value = {"/", ""})
    String showForm(Model model) {
        model.addAttribute("derivationForm", new DerivatioForm());
        return "derivation/derivationForm.html";
    }

    @PostMapping(value={"/",""})
    String derive(final @ModelAttribute DerivatioForm derivationForm,
                         final Model model,
                         final Locale locale){
        model.addAttribute("derivationForm", derivationForm);
        final Collection<DerivationPathNode> derivations= derivationService
                .derives(derivationForm.getEtymon(), derivationForm.getLemma())
                .getDerivation();
        final DerivationPrinter printer=new DerivationPrinter(GSFeatures.LABEL_PROVIDER_ID);
        final List<String> derivationsAsStr=derivations.stream().map((n)->printer.print(n, locale)).toList();
        derivationForm.setDerivations(derivationsAsStr);
        return "derivation/derivationForm.html";
    }
}
