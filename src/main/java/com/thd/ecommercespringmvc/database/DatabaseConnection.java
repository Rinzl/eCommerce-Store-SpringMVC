package com.thd.ecommercespringmvc.database;

import com.thd.ecommercespringmvc.StoreUtils.QueryUtils;
import com.thd.ecommercespringmvc.model.Product;
import com.thd.ecommercespringmvc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    public static final String DATABASE_NAME = "store";
    private static Logger logger = LogManager.getLogger(DatabaseConnection.class);

    private Connection connection;
    private PreparedStatement getUserInfoPS;
    private PreparedStatement getProductPS;
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
            getProductPS = connection.prepareStatement(QueryUtils.PRODUCT_PS);
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
        return products;
    }

    public List<Product> getFeaturedProduct() {
        List<Product> products = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(QueryUtils.PRODUCT_FEATURED_QUERY);
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDescription(rs.getString("description"));
                product.setDescriptionDetail(rs.getString("description_detail"));
                product.setImage(rs.getString("image"));
                product.setPrice(rs.getDouble("price"));
                product.setCreateDate(rs.getTimestamp("create_at"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }


}
