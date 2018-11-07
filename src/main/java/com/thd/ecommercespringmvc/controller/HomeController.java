package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    private static Logger logger = LogManager.getLogger(HomeController.class);

    @RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
    public String index(Model  model, HttpSession httpSession) {
        List<Product> test = Application.database.getFeaturedProduct();
        model.addAttribute("featureProduct", test);
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "index";
    }

    @RequestMapping(value = "/products", params = "page", method = RequestMethod.GET)
    public String products(HttpSession httpSession,  Model model,@RequestParam int page) {
        List<Product> products = Application.database.getProductList(page, 9);
        int productListSize = Application.database.productListSize();
        int maxList = (productListSize / 9);
        if (productListSize % 9 != 0) maxList+=1;
        model.addAttribute("productList", products);
        model.addAttribute("page", page);
        model.addAttribute("maxList", maxList);
        model.addAttribute("categoryList", Application.database.getCategoryList());
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "product";
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String products() {
        return "redirect:/products?page=1";
    }


    @RequestMapping(value = "/products/detail", params = "id", method = RequestMethod.GET)
    public String productDetail(HttpSession httpSession, @RequestParam int id, Model model) {
        Product p = Application.database.getProduct(id);
        model.addAttribute("product", p);
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        return "product-detail";
    }
}
