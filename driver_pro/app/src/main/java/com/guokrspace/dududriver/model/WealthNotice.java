package com.guokrspace.dududriver.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hyman on 15/11/13.
 */
public class WealthNotice implements BaseNoticeItem {

    public String order_id;
    public String time;
    public String payer;
    public String price;
    public String start;
    public String mobile;
    public String destination;
    public String sumprice;
    public String start_time;
    public String end_time;
    public String low_speed_time;
    public String distance;
    public String otherfee;

    public WealthNotice(String message){
        try{
            JSONObject object = new JSONObject(message);
            order_id = String.valueOf((Integer) object.get("id"));
            time = (String) object.get("ordernum");
            payer = object.get("payer") == null ? "2" :  (String ) object.get("payer");
            price = (String) object.get("price");
            start = (String) object.get("start");
            destination = (String) object.get("destination");
            mobile = object.get("passenger_mobile") == null ? "13900000002" : (String) object.get("passenger_mobile");
            sumprice = object.get("sumprice") == null ? "0.01" : (String) object.get("sumprice");
            start_time = (String) object.get("start_time");
            end_time = (String) object.get("end_time");
            low_speed_time = (String) object.get("low_speed_time");
            distance = (String) object.get("distance");
            otherfee = (String) object.get("otherfee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
