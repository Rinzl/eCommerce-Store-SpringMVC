package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.model.Product;
import com.thd.ecommercespringmvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
    public String index(Model  model, HttpSession httpSession) {
        List<Product> test = new ArrayList<>();
        boolean isLoggedIn = httpSession.getAttribute("isLoggedIn") != null;
        for (int i = 0; i<5; i++) {
            Product p = new Product();
            p.setId(i);
            p.setName("Iphone " + i);
            test.add(p);
        }

        model.addAttribute("featureProduct", test);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "product", params = "page", method = RequestMethod.GET)
    public String product(Model model,@RequestParam int page) {
        System.out.println(page);
        List<String> test = new ArrayList<>();
        test.add("Iphone X");
        test.add("Iphone Xs");
        test.add("Iphone Xs Max");
        test.add("Macbook Pro 2018");
        model.addAttribute("productList", test);
        return "product";
    }

    @RequestMapping(value = "product", method = RequestMethod.GET)
    public String product() {
        return "redirect:/product?page=1";
    }
    @RequestMapping(value = "product/detail", params = "id", method = RequestMethod.GET)
    public String productDetail(Model model) {
        model.addAttribute("product", 1);
        return "product-detail";
    }
}
