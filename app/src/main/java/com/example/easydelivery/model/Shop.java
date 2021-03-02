package com.example.easydelivery.model;

public class Shop {
    private String idShop;
    private String idClient;
    private String idBusiness;
    private String idDeliver;
    private String subTotal;
    private String priceDeliver;

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdBusiness() {
        return idBusiness;
    }

    public void setIdBusiness(String idBusiness) {
        this.idBusiness = idBusiness;
    }

    public String getIdDeliver() {
        return idDeliver;
    }

    public void setIdDeliver(String idDeliver) {
        this.idDeliver = idDeliver;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getPriceDeliver() {
        return priceDeliver;
    }

    public void setPriceDeliver(String priceDeliver) {
        this.priceDeliver = priceDeliver;
    }
}
