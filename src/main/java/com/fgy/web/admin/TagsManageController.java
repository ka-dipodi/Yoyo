package com.fgy.web.admin;


import com.fgy.po.Tag;
import com.fgy.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 标签Web控制器
 *
 */
@Controller
@RequestMapping("/admin")
public class TagsManageController {

    @Autowired
    private TagService tagService;

    /*标签管理列表*/
    @GetMapping("/tagsManage")
    public String tagsManage(@PageableDefault(size = 6,sort={"id"},direction = Sort.Direction.DESC)
                                          Pageable pageable,Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tagsManage";
    }
    /*新增标签*/
    @GetMapping("/tagsManage/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tagsManage-input";
    }
    /*修改标签*/
    @GetMapping("/tagsManage/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tagsManage-input";
    }
    /*后台校验*/
    /*传递参数列表内的Tag和BindingResult必须相邻--校验必需*/
    @RequestMapping("/tagsManage")
    public String post(@Valid Tag tag , BindingResult result , RedirectAttributes attributes){
        Tag tag1=tagService.getTagByName(tag.getName());
        if(tag1 != null){
            //自定义result返回错误信息 (对应验证变量名,错误名,错误信息)
            result.rejectValue("name","nameError","该标签已存在！");
        }
        if(result.hasErrors()){
            return "admin/tagsManage-input";
        }
        Tag t=tagService.saveTag(tag);
        if (t == null){
            attributes.addFlashAttribute("message","操作失败!");
        }else{
            attributes.addFlashAttribute("message","操作成功!");
        }
        return "redirect:/admin/tagsManage";
    }
    /*传递参数列表内的Tag和BindingResult必须相邻--校验必需*/
    @RequestMapping("/tagsManage/{id}")
    public String editPost(@Valid Tag tag , BindingResult result ,@PathVariable Long id,RedirectAttributes attributes){
        Tag tag1=tagService.getTagByName(tag.getName());
        if(tag1 != null){
            //自定义result返回错误信息 (对应验证变量名,错误名,错误信息)
            result.rejectValue("name","nameError","该标签已存在！");
        }
        if(result.hasErrors()){
            return "admin/tagsManage-input";
        }
        Tag t=tagService.updateTag(id,tag);
        if (t == null){
            attributes.addFlashAttribute("message","更新标签失败!");
        }else{
            attributes.addFlashAttribute("message","更新标签成功!");
        }
        return "redirect:/admin/tagsManage";
    }
    /*删除标签*/
    @RequestMapping("/tagsManage/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除标签成功！");
        return "redirect:/admin/tagsManage";
    }
}
