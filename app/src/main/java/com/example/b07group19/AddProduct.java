package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    private EditText ProductName, Price, Brand, Quantity, Length, Width, Height, url;

    private Button button;

    private Model model;

    private FirebaseAuth mAuth;

    DatabaseReference ref;


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
        model = Model.getInstance();


        ref = FirebaseDatabase.getInstance().getReference().child("Products");
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button4) {
            add();
            Toast.makeText(AddProduct.this, "Added Product", Toast.LENGTH_LONG).show();

        }
    }

    private void add() {
        String productName = ProductName.getText().toString().trim();
        double price = Double.parseDouble(Price.getText().toString().trim());
        String brand = Brand.getText().toString().trim();
        int quantity = Integer.parseInt(Quantity.getText().toString().trim());
        String length = Length.getText().toString().trim();
        String width = Width.getText().toString().trim();
        String height = Height.getText().toString().trim();
        String ei = url.getText().toString().trim();


        if (productName.isEmpty()) {
            ProductName.setError("Name is required!");
            ProductName.requestFocus();
            return;
        }

        if (price <= 0) {
            Price.setError("Price must not be negative");
            Price.requestFocus();
            return;
        }


        Product product = new Product(productName, price, brand, quantity, length, width,  height, ei);
        ref.setValue(product);

    }


}