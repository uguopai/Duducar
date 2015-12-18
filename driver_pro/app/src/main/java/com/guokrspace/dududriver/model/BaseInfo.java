package com.guokrspace.dududriver.model;

import com.guokrspace.dududriver.common.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daddyfang on 15/11/4.
 */
public class BaseInfo {

    public Map<String, Object> getBaseInfo(){
        Map<String, Object> info = new HashMap<String, Object>();

        info.put(Constants.PREFERENCE_KEY_DRIVER_NAME, driver.getName() == null ? "" : driver.getName());
        info.put(Constants.PREFERENCE_KEY_DRIVER_AVATAR, driver.getAvatar() == null ? "" : driver.getAvatar() );
        info.put(Constants.PREFERENCE_KEY_DRIVER_PLATE, driver.getPlate() == null ? "0000" : driver.getPlate());
        info.put(Constants.PREFERNECE_KEY_DRIVER_RATING, driver.getRating() == null ? "0.0" : driver.getRating());
        info.put(Constants.PREFERENCE_KEY_DRIVER_TOTAL_ORDER, driver.getTotal_order() == null ? "0" : driver.getTotal_order());
        info.put(Constants.PREFERENCE_KEY_DRIVER_BALANCE, driver.getBalance() == null ? "0.0" : driver.getBalance());
        info.put(Constants.PREFERENCE_KEY_DRIVER_FAVORABLE_RATE, driver.getFavorable_rate() == null ? "0.0" : driver.getFavorable_rate());

        info.put(Constants.PREFERENCE_KEY_DRIVER_KM_PRICE, charge_rule.getKm_price() == null ? "5" : charge_rule.getKm_price());
        info.put(Constants.PREFERENCE_KEY_DRIVER_LOW_SPEED_PRICE, charge_rule.getLow_speed_price() == null ? "0.5" : charge_rule.getLow_speed_price());
        info.put(Constants.PREFERENCE_KEY_DRIVER_STARTING_DISTANCE, charge_rule.getStarting_distance() == null ? "10" : charge_rule.getStarting_distance());
        info.put(Constants.PREFERENCE_KEY_DRIVER_STARTING_PRICE, charge_rule.getStarting_price() == null ? "8" : charge_rule.getStarting_price());

        return info;
    }

    public BaseInfo(){


    }

    public Charge_rule getCharge_rule() {
        return charge_rule;
    }

    public void setCharge_rule(Charge_rule charge_rule) {
        this.charge_rule = charge_rule;
    }

    private Charge_rule charge_rule;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    private Driver driver;
    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    private String CMD;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public class Driver{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;
        private String plate;


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        private String avatar;

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getFavorable_rate() {
            return favorable_rate;
        }

        public void setFavorable_rate(String favorable_rate) {
            this.favorable_rate = favorable_rate;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTotal_order() {
            return total_order;
        }

        public void setTotal_order(String total_order) {
            this.total_order = total_order;
        }

        private String rating;
        private String total_order;
        private String balance;
        private String favorable_rate;
    }

    public class Charge_rule{
        public String getStarting_price() {
            return starting_price;
        }

        public void setStarting_price(String starting_price) {
            this.starting_price = starting_price;
        }

        public String getStarting_distance() {
            return starting_distance;
        }

        public void setStarting_distance(String starting_distance) {
            this.starting_distance = starting_distance;
        }

        public String getKm_price() {
            return km_price;
        }

        public void setKm_price(String km_price) {
            this.km_price = km_price;
        }

        public String getLow_speed_price() {
            return low_speed_price;
        }

        public void setLow_speed_price(String low_speed_price) {
            this.low_speed_price = low_speed_price;
        }

        private String starting_price;
        private String starting_distance;
        private String km_price;
        private String low_speed_price;
    }
}
