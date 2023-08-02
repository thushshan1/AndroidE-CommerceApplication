package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.b07group19.models.Store;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    private EditText ProductName, Price, Brand, Quantity, Length, Width, Height, url;

    private Button button;

    private Model model;

    private Products product;


    private FirebaseAuth mAuth;

    DatabaseReference ref;

    private String currentUserID;

    private Store store;

    private String the;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(this);
        ProductName = (EditText) findViewById(R.id.ProductName);
        Price = (EditText) findViewById(R.id.Price);
        Brand = (EditText) findViewById(R.id.Brand);
        Quantity = (EditText) findViewById(R.id.Quantity);
        Length = (EditText) findViewById(R.id.Length);
        Width = (EditText) findViewById(R.id.Width);
        Height = (EditText) findViewById(R.id.Height);
        url = (EditText) findViewById(R.id.ExtraInfo);
        currentUserID = getIntent().getStringExtra("currentUserID");
        model = Model.getInstance();




    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button4) {
            insert();
            clearAll();

        }
    }



    private void insert(){
        Map<String,Object> map = new HashMap<>();
        map.put("product", ProductName.getText().toString());
        map.put("price", Price.getText().toString());
        map.put("brand", Brand.getText().toString());
        map.put("height", Height.getText().toString());
        map.put("length", Length.getText().toString());
        map.put("width", Width.getText().toString());
        map.put("quantity", Quantity.getText().toString());
        map.put("turl", url.getText().toString());

        the = ProductName.getText().toString();


        FirebaseDatabase.getInstance().getReference().child("stores").child(StoreDashboardActivity.thename).child("Products").child(the)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddProduct.this, "Added Product", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddProduct.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearAll()
    {
        ProductName.setText("");
        Price.setText("");
        Brand.setText("");
        Length.setText("");
        Width.setText("");
        Quantity.setText("");
        url.setText("");
        Height.setText("");
    }





}