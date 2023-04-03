package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.projection.UserPersonal;
import com.pradip.roommanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class UIController {
//    @Autowired
//    private UserService userService;

    @GetMapping("/")
    public String getIndex(){
        return "redirect:/login.html";
    }

    @GetMapping("/keep-alive")
    @ResponseBody
    public String getKeepAlive(){
        return "Server is running......";
    }
//    @GetMapping("/")
//    public String getIndex(){
//        return "users";
//    }
//
//    @GetMapping("/create-user")
//    public String createuserPage(){
//        return "user_create";
//    }
//
//    @GetMapping("/delete-user")
//    public String deleteUserPage(Model model) throws ClassNotFoundException {
//        List<UserPersonal> userPersonal = (List<UserPersonal>) userService.getAllUsers("UserPersonal").getData();
//        model.addAttribute("users",userPersonal);
//        return "user_delete";
//    }
}
