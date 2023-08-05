package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageProducts extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private Button button2;
    private Button button3;

    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        storeName = getIntent().getStringExtra("storeName");

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button) {
            Intent intent = new Intent(this, AddProduct.class);
            intent.putExtra("storeName", storeName);
            startActivity(intent);

        } else if (viewId == R.id.button2) {
            startActivity(new Intent(this, EditProduct.class));

        }
    }
}