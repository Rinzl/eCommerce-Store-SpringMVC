package com.thd.ecommercespringmvc.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class ProductFormUpload implements Serializable {
    private String name;
    private String quantity;
    private String category;
    private String description;
    private String descriptionDetail;
    private String image;
    private String price;

    private MultipartFile multipartFile;

    public ProductFormUpload() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductFormUpload{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", descriptionDetail='" + descriptionDetail + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", multipartFile=" + multipartFile +
                '}';
    }
}
