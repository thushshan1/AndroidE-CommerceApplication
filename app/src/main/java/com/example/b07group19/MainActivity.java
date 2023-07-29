package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClickUserRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), ShopperSignUp.class);
        startActivity(intent);
    }

    public void onClickOwnerSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), OwnerSignUp.class);
        startActivity(intent);
    }

    public void onClickOwnerLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("userType", "owners");
        startActivity(intent);
    }

    public void onClickShopperLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("userType", "shoppers");
        startActivity(intent);
    }
    public void onClickCreateStore(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateStoreActivity.class);
        startActivity(intent);
    }


}