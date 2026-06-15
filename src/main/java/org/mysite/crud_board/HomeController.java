package org.mysite.crud_board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Entity
class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String content;

    //jpa가 db에서 데이터를 꺼낼 때 빈 객체를 먼저 만들고 나서 값을 채우는 방식으로 동작해서 아무 인자 없는 no-arg생성자가 있어야됨
    protected Post() {}

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
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

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping("/write")
    public String create(@RequestParam String title, @RequestParam String content) {
        Long id = (long) (posts.size() + 1);

        Post post = new Post(id, title, content);

        posts.add(post);

        return "redirect:/list";
    }

    //삭제
    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {

        posts.removeIf(post -> post.getId().equals(id));

        return "redirect:/list";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam Long id, Model model) {
        Post post = findPostById(id);

        model.addAttribute("post", post);

        return "edit";
    }

    @PostMapping("/edit")
    public String edit(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String content
    ) {

        Post post = findPostById(id);

        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
        }

        return "redirect:/detail?id=" + id;
    }

    private Post findPostById(Long id) {
        for (Post post : posts) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }
}
