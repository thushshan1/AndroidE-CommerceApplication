package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StoreDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonManageProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_dashboard);

        buttonManageProducts = (Button) findViewById(R.id.buttonManageProducts);
        buttonManageProducts.setOnClickListener(this);
    }

    //@Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.buttonManageProducts) {
            startActivity(new Intent(this, ManageProducts.class));
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}