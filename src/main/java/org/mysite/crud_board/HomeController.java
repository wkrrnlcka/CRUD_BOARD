package org.mysite.crud_board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("title", "스프링 공부하기");
        return "list";
    }
}
