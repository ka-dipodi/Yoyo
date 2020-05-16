package com.fgy.web;

import com.fgy.NotFoundException;
import com.fgy.service.BlogService;
import com.fgy.service.TagService;
import com.fgy.service.TypeService;
import com.fgy.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /*首页展示博客列表，热门分类，热门标签*/
    @GetMapping("/")
    public String index(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        //model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("page",blogService.listBlogByPublic(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(6));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";

    }
    /*搜索博客*/
    @PostMapping("/search")
    public String search(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }
    /*浏览博客文章详情*/
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndCovert(id));
        return "blog";

    }
    /*底部局部刷新最新博客*/
    @GetMapping("/footer/rankTop")
    public String rankTop(Model model){
        model.addAttribute("RankTop",blogService.listRecommendBlogTop(3));
        return "_fragments :: RankTop";
    }
}
