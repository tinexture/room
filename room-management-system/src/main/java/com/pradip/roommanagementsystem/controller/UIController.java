package com.pradip.roommanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {
    @GetMapping("/")
    public String getIndex(){
        return "users";
    }

    @GetMapping("create-user")
    public String createuserPage(){
        return "user_create";
    }
}
