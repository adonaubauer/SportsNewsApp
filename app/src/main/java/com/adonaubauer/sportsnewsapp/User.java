package com.adonaubauer.sportsnewsapp;

/**
 * Created by Austin on 5/4/2017.
 */

public class User {

    private int _id;
    private String _username;
    private String _password;

    User() {}

    User(int id, String username, String password) {

        _id = id;

        _username = username;

        _password = password;

    }

    void setID(int id) {

        _id = id;

    }

    int getID() {

        return _id;

    }

    void setUsername(String username) {

        _username = username;

    }

    String getUsername() {

        return _username;

    }

    void setPassword(String password) {

        _password = password;

    }

    String getPassword() {

        return _password;

    }

}
