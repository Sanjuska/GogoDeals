package com.example.colak.gogodeals.MqttModule;

/**
 * Created by colak on 2016-10-27.
 */

public class Deal {


    /**
     * Created by colak on 06/10/16.
     */
    // Variables used in the class


    public String company;
    public String productName;
    public int price;
    public String picture;
    public String description;
    public String duration;


    public Deal(){
        this.company = "Grabbed deals";
    }

    public Deal(String company, String duration, String productName, int price, String picture, String description) {
        this.company = company;
        this.duration = duration;
        this.productName = productName;
        this.price = price;
        this.picture = picture;
        this.description = description;
    }
    private static final String TAG = "Deal";

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String toString(){
        return company;
    }

}