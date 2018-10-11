package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
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
        List<Product> test = Application.database.getFeaturedProduct();
        initDemoList(test);
        model.addAttribute("featureProduct", test);
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "index";
    }

    private void initDemoList(List<Product> test) {
        for (int i = 0; i<10; i++) {
            Product p = new Product();
            p.setId(100001);
            p.setName("Iphone " + i);
            p.setPrice(29900000);
            test.add(p);
        }
    }

    @RequestMapping(value = "/products", params = "page", method = RequestMethod.GET)
    public String products(HttpSession httpSession,  Model model,@RequestParam int page) {
        List<Product> products = Application.database.getProductList(page, 10);
        initDemoList(products);
        model.addAttribute("productList", products);
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "product";
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String products() {
        return "redirect:/products?page=1";
    }


    @RequestMapping(value = "/products/detail", params = "id", method = RequestMethod.GET)
    public String productDetail(HttpSession httpSession, Model model) {
        model.addAttribute("product", 1);
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "product-detail";
    }
}
