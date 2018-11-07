package com.thd.ecommercespringmvc.StoreUtils;

import com.thd.ecommercespringmvc.Application;
import com.thd.ecommercespringmvc.model.*;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utilities {
    public static JSONParser parser = new JSONParser();
    /**
     *
     * @param cartObject : gio hang
     * @return so luong mat hang trong gio
     */
    public static int countProduct(Object cartObject) {
        int cartSize = 0;
        if(cartObject != null) {
            cartSize = ((Cart) cartObject).countProduct();
        }
        return cartSize;
    }

    /**
     *
     * @param jsonRequest : JSON post thong tin mat hang duoc them/xoa vao gio
     * @return id cua mat hang trong gio
     * ParseException : return -1 neu gap loi khi parse JSON
     */
    public static int getIdDeleteOrAdd(String jsonRequest) {
        try {
            JSONObject object = (JSONObject) parser.parse(jsonRequest);
            return Integer.parseInt((String) object.get("id"));
        } catch (ParseException e) {
            return -1;
        }
    }

    public static Map<Integer, Integer> getIdsUpdateCart(String jsonReq) {
        Map<Integer, Integer> map = new HashMap<>();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(jsonReq);
            for (Object aJsonArray : jsonArray) {
                JSONObject object = (JSONObject) aJsonArray;
                int id = Integer.parseInt((String) object.get("id"));
                int cartQuantity = Integer.parseInt((String) object.get("num"));
                map.put(id, cartQuantity);
            }
            return map;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Pair<Integer, Integer> addDetailToCart(String request) {
        try {
            JSONObject object = (JSONObject) parser.parse(request);
            return new Pair<>(Integer.parseInt((String) object.get("id")), Integer.parseInt((String) object.get("num")));
        } catch (ParseException e) {
            return null;
        }
    }
    public static Customer customerFromJSON(String request) {
        try {
            JSONObject object = (JSONObject) parser.parse(request);
            String name = (String) object.get("name");
            String address = (String) object.get("address");
            String phone = (String) object.get("phone");
            Customer customer = new Customer();
            customer.setAddress(address);
            customer.setPhone(phone);
            customer.setName(name);
            return customer;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean compareCustomer(Customer c1, Customer c2) {
        if(c1 == null || c2 == null) return false;
        return c1.getName().equals(c2.getName()) &&
                c1.getAddress().equals(c2.getAddress()) &&
                c1.getPhone().equals(c2.getPhone());
    }

    public static Product convertRawProduct(ProductFormUpload productFormUpload) {
        Product product = new Product();
        product.setName(productFormUpload.getName());
        product.setQuantity(Integer.parseInt(productFormUpload.getQuantity()));
        product.setIdCategory(Integer.parseInt(productFormUpload.getCategory()));
        product.setPrice(Double.parseDouble(productFormUpload.getPrice()));
        product.setDescription(productFormUpload.getDescription());
        product.setDescriptionDetail(productFormUpload.getDescriptionDetail());
        product.setImage(productFormUpload.getImage());
        return product;
    }

    public static Category getCategoryFromJSON(String request) {
        Category c = new Category();
        try {
            JSONObject object = (JSONObject) parser.parse(request);
            c.setId(object.get("id") == null ? 0 : Integer.parseInt((String) object.get("id")));
            c.setName((String) object.get("cateName"));
            c.setDescription((String) object.get("cateDesc"));
            if (c.isValid()) {
                return c;
            }
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bill getBillFromJSON(String request) {
        try {
            JSONObject object = (JSONObject) parser.parse(request);
            Bill bill = new Bill();
            bill.setId(Integer.parseInt((String) object.get("id")));
            bill.setStatus(Integer.parseInt((String) object.get("bill_status")));
            return bill;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static User getUserFromJSON(String request) {
        try {
            JSONObject object = (JSONObject) parser.parse(request);
            User user = new User();
            user.setId(Integer.parseInt((String) object.get("id")));
            user.setUserName((String) object.get("name"));
            user.setPassword((String) object.get("pass"));
            user.setRole(Integer.parseInt((String) object.get("role")));
            return user;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
