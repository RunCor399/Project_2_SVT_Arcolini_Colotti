package it.polito.svt.vulnapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping("/")
    public ModelAndView getIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Spring Boot Movie");
        return mv;
    }
}
