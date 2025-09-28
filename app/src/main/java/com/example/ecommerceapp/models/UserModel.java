package com.example.ecommerceapp.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String email;
    public String name;
    public String age;
    public String userID;

    public String type; // owner, customer

    public UserModel(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserModel (String email, String name, String age) {
        this.email = email;
        this.name = name;
        this.age = age;
    }

}

