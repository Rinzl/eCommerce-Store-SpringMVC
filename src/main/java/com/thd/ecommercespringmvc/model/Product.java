package com.thd.ecommercespringmvc.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class Product implements Serializable {

    public static final String PRODUCT_ROOT_IMAGE = "C:\\Users\\tranh\\IdeaProjects\\ecommerce-spring-mvc\\public\\images\\";
    public static final String PRODUCT_RELATIVE_IMG_PATH = "/public_img/images/";
    public static NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));

    private int id;
    private String name;
    private int idCategory;
    private int quantity;
    private String category;
    private String description;
    private String descriptionDetail;
    private String image;
    private double price;
    private Date createDate;
    private boolean activeStatus;
    private int quantityInCart;

    public Product() {
    }

    public static String formatPrice(double price) {
        return nf.format(price).replace(" ","");
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        this.descriptionDetail = descriptionDetail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getFormattedPrice() {
        return nf.format(price).replace(" ","");
    }

    public String getFormattedTotalPrice() {
        return nf.format(price * quantityInCart).replace(" ","");
    }

    public double getTotalPrice() {
        return price * quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }
    public int productPriceToString() {
        return (int) price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idCategory=" + idCategory +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", descriptionDetail='" + descriptionDetail + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", createDate=" + createDate +
                ", activeStatus=" + activeStatus +
                ", quantityInCart=" + quantityInCart +
                '}';
    }
}
