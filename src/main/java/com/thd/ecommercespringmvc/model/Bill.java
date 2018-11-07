package com.thd.ecommercespringmvc.model;

import com.thd.ecommercespringmvc.StoreUtils.OrderStatus;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by HoangTu on 23/10/2018.
 */
public class Bill implements Serializable {
    private int id;
    private int id_customer;
    private Date date_oder;
    private Double total;
    private String note;
    private int status;
    private Date createDate;
    private Customer customer;
    public Bill() {
    }

    public Bill(int id, int id_customer, Date date_oder, Double total, String note, int status, Date createDate) {
        this.id = id;
        this.id_customer = id_customer;
        this.date_oder = date_oder;
        this.total = total;
        this.note = note;
        this.status = status;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public Date getDate_oder() {
        return date_oder;
    }

    public void setDate_oder(Date date_oder) {
        this.date_oder = date_oder;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", id_customer=" + id_customer +
                ", date_oder=" + date_oder +
                ", total=" + total +
                ", note='" + note + '\'' +
                ", activityStatus=" + status +
                ", createDate=" + createDate +
                ", customer=" + customer +
                '}';
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getFormattedPrice() {
        return Product.nf.format(total).replace(" ","");
    }

    public String getStatusAsText() {

        switch (status) {
            case OrderStatus.PENDING :
                return "Chờ xác nhận";
            case OrderStatus.CANCEL :
                return "Đã hủy";
            case OrderStatus.APPROVE :
                return "Hoàn thành";
            default:
                return "Lỗi";
        }
    }
}
