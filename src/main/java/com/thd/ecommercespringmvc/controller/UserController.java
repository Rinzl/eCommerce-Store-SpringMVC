package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping(value = "account/login")
    public String login(HttpSession httpSession, Model model, @ModelAttribute User user ) {
        User validUser = Application.database.getUserInfo(user.getUserName(), user.getPassword());
        if(validUser != null) {
            logger.info("User has logged in : " +validUser);
            httpSession.setAttribute("loggedInUser", validUser);
            return "redirect:/index";
        }
        else {
            logger.info("Wrong username or password !");
            model.addAttribute("loginFailed", true);
            return "login";
        }
    }

    @GetMapping(value = "/account/login")
    public String loginPage(HttpSession httpSession, Model model) {
        model.addAttribute("loginFailed", false);
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        if (httpSession.getAttribute("validUser")!=null) {
            return "redirect:/account/info";
        }
        return "login";
    }

    @GetMapping(value = "/account/register")
    public String register(HttpSession httpSession, Model model) {
        model.addAttribute("registerMessage", null);
        model.addAttribute("user", new User());
        if(httpSession.getAttribute("vaidUser") == null) {
            model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
            return "register";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/account/register")
    public String register(HttpSession httpSession, @ModelAttribute User registerUser, Model model) {
        User validUser = Application.database.getUserInfo(registerUser.getUserName(), registerUser.getPassword());
        if (!registerUser.isValidUser() && validUser == null) {
            model.addAttribute("registerMessage", "Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
            logger.warn("Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
            return "register";
        }
        logger.info("A new user is registered : " + validUser);
        httpSession.setAttribute("validUser", validUser);
        return "redirect:/index";
    }
}
