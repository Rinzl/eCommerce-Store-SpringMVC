package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.Customer;
import com.thd.ecommercespringmvc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping(value = "account/login")
    public String login(HttpSession httpSession, Model model, @ModelAttribute User user ) {
        logger.info("Attempt login : " + user);
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
        model.addAttribute("user", new User());
        model.addAttribute("info", null);
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        if (httpSession.getAttribute("loggedInUser")!=null) {
            return "redirect:/account/info";
        }
        return "login";
    }

    @GetMapping(value = "/account/register")
    public String register(HttpSession httpSession, Model model) {
        model.addAttribute("registerMessage", null);
        model.addAttribute("user", new User());
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        if(httpSession.getAttribute("vaidUser") == null) {
            model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
            return "register";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/account/register")
    public String register(HttpSession httpSession, @ModelAttribute User registerUser, Model model) {
        boolean isExist = Application.database.isUsernameExist(registerUser.getUserName().trim());
        if (!registerUser.isValidUser() || isExist) {
            model.addAttribute("registerMessage", "Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
            logger.warn("Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
            return "register";
        }
        if (Application.database.createUser(registerUser)) {
            logger.info("A new user is registered : " + registerUser);
            model.addAttribute("info", "Đăng kí thành công, vui lòng đăng nhập vào hệ thống");
            return "login";
        }
        model.addAttribute("registerMessage", "Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
        logger.warn("Thông tin đăng kí không hợp lệ hoặc đã tồn tại");
        return "register";
    }
    @GetMapping(value = "account/logout")
    public String logout(HttpSession httpSession) {
        httpSession.setAttribute("loggedInUser", null);
        return "redirect:/index";
    }

    @GetMapping(value = "account/info")
    public String info(HttpSession httpSession, Model model) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/account/login";
        }
        List<Customer> customerList = Application.database.getCustomerList(loggedInUser.getId());
        model.addAttribute("address", customerList.get(0));
        model.addAttribute("addressSize", customerList.size());
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "account-info";
    }
    @GetMapping(value = "account/addresses")
    public String addresses(HttpSession httpSession, Model model) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/account/login";
        }
        List<Customer> customerList = Application.database.getCustomerList(loggedInUser.getId());
        model.addAttribute("customers", customerList);
        model.addAttribute("customerCreater", new Customer());
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));

        return "addresses";
    }

    @PostMapping(value = "/account/add-address")
    public String addAddress(HttpSession httpSession, @ModelAttribute Customer customer) {
        User loggedInUser = (User) httpSession.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/account/login";
        }
        logger.info("New address added!");
        logger.info(customer);
        Application.database.createCustomer(customer, loggedInUser.getId());
        return "redirect:/account/addresses";
    }
    @PostMapping(value = "/account/delete-address")
    @ResponseBody
    public String deleteAddress(HttpSession httpSession, @RequestBody String request) {
        JSONObject jsonObject = new JSONObject();
        int id =Utilities.getIdDeleteOrAdd(request);
        if (Application.database.deleteCustomer(id)) {
            jsonObject.put("status", 1);
            return jsonObject.toJSONString();
        }
        jsonObject.put("status", 0);
        return jsonObject.toJSONString();
    }
}
