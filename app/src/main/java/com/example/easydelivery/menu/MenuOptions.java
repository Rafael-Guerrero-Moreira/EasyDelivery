package com.example.easydelivery.menu;

import com.example.easydelivery.generallist.ListBusinessforClient;
import com.example.easydelivery.generallist.ListOrders;
import com.example.easydelivery.generallist.MyCarListScreenActivity;
import com.example.easydelivery.model.MyCar;
import com.example.easydelivery.module.ShopActivity;
import com.example.easydelivery.views.activities.products.ProductsListScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuOptions {
    private static JSONObject PRODUCTS;
    private static JSONObject ORDERS;
    private static JSONObject REPORTS;
    private static JSONObject BUSINESS;
    private static JSONObject DELIVER;
    private static JSONObject MYCAR;

    static {
        try {
            PRODUCTS = new JSONObject()
                    .put("title", "Mis Productos")
                    .put("image", "https://img.icons8.com/dusk/344/product.png")
                    .put("activity", ProductsListScreenActivity.class);
            ORDERS = new JSONObject()
                    .put("title", "Pedidos")
                    .put("image", "https://img.icons8.com/dusk/344/purchase-order.png")
                    .put("activity", ListOrders.class);
            REPORTS = new JSONObject()
                    .put("title", "Reportes")
                    .put("image", "https://img.icons8.com/dusk/344/fine-print.png")
                    .put("activity", null);
            BUSINESS = new JSONObject()
                    .put("title", "Negocios")
                    .put("image", "https://img.icons8.com/dusk/344/small-business.png")
                    .put("activity", ListBusinessforClient.class);
            DELIVER = new JSONObject()
                    .put("title", "Repartidores")
                    .put("image", "https://img.icons8.com/dusk/344/paper-plane.png")
                    .put("activity", ShopActivity.class);
            MYCAR   = new JSONObject()
                    .put("title", "Mi Carrito")
                    .put("image", "https://img.icons8.com/dusk/344/paper-plane.png")
                    .put("activity", MyCarListScreenActivity.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getOptions(String userType) {
        JSONArray result = new JSONArray();
        switch (userType) {
            case "Business":
                return result.put(PRODUCTS).put(ORDERS).put(REPORTS);
            case "Client":
                return result.put(BUSINESS).put(MYCAR).put(ORDERS);
            case "Delivery":
                return result.put(ORDERS);
        }
        return result;
    }
}