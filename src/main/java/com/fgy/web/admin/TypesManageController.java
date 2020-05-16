package com.fgy.web.admin;


import com.fgy.po.Type;
import com.fgy.service.TypeService;
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
 * 分类Web控制器
 */
@Controller
@RequestMapping("/admin")
public class TypesManageController {

    @Autowired
    private TypeService typeService;

    /*分类管理列表*/
    @GetMapping("/typesManage")
    public String typesManage(@PageableDefault(size = 6,sort={"id"},direction = Sort.Direction.DESC)
                                          Pageable pageable,Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/typesManage";
    }
    /*新增分类*/
    @GetMapping("/typesManage/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/typesManage-input";
    }
    /*修改分类*/
    @GetMapping("/typesManage/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/typesManage-input";
    }
    /*新增后台验证*/
    /*传递参数列表内的Type和BindingResult必须相邻--校验必需*/
    @RequestMapping("/typesManage")
    public String post(@Valid Type type , BindingResult result , RedirectAttributes attributes){
        Type type1=typeService.getTypeByName(type.getName());
        if(type1 != null){
            //自定义result返回错误信息 (对应验证变量名,错误名,错误信息)
            result.rejectValue("name","nameError","该分类已存在！");
        }
        if(result.hasErrors()){
            return "admin/typesManage-input";
        }
        Type t=typeService.saveType(type);
        if (t == null){
            attributes.addFlashAttribute("message","操作失败!");
        }else{
            attributes.addFlashAttribute("message","操作成功!");
        }
        return "redirect:/admin/typesManage";
    }
    /*修改后台验证*/
    /*传递参数列表内的Type和BindingResult必须相邻--校验必需*/
    @RequestMapping("/typesManage/{id}")
    public String editPost(@Valid Type type , BindingResult result ,@PathVariable Long id,RedirectAttributes attributes){
        Type type1=typeService.getTypeByName(type.getName());
        if(type1 != null){
            //自定义result返回错误信息 (对应验证变量名,错误名,错误信息)
            result.rejectValue("name","nameError","该分类已存在！");
        }
        if(result.hasErrors()){
            return "admin/typesManage-input";
        }
        Type t=typeService.updateType(id,type);
        if (t == null){
            attributes.addFlashAttribute("message","更新分类失败!");
        }else{
            attributes.addFlashAttribute("message","更新分类成功!");
        }
        return "redirect:/admin/typesManage";
    }
    /*删除分类*/
    @RequestMapping("/typesManage/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除分类成功！");
        return "redirect:/admin/typesManage";
    }
}
