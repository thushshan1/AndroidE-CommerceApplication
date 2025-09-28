package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ecommerceapp.models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    private String storeName;

    private String the2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Get storeName from Intent
        storeName = getIntent().getStringExtra("storeName");
        currentUserID = getIntent().getStringExtra("currentUserID");

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

        storeName = getIntent().getStringExtra("storeName");
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
        // INPUT VALIDATION: Sanitize and validate all inputs
        String productName = ProductName.getText().toString().trim();
        String price = Price.getText().toString().trim();
        String brand = Brand.getText().toString().trim();
        String height = Height.getText().toString().trim();
        String length = Length.getText().toString().trim();
        String width = Width.getText().toString().trim();
        String quantity = Quantity.getText().toString().trim();
        String imageUrl = url.getText().toString().trim();
        
        // Validate required fields
        if (productName.isEmpty()) {
            ProductName.setError("Product name is required!");
            ProductName.requestFocus();
            return;
        }
        
        if (price.isEmpty()) {
            Price.setError("Price is required!");
            Price.requestFocus();
            return;
        }
        
        // Validate price is a valid number
        try {
            double priceValue = Double.parseDouble(price);
            if (priceValue <= 0) {
                Price.setError("Price must be greater than 0!");
                Price.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Price.setError("Please enter a valid price!");
            Price.requestFocus();
            return;
        }
        
        if (brand.isEmpty()) {
            Brand.setError("Brand is required!");
            Brand.requestFocus();
            return;
        }
        
        // Validate quantity is a valid number
        if (!quantity.isEmpty()) {
            try {
                int quantityValue = Integer.parseInt(quantity);
                if (quantityValue < 0) {
                    Quantity.setError("Quantity cannot be negative!");
                    Quantity.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Quantity.setError("Please enter a valid quantity!");
                Quantity.requestFocus();
                return;
            }
        }
        
        Products product = new Products();
        product.product = productName;
        product.price = price;
        product.brand = brand;
        product.height = height;
        product.length = length;
        product.width = width;
        product.quantity = quantity;
        product.turl = imageUrl;

        the2 = ProductName.getText().toString();



        FirebaseDatabase.getInstance().getReference("stores").child(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store store = snapshot.getValue(Store.class);
                if (store == null) {
                    Toast.makeText(AddProduct.this, "Store not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (store.products == null) {
                    store.products = new ArrayList<>();
                }
                store.products.add(product);

                // save the updated store back to firebase
                FirebaseDatabase.getInstance().getReference("stores").child(storeName).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddProduct.this, task.isSuccessful() ? "added" : "failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
