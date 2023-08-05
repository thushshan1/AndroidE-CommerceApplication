package com.example.b07group19;

import android.util.Log;

import com.example.b07group19.models.UserModel;

import java.util.function.Consumer;

public class Presenter {
    private Model model;
    private MainActivity view;

    public Presenter(Model model, MainActivity view) {
        this.model = model;
        this.view = view;
    }

    public void login(String email, String password){
        model.authenticate(email, password, (String type) ->{
            if (type == null) view.failedToLogin();
            else if(type.equals("user")) view.redirectToCustomerDashboard();
            else view.redirectToStoreDashboard();
        });
    }
}
