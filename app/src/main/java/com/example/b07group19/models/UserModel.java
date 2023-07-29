package com.example.b07group19.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String email;
    public String name;
    public String age;
    public String userID;

    public String type; // owner, customer

    public UserModel(){

    }


    public UserModel (String email, String name, String age) {
        this.email = email;
        this.name = name;
        this.age = age;
    }
}

