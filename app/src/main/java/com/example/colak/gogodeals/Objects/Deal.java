package com.example.colak.gogodeals.Objects;

import android.widget.ImageView;

/**
 * Created by colak on 2016-11-20.
 */

/** Deal class is an class which creates an DEAL object which is extracted from the
 *   message which is retrived. Deal class creates variables which are necessery
 *   for getting sent infromation from the message.*/


public class Deal {

    // Variables used in the class
    public String company;
    public String price;
    public ImageView picture;
    public String stringPicture;
    public String description;
    public String duration;
    public String dealID;
    public String verificationID;


    //Getters and setters

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

    public String getStringPicture() {
        return stringPicture;
    }

    public void setStringPicture(String stringPicture) {
        this.stringPicture = stringPicture;
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

    // Method for getting verification ID from the retrieved message
    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    //Constructor used when creating an object of Deal which is used in list view of all grabbed deals.
    public Deal(String company, String duration, String price, ImageView picture, String description, String dealID) {
        this.company = company;
        this.duration = duration;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.dealID = dealID;
    }

    public Deal(String company, String duration, String price, String picture, String description, String dealID) {
        this.company = company;
        this.duration = duration;
        this.price = price;
        this.stringPicture = picture;
        this.description = description;
        this.dealID = dealID;
    }
    private static final String TAG = "Deal";


    public String toString(){
        return description;
    }

    // method for getting DealID which is used for connection user who grabbed a deal and the deal.
    public boolean equals(Object o) {
        if(o instanceof Deal) {
            Deal d = (Deal) o;
            return dealID.equals(d.dealID);
        }
        return false;
    }

}