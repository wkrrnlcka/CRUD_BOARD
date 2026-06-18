package org.mysite.crud_board;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    private final PostService postService;
    private final userService userService;

    public HomeController(PostService postService,
                          userService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/list")
    public String list(HttpSession session, Model model) {

        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser != null) {
            model.addAttribute("user", loginUser);
        }

        model.addAttribute("posts", postService.findAll());
        return "list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);

        return "detail";
    }

    @GetMapping("/write")
    public String write(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        return "write";
    }

    @PostMapping("/write")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }


        Post post = new Post(title, content, loginUser);
        postService.save(post);

        return "redirect:/list";
    }

    //삭제
    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Post post = postService.findById(id);

        if(!post.getWriter().getId().equals(loginUser.getId())) {
            return "redirect:/list";
        }

        postService.deleteById(id);

        return "redirect:/list";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String content
    ) {

        Post post = postService.findById(id);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
            postService.save(post);
        }

        return "redirect:/detail?id=" + id;
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password
                         ) {

        User user = new User(username, password);

        userService.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                         @RequestParam String password,
                         HttpSession session) {

        User user = userService.login(username, password);

        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/list";
        }

        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/list";
    }


}