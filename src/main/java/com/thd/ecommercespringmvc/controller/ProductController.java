package com.thd.ecommercespringmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @PostMapping(value = "/addtocart", params = "id")
    public String addToCart(Model model, @RequestParam int id) {
        System.out.println("Product with " + id + " added to cart. ");
        
    }
}
