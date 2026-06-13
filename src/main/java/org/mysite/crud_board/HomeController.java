package org.mysite.crud_board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

class Post {
    private Long id;
    private String title;
    private String content;

    public Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

@Controller
public class HomeController {

    List<Post> posts = new ArrayList<>();

    public HomeController() {
        posts.add(new Post(1L, "스프링 공부하기", "스프링 MVC 공부중"));
        posts.add(new Post(2L, "CRUD 구현하기", "게시판 CRUD 구현"));
        posts.add(new Post(3L, "JPA 공부하기", "JPA 기초 학습"));
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("posts", posts);
        return "list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long id, Model model) {

        Post foundPost = null;

        for (Post post : posts) {
            if (post.getId().equals(id)) {
                foundPost = post;
                break;
            }
        }

        model.addAttribute("post", foundPost);

        return "detail";
    }


}
