package com.thd.ecommercespringmvc.model;

public class Customer {
    private int id;
    private int idUser;
    private String name;
    private String address;
    private String phone;
    private Integer defaultAddress;

    public Customer() {
        id = 0;
        idUser = 0;
        name ="";
        address = "";
        phone = "";
        defaultAddress = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Integer defaultAddress) {
        if(defaultAddress == null) {
            this.defaultAddress = 0;
        }
        else this.defaultAddress = defaultAddress;
    }
    public String getContent() {
        return "<br> "+ address + "<br>"+phone;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", defaultAddress=" + defaultAddress +
                '}';
    }
}
