package com.fgy.web.admin;


import com.fgy.po.Blog;
import com.fgy.po.User;
import com.fgy.service.BlogService;
import com.fgy.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * 博客管理控制器
 */
@Controller
@RequestMapping("/admin")
public class BlogsManageController {

    /*设置博客跳转路径*/
    private static final String  INPUT="admin/blogsManage-input";
    private static final String  LIST="admin/blogsManage";
    private static final String  REDIRECT_LIST="redirect:/admin/blogsManage";
    private static final String  COMMENTLIST="admin/commentsManage";


    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    /*博客管理列表*/
    @GetMapping("/blogsManage")
    public String blogsManage(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }

    /*局部刷新*/
    /*此方法返回一个局部片段,依据传递的值(blogList)刷新页面相对应指定的区域(th:fragment="blogList"),而非刷新整个页面;*/
    /*组合搜索博客*/
    @PostMapping("/blogsManage/search")
    public String search(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogsManage :: blogList";
    }

    /*初始化分类以及标签*/
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }
    /*点击新增博客跳转*/
    @GetMapping("/blogsManage/input")
    public String input(Model model){
        setTypeAndTag(model);//
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    /*点击修改博客跳转*/
    @GetMapping("/blogsManage/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
       setTypeAndTag(model);
       Blog blog=blogService.getBlog(id);
       blog.init();
       model.addAttribute("blog",blog);
       return INPUT;
    }
    /*点击管理博客评论列表*/
    @GetMapping("/commentsManage/{id}/comment")
    public String editcomment(@PathVariable Long id,Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(id));
        return COMMENTLIST;
    }

    /*保存博客*/
    @PostMapping("/blogsManage")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog blog1;
        if (blog.getId()==null){
            blog1=blogService.saveBlog(blog);
        }else{
            blog1=blogService.updateBlog(blog.getId(),blog);
        }
        if(blog1 == null){
            attributes.addFlashAttribute("message","操作失败!");
        } else{
            attributes.addFlashAttribute("message","操作成功！");
        }
        return REDIRECT_LIST;
    }
    /*删除博客*/
    @GetMapping("blogsManage/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除博客成功！");
        return REDIRECT_LIST;
    }

    /*删除评论*/
    @GetMapping("commentsManage/{id}/delete")
    public String deletecomment(@PathVariable Long id,RedirectAttributes attributes){
        Blog b=commentService.findBlog(id);
        commentService.deleteComment(id);
        attributes.addFlashAttribute("message","删除评论成功！");
        return  "redirect:/admin/commentsManage/"+b.getId()+"/comment";
    }
}
