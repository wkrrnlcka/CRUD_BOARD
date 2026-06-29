package org.mysite.crud_board;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository  extends JpaRepository<Post,Long> {
    @Modifying(clearAutomatically = true) // 💡 값을 변경하는 쿼리임을 나타냅니다. (Insert, Update, Delete)
    @Query("update Post p set p.views = p.views + 1 where p.id = :id")
    void updateViews(@Param("id") Long id);
}
