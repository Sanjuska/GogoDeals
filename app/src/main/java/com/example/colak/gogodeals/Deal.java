package com.example.colak.gogodeals;

import android.widget.ImageView;

/**
 * Created by colak on 2016-10-27.
 */

public class Deal {


    /**
     * Created by colak on 06/10/16.
     */
    // Variables used in the class


    public String company;
    public String price;
    public ImageView picture;
    public String description;
    public String duration;
    public String dealID;
    public String verificationID;


    public Deal(){
        this.description = "Grabbed deals";
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
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

    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public Deal(String company, String duration, String price, ImageView picture, String description, String dealID) {
        this.company = company;
        this.duration = duration;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.dealID = dealID;
    }
    private static final String TAG = "Deal";


    public String toString(){
        return description;
    }

    public boolean equals(Object o) {
        if(o instanceof Deal) {
            Deal d = (Deal) o;
            return dealID.equals(d.dealID);
        }
        return false;
    }

<<<<<<< HEAD
}