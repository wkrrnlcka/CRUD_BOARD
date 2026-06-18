package org.mysite.crud_board;


import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    //jpa가 db에서 데이터를 꺼낼 때 빈 객체를 먼저 만들고 나서 값을 채우는 방식으로 동작해서 아무 인자 없는 no-arg생성자가 있어야됨
    protected Post() {}

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post(String title, String content, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
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

    public User getWriter(){
        return writer;
    }
}
