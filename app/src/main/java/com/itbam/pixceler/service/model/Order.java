package com.itbam.pixceler.service.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {
    public static Integer id;
    public static double total;
    public static boolean flag = false;

    public static void setOrder(String orderDto) throws JSONException {
        JSONObject dto = new JSONObject(orderDto);
        id = dto.getInt("id");
        total = dto.getDouble("total");
    }
}
