package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
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
    private boolean isEditMode = false;
    private int editProductPosition = -1;
    
    private static final String STATE_PRODUCT_NAME = "product_name";
    private static final String STATE_PRICE = "price";
    private static final String STATE_BRAND = "brand";
    private static final String STATE_QUANTITY = "quantity";
    private static final String STATE_LENGTH = "length";
    private static final String STATE_WIDTH = "width";
    private static final String STATE_HEIGHT = "height";
    private static final String STATE_URL = "url";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Get storeName from Intent
        storeName = getIntent().getStringExtra("storeName");
        currentUserID = getIntent().getStringExtra("currentUserID");
        
        // If currentUserID is null, get it from Firebase Auth
        if (currentUserID == null) {
            currentUserID = FirebaseAuth.getInstance().getUid();
        }

        button = (Button) findViewById(R.id.AddProduct);
        button.setOnClickListener(this);
        ProductName = (EditText) findViewById(R.id.ProductName);
        Price = (EditText) findViewById(R.id.Price);
        Brand = (EditText) findViewById(R.id.Brand);
        Quantity = (EditText) findViewById(R.id.Quantity);
        Length = (EditText) findViewById(R.id.Length);
        Width = (EditText) findViewById(R.id.Width);
        Height = (EditText) findViewById(R.id.Height);
        url = (EditText) findViewById(R.id.ExtraInfo);
        
        // Check if we're in edit mode
        isEditMode = getIntent().getBooleanExtra("editMode", false);
        if (isEditMode) {
            // Populate fields with product data
            ProductName.setText(getIntent().getStringExtra("productName"));
            Price.setText(getIntent().getStringExtra("productPrice"));
            Brand.setText(getIntent().getStringExtra("productBrand"));
            Quantity.setText(getIntent().getStringExtra("productQuantity"));
            Length.setText(getIntent().getStringExtra("productLength"));
            Width.setText(getIntent().getStringExtra("productWidth"));
            Height.setText(getIntent().getStringExtra("productHeight"));
            url.setText(getIntent().getStringExtra("productImageUrl"));
            editProductPosition = getIntent().getIntExtra("productPosition", -1);
            button.setText("Update Product");
        } else {
            // Restore saved state if available (for new products)
            if (savedInstanceState != null) {
                ProductName.setText(savedInstanceState.getString(STATE_PRODUCT_NAME, ""));
                Price.setText(savedInstanceState.getString(STATE_PRICE, ""));
                Brand.setText(savedInstanceState.getString(STATE_BRAND, ""));
                Quantity.setText(savedInstanceState.getString(STATE_QUANTITY, ""));
                Length.setText(savedInstanceState.getString(STATE_LENGTH, ""));
                Width.setText(savedInstanceState.getString(STATE_WIDTH, ""));
                Height.setText(savedInstanceState.getString(STATE_HEIGHT, ""));
                url.setText(savedInstanceState.getString(STATE_URL, ""));
            }
        }
        
        // Set up back button with smooth transition
        View backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save form state
        outState.putString(STATE_PRODUCT_NAME, ProductName.getText().toString());
        outState.putString(STATE_PRICE, Price.getText().toString());
        outState.putString(STATE_BRAND, Brand.getText().toString());
        outState.putString(STATE_QUANTITY, Quantity.getText().toString());
        outState.putString(STATE_LENGTH, Length.getText().toString());
        outState.putString(STATE_WIDTH, Width.getText().toString());
        outState.putString(STATE_HEIGHT, Height.getText().toString());
        outState.putString(STATE_URL, url.getText().toString());
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation for back button
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    
    @Override
    public void finish() {
        super.finish();
        // Apply smooth fade transition when activity finishes
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.AddProduct) {
            insert();
            // Don't clear here - only clear on successful save
        }
    }

    private void insert(){
        // INPUT VALIDATION: Only Product Name, Price, and Quantity are required
        String productName = ProductName.getText().toString().trim();
        String price = Price.getText().toString().trim();
        String brand = Brand.getText().toString().trim();
        String height = Height.getText().toString().trim();
        String length = Length.getText().toString().trim();
        String width = Width.getText().toString().trim();
        String quantity = Quantity.getText().toString().trim();
        String imageUrl = url.getText().toString().trim();
        
        // Validate required fields only
        boolean hasError = false;
        
        if (productName.isEmpty()) {
            ProductName.setError("Product name is required!");
            ProductName.requestFocus();
            hasError = true;
        }
        
        if (price.isEmpty()) {
            Price.setError("Price is required!");
            if (!hasError) {
                Price.requestFocus();
            }
            hasError = true;
        } else {
            // Validate price is a valid number
            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) {
                    Price.setError("Price must be greater than 0!");
                    if (!hasError) {
                        Price.requestFocus();
                    }
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                Price.setError("Please enter a valid price!");
                if (!hasError) {
                    Price.requestFocus();
                }
                hasError = true;
            }
        }
        
        if (quantity.isEmpty()) {
            Quantity.setError("Quantity is required!");
            if (!hasError) {
                Quantity.requestFocus();
            }
            hasError = true;
        } else {
            // Validate quantity is a valid number
            try {
                int quantityValue = Integer.parseInt(quantity);
                if (quantityValue < 0) {
                    Quantity.setError("Quantity cannot be negative!");
                    if (!hasError) {
                        Quantity.requestFocus();
                    }
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                Quantity.setError("Please enter a valid quantity!");
                if (!hasError) {
                    Quantity.requestFocus();
                }
                hasError = true;
            }
        }
        
        // If there are validation errors, don't proceed (but keep the form data)
        if (hasError) {
            return;
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



        // Use currentUserID as the key (stores are now keyed by owner ID)
        FirebaseDatabase.getInstance().getReference("stores").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                
                if (isEditMode && editProductPosition >= 0 && editProductPosition < store.products.size()) {
                    // Update existing product
                    store.products.set(editProductPosition, product);
                } else {
                    // Add new product
                    store.products.add(product);
                }

                // save the updated store back to firebase
                FirebaseDatabase.getInstance().getReference("stores").child(currentUserID).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (isEditMode) {
                                Toast.makeText(AddProduct.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddProduct.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                            }
                            // Clear form only on success
                            clearAll();
                            // Go back with smooth slide animation
                            finish();
                        } else {
                            if (isEditMode) {
                                Toast.makeText(AddProduct.this, "Failed to update product. Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddProduct.this, "Failed to add product. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                            // Don't clear form on failure - user can try again
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProduct.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        // Clear errors
        ProductName.setError(null);
        Price.setError(null);
        Quantity.setError(null);
    }

}
