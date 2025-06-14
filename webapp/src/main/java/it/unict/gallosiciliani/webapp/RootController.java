package it.unict.gallosiciliani.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootController {

    @GetMapping(value={"", "/","/ns", "/ns/"})
    RedirectView redirectHome(){
        return new RedirectView("/ns/projects/gallosiciliani2023Project");
    }
}
