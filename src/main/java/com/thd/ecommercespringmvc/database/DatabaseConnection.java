package com.thd.ecommercespringmvc.database;

import com.thd.ecommercespringmvc.StoreUtils.OrderStatus;
import com.thd.ecommercespringmvc.StoreUtils.QueryUtils;
import com.thd.ecommercespringmvc.StoreUtils.UserRole;
import com.thd.ecommercespringmvc.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class DatabaseConnection {

    public static final String DATABASE_NAME = "store";
    private static Logger logger = LogManager.getLogger(DatabaseConnection.class);

    private Connection connection;
    private PreparedStatement getUserInfoPS;
    private PreparedStatement getProductPS;
    private PreparedStatement getProductByPage;
    private PreparedStatement createUser;
    private PreparedStatement createCustomer;
    private PreparedStatement getCustomerList;
    private PreparedStatement deleteCustomer;
    private PreparedStatement createBill;
    private PreparedStatement createBillDetail;
    private PreparedStatement createProduct;
    private PreparedStatement updateProduct;
    private PreparedStatement updateProduct1;
    private PreparedStatement updateProductQuantity;

    private PreparedStatement getCategory;
    private PreparedStatement deleteCategory;
    private PreparedStatement updateCategory;
    private PreparedStatement insertCategory;
    private PreparedStatement updateUser;
    private PreparedStatement getAdminInfoPS;
    private Statement statement;
    public DatabaseConnection(){
        openConnection();
        logger.info("Connected to server successfully!");
    }

    private void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root","12345");
            statement = connection.createStatement();
            getUserInfoPS = connection.prepareStatement(QueryUtils.USER_INFO_PS);
            getAdminInfoPS = connection.prepareStatement(QueryUtils.ADMIN_INFO_PS);
            updateUser = connection.prepareStatement(QueryUtils.USER_UPDATE);
            getProductPS = connection.prepareStatement(QueryUtils.PRODUCT_PS);
            getProductByPage = connection.prepareStatement(QueryUtils.PRODUCT_PAGE_QUERY);
            createUser = connection.prepareStatement(QueryUtils.USER_CREATE_PS);
            createCustomer = connection.prepareStatement(QueryUtils.CUSTOMER_CREATE_PS);
            getCustomerList = connection.prepareStatement(QueryUtils.GET_CUSTOMER_PS);
            deleteCustomer = connection.prepareStatement(QueryUtils.CUSTOMER_DELETE_PS);
            createBill = connection.prepareStatement(QueryUtils.BILL_CREATE_PS);
            createBillDetail = connection.prepareStatement(QueryUtils.BILL_DETAIL_CREATE_PS);
            createProduct = connection.prepareStatement(QueryUtils.PRODUCT_CREATE_PS);
            updateProduct = connection.prepareStatement(QueryUtils.PRODUCT_UPDATE_QUERY);
            updateProduct1 = connection.prepareStatement(QueryUtils.PRODUCT_UPDATE1_QUERY);
            updateProductQuantity = connection.prepareStatement(QueryUtils.QUANTITY_UPDATE_PRODUCT);

            getCategory = connection.prepareStatement(QueryUtils.CATEGORY_PS);
            deleteCategory = connection.prepareStatement(QueryUtils.DELETE_CATEGORY);
            updateCategory = connection.prepareStatement(QueryUtils.UPDATE_CATEGORY);
            insertCategory = connection.prepareStatement(QueryUtils.INSERT_CATEGORY);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
            System.exit(1);
        }
    }
    public User getUserInfo(String userName, String password) {
        try {
            getUserInfoPS.setString(1, userName);
            getUserInfoPS.setString(2, password);
            ResultSet set = getUserInfoPS.executeQuery();
            getUserInfoPS.clearParameters();
            if (set.next()) {
                User user = new User();
                user.setId(set.getInt("id"));
                user.setUserName(set.getString("username"));
                user.setPassword(set.getString("password"));
                return user;
            } else return null;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
    public boolean isUsernameExist(String username) {
        try {
            logger.info(username);
            ResultSet rs = statement.executeQuery("SELECT * from store.users WHERE users.username = '" + username +"' limit 1");
            return rs.next();
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }
    public boolean createUser(User user) {
        try {
            createUser.setString(1,user.getUserName());
            createUser.setString(2, user.getPassword());
            createUser.setInt(3, user.getRole());
            int kq = createUser.executeUpdate();
            return kq == 1;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    public Product getProduct(int id) {
        Product product = null;
        try {
            getProductPS.setInt(1, id);
            ResultSet rs = getProductPS.executeQuery();
            getProductPS.clearParameters();
            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDescription(rs.getString("description"));
                product.setDescriptionDetail(rs.getString("description_detail"));
                product.setImage(rs.getString("image"));
                product.setPrice(rs.getDouble("price"));
                product.setCreateDate(rs.getTimestamp("create_at"));
            }
            return product;
        } catch (SQLException e) {
            logger.error(e);
            return product;
        }
    }

    public List<Product> getProductList(int page, int productPerPage) {
        List<Product> products = new ArrayList<>();
        try {
            getProductByPage.setInt(1, productPerPage*(page - 1));
            ResultSet rs = getProductByPage.executeQuery();
            getProductByPage.clearParameters();
            getListProduct(products, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public int createProduct(Product product) {
        try {
            createProduct.setString(1, product.getName());
            createProduct.setInt(2, product.getIdCategory());
            createProduct.setInt(3, product.getQuantity());
            createProduct.setString(4, product.getDescription());
            createProduct.setString(5, product.getDescriptionDetail());
            createProduct.setString(6, product.getImage());
            createProduct.setDouble(7, product.getPrice());
            return createProduct.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        }
    }
    public int updateProduct(Product product,boolean isUpdateImage) {
        try {
            if (isUpdateImage) {
                logger.info("Update with image");
                updateProduct.setString(1, product.getName());
                updateProduct.setInt(2, product.getIdCategory());
                updateProduct.setInt(3, product.getQuantity());
                updateProduct.setString(4, product.getDescription());
                updateProduct.setString(5, product.getDescriptionDetail());
                updateProduct.setString(6, product.getImage());
                updateProduct.setDouble(7, product.getPrice());
                updateProduct.setInt(8, product.getId());
                return updateProduct.executeUpdate();
            } else {
                logger.info("update without image");
                updateProduct1.setString(1, product.getName());
                updateProduct1.setInt(2, product.getIdCategory());
                updateProduct1.setInt(3, product.getQuantity());
                updateProduct1.setString(4, product.getDescription());
                updateProduct1.setString(5, product.getDescriptionDetail());
                updateProduct1.setDouble(6, product.getPrice());
                updateProduct1.setInt(7, product.getId());
                return updateProduct1.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return 0;
        }
    }

    private boolean reduceProductQuantity(int id, int reduceAmount) {
        try {
            updateProductQuantity.setInt(1, reduceAmount);
            updateProductQuantity.setInt(2, id);
            updateProductQuantity.executeUpdate();
            updateProductQuantity.clearParameters();
            logger.info("Product with id : " + id + " is reduced with amount = " + reduceAmount);
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        try {
            statement.executeUpdate("DELETE FROM store.products WHERE id = " + id);
            return true;
        } catch (SQLException e) {
            logger.error("Xóa product thất bại : " + e.getMessage());
            return false;
        }
    }

    public int productListSize() {
        try {
            ResultSet rs = statement.executeQuery(QueryUtils.PRODUCT_COUNT_QUERY);
            if (rs.next()) {
                return rs.getInt("count");
            }
            else throw new SQLException();
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }
    }
    public List<Product> getFeaturedProduct() {
        List<Product> products = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(QueryUtils.PRODUCT_FEATURED_QUERY);
            getListProduct(products, rs);
            return products;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT products.id, products.name, category.name as cate_name,products.description, products.description_detail, price, quantity, image, id_category, products.create_at FROM store.products inner join store.category on id_category = category.id;");
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setIdCategory(rs.getInt("id_category"));
                product.setName(rs.getString("name"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDescription(rs.getString("description"));
                product.setDescriptionDetail(rs.getString("description_detail"));
                product.setImage(rs.getString("image"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getString("cate_name"));
                product.setCreateDate(rs.getTimestamp("create_at"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
    public boolean createCustomer(Customer customer, int userID) {
        try {
            createCustomer.setInt(1, userID);
            createCustomer.setString(2, customer.getName());
            createCustomer.setString(3, customer.getAddress());
            createCustomer.setString(4, customer.getPhone());
            createCustomer.setInt(5, customer.getDefaultAddress());
            return createCustomer.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }
    public List<Customer> getCustomerList(int userID) {
        List<Customer> customerList = new ArrayList<>();
        try {
            getCustomerList.setInt(1, userID);
            ResultSet rs = getCustomerList.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setIdUser(rs.getInt("id_user"));
                c.setAddress(rs.getString("address"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setDefaultAddress(rs.getInt("is_default"));
                customerList.add(c);
            }
            customerList.sort(Comparator.comparing(Customer::getDefaultAddress));
            Collections.reverse(customerList);
            return customerList;
        } catch (SQLException e) {
            logger.error(e);
            return customerList;
        }
    }
    public boolean deleteCustomer(int id) {
        try {
            deleteCustomer.setInt(1, id);
            int kq = deleteCustomer.executeUpdate();
            return kq == 1;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }
    public boolean createOrder(Cart cart, int customerID) {
        try {
            int billID = 0;
            createBill.setInt(1,customerID);
            createBill.setDouble(2, cart.getTotalPrice());
            createBill.setInt(3, OrderStatus.PENDING);
            createBill.executeUpdate();
            ResultSet idRS = statement.executeQuery("SELECT id FROM store.bills where id_customer = "+ customerID +" order by create_at desc limit 1;");
            if (idRS.next()) {
                billID = idRS.getInt("id");
            }
            for (Map.Entry<Integer, Integer> entry : cart.getProductQuantity().entrySet()) {
                createBillDetail.setInt(1, billID);
                createBillDetail.setInt(2,entry.getKey());
                createBillDetail.setInt(3, entry.getValue());
                reduceProductQuantity(entry.getKey(), entry.getValue());
                createBillDetail.executeUpdate();
                createBillDetail.clearParameters();
            }
            return true;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }
    public Customer getCustomer(int userID) {
        Customer customer = new Customer();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM store.customer where id_user = " + userID +" order by create_at desc limit 1");
            resultSetToCustomer(customer, rs);
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Customer getCustomerByID(int customerID) {
        Customer customer = new Customer();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM store.customer WHERE id = " + customerID);
            resultSetToCustomer(customer, rs);
            rs.close();
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void resultSetToCustomer(Customer customer, ResultSet rs) throws SQLException {
        if(rs.next()) {
            customer.setId(rs.getInt("id"));
            customer.setIdUser(rs.getInt("id_user"));
            customer.setName(rs.getString("name"));
            customer.setAddress(rs.getString("address"));
            customer.setPhone(rs.getString("phone"));
            customer.setDefaultAddress(rs.getInt("is_default"));
        }
    }

    public Category getCategory(int id){
        Category category = null;
        try {
            getCategory.setInt(1,id);
            ResultSet rs = getCategory.executeQuery();
            getCategory.clearParameters();
            if (rs.next()){
                category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setActiveStatus(rs.getBoolean("status_active"));
                category.setActiveStatus(rs.getBoolean("status_delete"));
                category.setCreateDate(rs.getTimestamp("create_at"));
            }
            return category;
        } catch (SQLException e) {
            logger.error(e);
            return category;
        }
    }
    public boolean deleteCategory(int id){
        try {
            int a;
            deleteCategory.setInt(1,id);
            a = deleteCategory.executeUpdate();
            if (a > 0) {
                logger.info("delete thanh cong");
                return true;
            } else {
                logger.info("xoa that bai");
                return false;
            }
        } catch (SQLException e) {
            logger.error("xoa that bai " +e.getMessage());
            return false;
        }
    }
    public void updateCategory(Category category){
        try {
            updateCategory.setString(1,category.getName());
            updateCategory.setString(2,category.getDescription());
            updateCategory.setInt(3,category.getId());
            updateCategory.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void insertCategory(Category category){
        try {
            insertCategory.setString(1,category.getName());
            insertCategory.setString(2,category.getDescription());
            insertCategory.executeUpdate();
            //          logger.info(kq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM store.category");
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                categories.add(c);
            }
            return categories;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public List<Bill> billList() {
        List<Bill> billList = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(QueryUtils.BILLS_ALL);
            while (rs.next()) {
                Bill b = new Bill();
                b.setId(rs.getInt("id"));
                b.setTotal((double) rs.getInt("total"));
                int idc = rs.getInt("id_customer");
                b.setId_customer(idc);
                b.setCreateDate(rs.getTimestamp("date_oder"));
                b.setStatus(rs.getInt("status"));
                billList.add(b);
            }
            for (Bill b : billList) {
                b.setCustomer(getCustomerByID(b.getId_customer()));
            }
            return billList;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }

    private void getListProduct(List<Product> products, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setIdCategory(rs.getInt("id_category"));
            product.setName(rs.getString("name"));
            product.setQuantity(rs.getInt("quantity"));
            product.setDescription(rs.getString("description"));
            product.setDescriptionDetail(rs.getString("description_detail"));
            product.setImage(rs.getString("image"));
            product.setPrice(rs.getDouble("price"));
            product.setCreateDate(rs.getTimestamp("create_at"));
            products.add(product);
        }
    }

    public boolean deleteBill(int billID) {
        try {
            statement.executeUpdate(QueryUtils.DELETE_BILLS + billID);
            return true;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    public void updateBill(Bill bill) {
        try {
            statement.executeUpdate("UPDATE store.bills SET status = " + bill.getStatus() + " WHERE id = " + bill.getId());
        } catch (SQLException e) {
            logger.error(e);
        }
    }
    public Map<Integer, Integer> getBillStatus() {
        Map<Integer, Integer> status = new HashMap<>();
        status.put(0, 0);
        status.put(1, 0);
        status.put(-1, 0);
        try {
            ResultSet rs = statement.executeQuery("SELECT status, count(*) as num FROM store.bills group by status;");
            while (rs.next()) {
                status.put(rs.getInt("status"), rs.getInt("num"));
            }
            return status;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
    public int getNewBillToDay() {
        try {
            ResultSet rs = statement.executeQuery("SELECT count(*) as billCount FROM store.bills where date(date_oder) = date(now());");
            if(rs.next()) {
                return rs.getInt("billCount");
            }
            return -1;
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }
    }
    public int getNewSaleToday() {
        try {
            ResultSet rs = statement.executeQuery("SELECT count(*) as saleToday FROM store.bill_detail where date(create_at) = date(now());");
            if(rs.next()) {
                return rs.getInt("saleToday");
            }
            else return -1;
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }

    }
    public int getNewCustomerNumber() {
        try {
            ResultSet rs = statement.executeQuery("SELECT count(*) as cn FROM store.users where role = " + UserRole.CUSTOMNER + " and DATE(create_at) = date(now());");
            if(rs.next()) {
                return rs.getInt("cn");
            } else throw new SQLException();
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }
    }
    public Map<Integer, Integer> monthlySaleNumber(int year) {
        Map<Integer, Integer> monthlySale = new HashMap<>();
        for(int i =1; i<=12;i++) {
            monthlySale.put(i, 0);
        }
        try {
            ResultSet rs = statement.executeQuery("SELECT Month(create_at) as month, sum(quantity) as total FROM store.bill_detail where year(create_at) = "+ year +" group by month(create_at);");
            while (rs.next()) {
                monthlySale.put(rs.getInt("month"), rs.getInt("total"));
            }
            return monthlySale;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
    public Map<String, Integer> salePerCategory(int month, int year) {
        try {
            Map<String, Integer> salePerCate = new HashMap<>();
            List<Category> categoryList = getCategoryList();
            for (Category category : categoryList) {
                salePerCate.put(category.getName(), 0);
            }
            ResultSet resultSet = statement.executeQuery("SELECT category.name, count(*) as total FROM store.category inner join store.products on products.id_category = category.id inner join store.bill_detail on products.id = bill_detail.id_product where month(bill_detail.create_at) = "+ month+" and year(bill_detail.create_at) = "+ year +" group by name");
            while (resultSet.next()) {
                salePerCate.put(resultSet.getString("name"), resultSet.getInt("total"));
            }
            return salePerCate;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }

    public List<User> getEmpList() {
        List<User> userList = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM store.users where role = " + UserRole.ADMIN + " or role = " + UserRole.SALE);
            while (rs.next()) {
                User u = new User();
                u.setUserName(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setId(rs.getInt("id"));
                u.setRole(rs.getInt("role"));
                userList.add(u);
            }
            return userList;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }

    public boolean deleteUser(int userID) {
        try {
            statement.executeUpdate("DELETE FROM store.users WHERE id = " + userID);
            return true;
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    public void updateUser(User user) {
        try {
            updateUser.setString(1,user.getUserName());
            updateUser.setString(2,user.getPassword());
            updateUser.setInt(3,user.getRole());
            updateUser.setInt(4, user.getId());
            updateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public User getAdminInfo(String userName, String password) {
        try {
            getAdminInfoPS.setString(1, userName);
            getAdminInfoPS.setString(2, password);
            ResultSet set = getAdminInfoPS.executeQuery();
            if (set.next()) {
                User user = new User();
                user.setId(set.getInt("id"));
                user.setUserName(set.getString("username"));
                user.setPassword(set.getString("password"));
                user.setRole(set.getInt("role"));
                return user;
            } else return null;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
}
