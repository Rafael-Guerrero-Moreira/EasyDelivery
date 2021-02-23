package com.example.easydelivery.menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuOptions {
    private static JSONObject PRODUCTS;
    private static JSONObject ORDERS;
    private static JSONObject REPORTS;
    private static JSONObject BUSINESS;

    static {
        try {
            PRODUCTS = new JSONObject()
                    .put("title", "Mis Productos")
                    .put("image", "a.jpg")
                    .put("activity", "a");
            ORDERS = new JSONObject()
                    .put("title", "Pedidos");
            REPORTS = new JSONObject()
                    .put("title", "Reportes");
            BUSINESS = new JSONObject()
                    .put("title", "Negocios");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getOptions(String userType) {
        JSONArray result = new JSONArray();
        switch (userType) {
            case "Bussines":
                return result.put(PRODUCTS).put(ORDERS).put(REPORTS);
            case "Users":
                return result.put(BUSINESS);
            case "Delivery":
                    return result;
        }
        return result;
    }
}