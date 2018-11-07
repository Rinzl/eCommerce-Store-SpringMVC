package com.thd.ecommercespringmvc.StoreUtils;

public class QueryUtils {

    public static final String USER_INFO_PS = "SELECT * FROM store.users WHERE username = ? and password = ?";
    public static final String ADMIN_INFO_PS = "SELECT * FROM store.users WHERE username = ? and password = ? and ( role = 2 or role = 3 )";
    public static final String USER_UPDATE = "UPDATE store.users SET username = ?, password = ?, role = ? where id = ?";
    public static final String USER_EXIST_PS = "SELECT * FROM store.users WHERE username = ? ";
    public static final String PRODUCT_PS = "SELECT * FROM store.products WHERE id = ?";
    public static final String PRODUCT_FEATURED_QUERY = "SELECT * FROM store.products order by create_at limit 6";
    public static final String PRODUCT_COUNT_QUERY = "SELECT count(*) as count FROM store.products";
    public static final String PRODUCT_PAGE_QUERY = "SELECT * FROM store.products limit 9 offset ?";
    public static final String PRODUCT_UPDATE_QUERY = "UPDATE store.products set name = ?, id_category = ?, quantity = ?, description = ?, description_detail = ?, image = ?, price = ? where id = ?";
    public static final String QUANTITY_UPDATE_PRODUCT = "update store.products set quantity = quantity - ? where id = ?";
    public static final String PRODUCT_UPDATE1_QUERY = "UPDATE store.products set name = ?, id_category = ?, quantity = ?, description = ?, description_detail = ?, price = ? where id = ?";

    public static final String USER_CREATE_PS = "INSERT INTO store.users (username, password, role) values (?,?,?)";
    public static final String PRODUCT_CREATE_PS = "INSERT INTO store.products (name, id_category, quantity, description, description_detail, image, price) values (?,?,?,?,?,?,?)";
    public static final String CUSTOMER_DELETE_PS = "DELETE FROM store.customer where id = ?";
    public static final String CUSTOMER_CREATE_PS = "INSERT INTO store.customer (id_user, name, address, phone, is_default) values (?,?,?,?,?)";
    public static final String BILL_CREATE_PS = "INSERT INTO store.bills (id_customer, total, status) values (?,?,?)";
    public static final String BILL_DETAIL_CREATE_PS = "INSERT INTO store.bill_detail (id_bill, id_product, quantity) values (?,?,?)";
    public static final String GET_CUSTOMER_PS = "SELECT id, id_user, name, address, phone, is_default FROM store.customer where id_user = ?";

    public static final String CATEGORY_PS = "SELECT * FROM store.category WHERE id = ? ";
    public static final String DELETE_CATEGORY = "DELETE FROM store.category WHERE id = ? ";
    public static final String INSERT_CATEGORY = "INSERT INTO store.category ( name , description ) VALUES ( ? , ? )";
    public static final String UPDATE_CATEGORY = "UPDATE store.category set name = ? , description = ? WHERE id = ? ";

    public static final String BILL_DETAIL_PS = "SELECT * FROM store.bill_detail WHERE id = ? ";
    public static final String DELETE_BILL_DETAIL = "DELETE FROM store.bill_deatil WHERE id = ? ";
    public static final String UPDATE_BILL_DETAIL = "UPDATE store.bill_detail set quantity = ? , status_active = ? , status_delete = ? WHERE id = ? ";

    public static final String BILLS_PS = "SELECT * FROM store.bills WHERE id = ? ";
    public static final String BILLS_ALL = "SELECT * FROM store.bills";
    public static final String DELETE_BILLS = "DELETE FROM store.bills WHERE id = ";
    public static final String UPDATE_BILLS = "UPDATE store.bills set total = ? , note = ? , status_active = ? , status_delete = ? WHERE id = ? ";

}