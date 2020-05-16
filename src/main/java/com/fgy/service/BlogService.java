package com.fgy.service;

import com.fgy.po.Blog;
import com.fgy.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    /*获取并转换Markdown为HTML*/
    Blog getAndCovert(Long id);

    /*归档查询*/
    Map<String,List<Blog>> archivesBlog();

    Long countBlog();
    //关联查询
    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);

    /*依靠分类查询博客*/
    Page<Blog> listBlogByType(Pageable pageable,BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);
    /*前端搜索博客*/
    Page<Blog> listBlog(String query,Pageable pageable);
    /*返回推荐的博客*/
    List<Blog> listRecommendBlogTop(Integer size);
    /*返回发布的博客*/
    Page<Blog> listBlogByPublic(Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
