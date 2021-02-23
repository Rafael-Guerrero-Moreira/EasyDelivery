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
                    .put("image", "https://blog.gs1mexico.org/hubfs/Newsletter/2017/Septiembre%202017/Clasificacion%20de%20Productos%20SAT%20ONU%20UNSPSC%20GS1%20Mexico%20Syncfonia%20Factura%20Blog.png")
                    .put("activity", "a");
            ORDERS = new JSONObject()
                    .put("title", "Pedidos")
                    .put("image", "https://www.consumiblestpv.com/WebRoot/acens/Shops/consumiblestpv_com/547E/E231/7BE2/5F0B/8AC4/4DF0/7515/6434/carro-compra-supermercado.jpg")
                    .put("activity", "b");
            REPORTS = new JSONObject()
                    .put("title", "Reportes")
                    .put("image", "https://i.pinimg.com/originals/6c/d8/b7/6cd8b76dbb55d0108d23d7727df097e5.png")
                    .put("activity", "c");
            BUSINESS = new JSONObject()
                    .put("title", "Negocios")
                    .put("image", "https://i0.wp.com/feriainclusivaempresassb.com/images/feriasb/home.png")
                    .put("activity", "d");
            DELIVER = new JSONObject()
                    .put("title", "Repartidores")
                    .put("image", "https://image.freepik.com/vector-gratis/servicio-entrega-concepto-mascara_23-2148505104.jpg")
                    .put("activity", "e");
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
                return result.put(BUSINESS).put(PRODUCTS).put(DELIVER);
            case "Delivery":
                return result.put(ORDERS);
        }
        return result;
    }
}