package com.thd.ecommercespringmvc.StoreUtils;

public class QueryUtils {

    public static final String USER_INFO_PS = "SELECT * FROM store.users WHERE username = ? and password = ?";
    public static final String PRODUCT_PS = "SELECT * FROM store.products WHERE id = ?";
    public static final String PRODUCT_FEATURED_QUERY = "SELECT * FROM store.products order by create_at  limit 6";
}
