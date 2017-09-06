package ua.cn.sandi.jsonview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mikni on 03.09.2017.
 */

public class Order {
    private String id;
    private String name;
    private String phone;
    private String imageUrl;

    public String getName() {   return this.name;}
    public String getPhone() {  return this.phone;}
    public String getImageUrl() {   return this.imageUrl;}

    /////////////

    public static Order fromJson(JSONObject jsonObject) {
        Order o = new Order();
        // Deserialize json into object fields
        try {
            o.id = jsonObject.getString("id");
            o.name = jsonObject.getString("name");
            o.phone = jsonObject.getString("display_phone");
            o.imageUrl = jsonObject.getString("image_url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return o;
    }

    public static ArrayList<Order> fromJson(JSONArray jsonArray) {
        JSONObject orderJson;
        ArrayList<Order> orders = new ArrayList<Order>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                orderJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Order order = Order.fromJson(orderJson);
            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }



}
