package com.example.easydelivery.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuOptions {
    private static JSONObject PRODUCTS;
    private static JSONObject ORDERS;
    private static JSONObject REPORTS;
    private static JSONObject BUSINESS;
    private static JSONObject DELIVER;

    static {
        try {
            PRODUCTS = new JSONObject()
                    .put("title", "Mis Productos")
                    .put("image", "https://cdn.icon-icons.com/icons2/1138/PNG/512/1486395306-11-packaging_80579.png")
                    .put("activity", "a");
            ORDERS = new JSONObject()
                    .put("title", "Pedidos")
                    .put("image", "https://cdn.icon-icons.com/icons2/943/PNG/512/shoppaymentorderbuy-20_icon-icons.com_73877.png")
                    .put("activity", "b");
            REPORTS = new JSONObject()
                    .put("title", "Reportes")
                    .put("image", "https://i.pinimg.com/originals/6c/d8/b7/6cd8b76dbb55d0108d23d7727df097e5.png")
                    .put("activity", "c");
            BUSINESS = new JSONObject()
                    .put("title", "Negocios")
                    .put("image", "https://img.icons8.com/dusk/344/small-business.png")
                    .put("activity", "d");
            DELIVER = new JSONObject()
                    .put("title", "Repartidores")
                    .put("image", "https://image.flaticon.com/icons/png/128/3731/3731784.png")
                    .put("activity", "e");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getOptions(String userType) {
        JSONArray result = new JSONArray();
        switch (userType) {
            case "Business":
                return result.put(PRODUCTS).put(ORDERS).put(REPORTS).put(BUSINESS).put(DELIVER);
            case "Client":
                return result.put(BUSINESS).put(PRODUCTS).put(DELIVER);
            case "Delivery":
                return result.put(ORDERS);
        }
        return result;
    }
}