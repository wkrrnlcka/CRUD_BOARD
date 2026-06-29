package org.mysite.crud_board;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Post getPostDetail(Long id) {
        postRepository.updateViews(id);
        return postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Post Not Found"));
    }
}
