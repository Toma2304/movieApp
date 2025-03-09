package org.example.movieapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {
    @RequestMapping("/app/**")
    public String forward() {
        return "forward:/index.html";
    }
}
