package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.Cart;
import com.thd.ecommercespringmvc.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    private static Logger logger = LogManager.getLogger(CartController.class);

    @PostMapping(value = "/checkout/cart/add")
    @ResponseBody
    public String addToCart(HttpSession httpSession, @RequestBody String jsonRequest) {
        JSONObject jsonObject = new JSONObject();
        int id = Utilities.getIdFromJsonRequestAddCart(jsonRequest);
        if (httpSession.getAttribute("cart") != null) {
            logger.info("Product with id : " + id + " added to cart. ");
            Cart cart = (Cart) httpSession.getAttribute("cart");
            cart.addToCart(id);
            logger.info(cart);
            httpSession.setAttribute("cart", cart);
            jsonObject.put("status", 1);
        } else {
            Cart cart = new Cart();
            logger.info("New cart is created");
            cart.addToCart(id);
            logger.info("Product with id : " + id + " added to cart. ");
            logger.info(cart);
            httpSession.setAttribute("cart", cart);
            jsonObject.put("status", 1);
        }
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/checkout/cart", method = RequestMethod.GET)
    public String cart(Model model, HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute("cart");
        List<Product> productList = new ArrayList<>();

        if(cart == null) {
            model.addAttribute("cartSize", 0);
        }
        else {
            model.addAttribute("cartSize", cart.countProduct());

            for (int id : cart.getAllProductId()) {
                Product p = Application.database.getProduct(id);
                p.setQuantityInCart(cart.getProductOrderNumber(p.getId()));
                productList.add(p);
            }
        }
        model.addAttribute("productList", productList);
        return "cart";
    }

    @PostMapping(value = "/checkout/cart/update")
    @ResponseBody
    public String updateCart(HttpSession httpSession, @RequestBody String jsonReq) {
        Map<Integer, Integer> ids = Utilities.getIdsUpdateCart(jsonReq);
        JSONObject object = new JSONObject();
        if (ids != null) {
            logger.info("Request to update cart : " + ids);
            Cart cart = (Cart) httpSession.getAttribute("cart");
            for (Map.Entry<Integer, Integer> entry : ids.entrySet()) {
                cart.updateCart(entry.getKey(), entry.getValue());
            }
            logger.info("Cart after update : " + cart);
            httpSession.setAttribute("cart", cart);
            object.put("status", 1);
            return object.toJSONString();
        }
        logger.info("Update cart failed");
        object.put("status", 0);
        return object.toJSONString();
    }
}
