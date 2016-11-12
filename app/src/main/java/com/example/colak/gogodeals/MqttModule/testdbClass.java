package com.example.colak.gogodeals.MqttModule;

/**
 * Created by Nikos on 05/11/2016.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testdbClass {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";

    public static String name2;

    //db access
    static final String USER = "root";
    static final String PASS = "DIT029XES";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            //register driver and connect to db
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "select * from new_table";
            name2 = "user";

            System.out.println("test");
            ResultSet rs = stmt.executeQuery(sql);
            //stmt.executeUpdate(sql);

            //data from result set
            while(rs.next()){
                //Retrieve by column name
                String username  = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                //String last = rs.getString("last");

                //Display values
                System.out.print("Username: " + username + ", Password: " + password +", Email: " + email+'\n');
                //System.out.print("Password: " + password + '\n');
                //System.out.print("Email: " + email+ '\n');
                //System.out.println(", Last: " + last);
            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("disconnect");
    }
}