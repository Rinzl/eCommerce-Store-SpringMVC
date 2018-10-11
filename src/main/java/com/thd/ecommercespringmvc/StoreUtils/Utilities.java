package com.thd.ecommercespringmvc.StoreUtils;

import com.thd.ecommercespringmvc.model.Cart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utilities {
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
     * @param jsonRequest : JSON post thong tin mat hang duoc them vao gio
     * @return id cua mat hang trong gio
     * ParseException : return -1 neu gap loi khi parse JSON
     */
    public static int getIdFromJsonRequestAddCart(String jsonRequest) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(jsonRequest);
            return Integer.parseInt((String) object.get("id"));
        } catch (ParseException e) {
            return -1;
        }
    }

    public static Map<Integer, Integer> getIdsUpdateCart(String jsonReq) {
        JSONParser parser = new JSONParser();
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
}
