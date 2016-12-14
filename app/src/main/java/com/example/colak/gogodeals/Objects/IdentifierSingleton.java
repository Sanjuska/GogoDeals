package com.example.colak.gogodeals.Objects;

import java.util.UUID;

/**
 * Created by mattias on 2016-11-29.
 */

public class IdentifierSingleton {
    private static IdentifierSingleton instance = null;


    public static final UUID SESSION_ID = UUID.randomUUID();
    public static UUID USER_ID;
    public static User USER;

    /**
     * Get singleton instance
     * @return
     */
    public static IdentifierSingleton getInstance() {
        if (instance == null) {
            instance = new IdentifierSingleton();
        }
        return instance;
    }


    /**
     * Private constructor
     */
    private IdentifierSingleton() {
        USER_ID = null;
        USER = null;
    }

    /**
     * Set the user id
     * @param user
     */
    public static void set(String user) {
        IdentifierSingleton.USER_ID = UUID.fromString(user);
    }

    /**
     * Set the user
     * @param user
     */
    public static void set(User user) { USER = user; }
}
