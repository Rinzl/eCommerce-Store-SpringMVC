package com.thd.ecommercespringmvc.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HoangTu on 19/10/2018.
 */

public class Category implements Serializable {
    private int id;
    private String name;
    private String description;
    private boolean activeStatus;
    private Date createDate;

    public Category() {
    }

    public Category(int id, String name, String description , boolean activeStatus, Date createDate) {
        this.id = id;
        this.name = name;
        this.description=description;
        this.activeStatus = activeStatus;
        this.createDate = createDate;
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
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public boolean isValid() {
        return !this.name.isEmpty() && !this.description.isEmpty();
    }
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", activeStatus=" + activeStatus +
                ", createDate=" + createDate +
                '}';
    }
}
