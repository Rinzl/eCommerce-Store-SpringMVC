package com.thd.ecommercespringmvc.controller;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.StoreUtils.Utilities;
import com.thd.ecommercespringmvc.model.Cart;
import com.thd.ecommercespringmvc.model.Customer;
import com.thd.ecommercespringmvc.model.Product;
import com.thd.ecommercespringmvc.model.User;
import javafx.util.Pair;
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
import java.util.prefs.AbstractPreferences;

@Controller
public class CartController {
    private static Logger logger = LogManager.getLogger(CartController.class);

    @PostMapping(value = "/checkout/cart/add")
    @ResponseBody
    public String addToCart(HttpSession httpSession, @RequestBody String jsonRequest) {
        JSONObject jsonObject = new JSONObject();
        int id = Utilities.getIdDeleteOrAdd(jsonRequest);
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
        double total = 0;
        if(cart == null) {
            model.addAttribute("cartSize", 0);
        }
        else {
            model.addAttribute("cartSize", cart.countProduct());

            total = getProductFromCart(total, cart, productList);
            cart.setTotalPrice(total);
            httpSession.setAttribute("cart", cart);
        }
        model.addAttribute("loggedInUser", httpSession.getAttribute("loggedInUser"));
        model.addAttribute("totalPrice", Product.formatPrice(total));
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

    @PostMapping(value = "/checkout/cart/remove")
    @ResponseBody
    public String removeFromCart(HttpSession httpSession, @RequestBody String request) {
        JSONObject object = new JSONObject();
        int id = Utilities.getIdDeleteOrAdd(request);
        Cart cart = (Cart) httpSession.getAttribute("cart");
        if (cart!=null) {
            logger.info("Product with id = " + id + " removed");
            cart.removeProduct(id);
            httpSession.setAttribute("cart", cart);
            object.put("status", 1);
            return object.toJSONString();
        }
        logger.info("remove product from cart failed");
        object.put("status", 0);
        return object.toJSONString();
    }
    @PostMapping(value = "/checkout/cart/add-detail")
    @ResponseBody
    public String addToCartDetail(HttpSession httpSession, @RequestBody String request) {
        logger.info(request);
        JSONObject jsonObject = new JSONObject();
        Pair<Integer, Integer> detailInfo = Utilities.addDetailToCart(request);
        if (detailInfo == null) {
            jsonObject.put("status", 1);
            return jsonObject.toJSONString();
        }
        else if (httpSession.getAttribute("cart") != null) {
            logger.info("Product with id : " + detailInfo.getKey() + " added to cart. ");
            Cart cart = (Cart) httpSession.getAttribute("cart");
            cart.addToCart(detailInfo.getKey(), detailInfo.getValue());
            logger.info(cart);
            httpSession.setAttribute("cart", cart);
            jsonObject.put("status", 1);
        } else {
            Cart cart = new Cart();
            logger.info("New cart is created");
            cart.addToCart(detailInfo.getKey(),detailInfo.getValue());
            logger.info("Product with id : " + detailInfo.getKey() + " added to cart. ");
            logger.info(cart);
            httpSession.setAttribute("cart", cart);
            jsonObject.put("status", 1);
        }
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "/checkout")
    public String checkout(HttpSession httpSession, Model model) {
        Cart cart = (Cart) httpSession.getAttribute("cart");
        User user = (User) httpSession.getAttribute("loggedInUser");
        if (cart == null) {
            return "redirect:/checkout/cart";
        }
        double total= 0;
        List<Product> productList = new ArrayList<>();
        total = getProductFromCart(total, cart, productList);
        model.addAttribute("customer", Application.database.getCustomer(user.getId()));
        model.addAttribute("loggedInUser", user);
        model.addAttribute("cartSize", Utilities.countProduct(httpSession.getAttribute("cart")));
        model.addAttribute("totalPrice", Product.formatPrice(total));
        model.addAttribute("productList", productList);
        return "checkout";
    }

    @PostMapping(value = "/checkout/order")
    @ResponseBody
    public String order(HttpSession session, @RequestBody String request) {
        JSONObject object = new JSONObject();
        User user = (User) session.getAttribute("loggedInUser");
        Cart cart = (Cart) session.getAttribute("cart");
        if (user == null) {
            object.put("status", -1);
            return object.toJSONString();
        }
        Customer requestCustomer = Utilities.customerFromJSON(request);
        Customer customer = Application.database.getCustomer(user.getId());
        if (!Utilities.compareCustomer(customer, requestCustomer)) {
            Application.database.createCustomer(requestCustomer, user.getId());
            customer = Application.database.getCustomer(user.getId());
        }
        if (Application.database.createOrder(cart, customer.getId())) {
                object.put("status", 1);
                logger.info("A new order is created with detail : " + cart +"\t" + customer);
                return object.toJSONString();
            }
        object.put("status", 0);
        return object.toJSONString();
    }

    private double getProductFromCart(double total, Cart cart, List<Product> productList) {
        for (int id : cart.getAllProductId()) {
            Product p = Application.database.getProduct(id);
            p.setQuantityInCart(cart.getProductOrderNumber(p.getId()));
            productList.add(p);
            total+= p.getTotalPrice();
        }
        return total;
    }
}
