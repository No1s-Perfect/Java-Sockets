package org.una.server.model;

import org.json.JSONObject;

import java.sql.SQLException;

public class UserModel {
    private static UserModel instance = null;



    private UserModel() {
       
    }

    public static UserModel getInstance() {
        if (instance == null) instance = new UserModel();
        return instance;
    }

    public String getAuthorization(String username, String password) throws SQLException {
        return "asdasd";
    }

    public void register(String username, String password, String name, String lastname, String email,
                         String address, String workphone, String mobilephone) throws SQLException {
       
    }

    public void update(String username, String name, String lastname, String email, String address,
                       String workphone, String mobilephone) throws SQLException {
        
    }

    public JSONObject get(String username) throws SQLException {
        
        return new JSONObject();
    }
}
