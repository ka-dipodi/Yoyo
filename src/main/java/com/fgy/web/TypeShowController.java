package com.fgy.web;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fgy.po.Blog;
import com.fgy.po.Type;
import com.fgy.service.BlogService;
import com.fgy.service.TypeService;
import com.fgy.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Type> types= typeService.listTypeTop(1000);
        if(id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlogByType(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "types";
    }

}
