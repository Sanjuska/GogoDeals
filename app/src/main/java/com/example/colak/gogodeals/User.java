package com.example.colak.gogodeals;

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
}
