package com.example.easydelivery.model;

public class InfoBusiness {
   private String idBuissnes;
   private String phone;
   private String typecomerce;
   private String address;
   private String coordinates;
   private String urllogo;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getIdBuissnes() {
        return idBuissnes;
    }

    public void setIdBuissnes(String idBuissnes) {
        this.idBuissnes = idBuissnes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypecomerce() {
        return typecomerce;
    }

    public void setTypecomerce(String typecomerce) {
        this.typecomerce = typecomerce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrllogo() {
        return urllogo;
    }

    public void setUrllogo(String urllogo) {
        this.urllogo = urllogo;
    }
}
