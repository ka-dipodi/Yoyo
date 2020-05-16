package com.fgy.dao;

import com.fgy.po.Blog;
import com.fgy.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long id, Sort sort);

    @Query(value="Select c from Comment c where c.parentComment.id=?1")
    Comment findParentCommentId(Long id);
}
