package com.fgy.web;

import com.fgy.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutMEController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/aboutME")
    public String aboutMe(){
        return "aboutME";
    }

}
