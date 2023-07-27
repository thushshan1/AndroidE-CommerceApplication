package com.example.b07group19.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String username;
    public String password;

    public UserModel(){
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return username + "!!!!!";
    }

}
