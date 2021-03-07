package com.example.easydelivery.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDatail {
    private String idOrder;
   // private String idClient;
   // private String clientName;
   // private String idBusiness;
   // private String businessname;
    private String idProduct;
    private String productname;
   // private String idDelivery;
   // private String deliveryname;
    private String photoProduct;
    private String price;
    private String quantity;
    private String subTotal;
    //private String estado;
    //private String date;




    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }


    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getPhotoProduct() {
        return photoProduct;
    }

    public void setPhotoProduct(String photoProduct) {
        this.photoProduct = photoProduct;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



}
