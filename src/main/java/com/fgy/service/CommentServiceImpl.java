package com.fgy.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fgy.dao.BlogRepository;
import com.fgy.dao.CommentRepository;
import com.fgy.po.Blog;
import com.fgy.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort=Sort.by("createTime");
        //获得当前博客所有父级评论(comments)
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        //返回
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Blog findBlog(Long id) {
        Blog b=commentRepository.findById(id).get().getBlog();
        return b;
    }

    private List<Comment> commentlist= new ArrayList<>();
    @Transactional
    @Override
    public void deleteComment(Long id) {
        Integer i=0;
        Comment c1=commentRepository.findById(id).get();
        commentlist.add(c1);
        while(searchComment(id)!=null);
        for (i = commentlist.size(); i > 0; i--) {
            long y = commentlist.get(i-1).getId();
            commentRepository.deleteById(y);

        }
        commentlist=new ArrayList<>();
    }

    /*递归查询子评论*/
    private String searchComment(Long id){
        Comment comment=commentRepository.findParentCommentId(id);
        if(comment!=null){
            commentlist.add(comment);
            return searchComment(comment.getId());
        }else{
            return null;
        }
    }

    /*评论集合.getReplyComments()---->获得对象的子代评论！！！！！*/
    /*循环父级评论*/
    private List<Comment> eachComment(List<Comment> comments){
        //复制以免影响原有评论(commentsView)
        List<Comment> commentsView=new ArrayList<>();
        for(Comment comment1 : comments){
            Comment c=new Comment();
            BeanUtils.copyProperties(comment1,c);
            commentsView.add(c);
        }
        //合并子级评论各个评论到父级子代的集合里
        combineChildren(commentsView);
        return commentsView;
    }
    /*子级评论*/
    private void combineChildren(List<Comment> comments){
        for(Comment comment : comments){
            //用于存放父级的下一级子代(replys1)
            List<Comment> replys1=comment.getReplyComments();
            for(Comment reply1 : replys1){
                //循环迭代，找出子代，加入子代集合(tempReply)
                recursively(reply1);
            }
        //修改循环的父级对象为(已找到的子代)
        comment.setReplyComments(tempReply);
        //初始化集合
        tempReply=new ArrayList<>();
        i=1;
        }
    }

    /*用于存放子代的集合*/
    private List<Comment> tempReply=new ArrayList<>();
    private Integer i=1;
    /*递归迭代 找出子代的子代的子代······*/
    private void recursively(Comment comment){
        //将当前评论加入子代合集

        if(i<2 ){
            tempReply.add(comment);
            i++;
        }
        if (comment.getReplyComments().size()>0){
            List<Comment> replys=comment.getReplyComments();
            for(Comment reply : replys){
                tempReply.add(reply);
                if (reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }

    }
}
