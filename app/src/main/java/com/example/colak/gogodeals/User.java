package com.example.colak.gogodeals;

import com.example.colak.gogodeals.Objects.Deal;

import java.util.ArrayList;

/**
 * Created by mattias on 2016-12-01.
 */

public class User {
    private final String name;
    private final String email;
    private final String password;
    private ArrayList<Deal> grabbed;
    private ArrayList<Deal> grocode;


    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setGrabbed(ArrayList<Deal> grabbed) {
        this.grabbed = grabbed;
    }

    public ArrayList<Deal> getGrabbed() {
        return grabbed;
    }

    public ArrayList<Deal> getGrocode() {
        return grocode;
    }

    public void setGrocode(ArrayList<Deal> grocode) {
        this.grocode = grocode;
    }
}
