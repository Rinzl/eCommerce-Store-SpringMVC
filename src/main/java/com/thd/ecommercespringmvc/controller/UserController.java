package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @PostMapping(value = "/login")
    public String login(HttpSession httpSession, @ModelAttribute User user ) {
        if(user.checkValidUser()) {
            httpSession.setAttribute("isLoggedIn", true);
        }

        System.out.println(user);
        return "redirect:/index";
    }
}
