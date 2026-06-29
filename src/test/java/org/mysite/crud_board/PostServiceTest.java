package org.mysite.crud_board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시글 저장 기능이 정상 작동해야한다")
    void saveTest() {
        Post post = new Post("테스트 제목", "테스트 내용");

        postService.save(post);

        Post savedPost = postService.findById(post.getId());

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    @DisplayName("동시에 두 사용자가 수정요청을 보내면= 데이터가 덮어씌워진다")
    void updateConcurrencyTest() throws InterruptedException {
        Post post = new Post("원래 제목", "원래 내용");
        postService.save(post);
        Long postId = post.getId();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        Future<?> futureA = executorService.submit(()-> {
            try {
                Post postA = postService.findById(postId);
                postA.setTitle("A가 수정함");
                postService.save(postA);
            } finally {
                latch.countDown();
            }
        });

        Future<?> futureB = executorService.submit(()-> {
            try {
                Post postB = postService.findById(postId);
                postB.setTitle("B가 수정함");
                postService.save(postB);
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        int failureCount = 0;

        try {
            futureA.get();
        } catch (ExecutionException e) {
            failureCount++;
            assertThat(e.getCause()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
        }

        try {
            futureB.get();
        } catch (ExecutionException e) {
            failureCount++;
            assertThat(e.getCause()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
        }

        assertThat(failureCount).isEqualTo(1);

        Post resultPost = postService.findById(postId);
        System.out.println("===================================");
        System.out.println("최종 DB에 남은 게시글 제목 : " +resultPost.getTitle());
        System.out.println("===================================");
    }
}