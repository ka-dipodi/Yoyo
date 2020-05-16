package com.fgy.service;

import com.fgy.po.Blog;
import com.fgy.po.Comment;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    Blog findBlog(Long id);

    void deleteComment(Long id);

}
