package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.models.Cart;
import com.example.ecommerceapp.models.UserCart;

public class ShoppingCart extends AppCompatActivity {
    RecyclerView recyclerView;
    ShoppingCartAdaptor adaptor;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        recyclerView = (RecyclerView)findViewById(R.id.rv_store);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Set up toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        
        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUserID = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
                if (currentUserID == null) {
                    Toast.makeText(ShoppingCart.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Cart cart = UserCart.getCart(currentUserID);
                if (cart != null && cart.getOrderList() != null && !cart.getOrderList().isEmpty()) {
                    Model.getInstance().saveOrders(cart);
                    Toast.makeText(ShoppingCart.this, "Order created!", Toast.LENGTH_LONG).show();

                    // Clear cart of current user
                    UserCart.clearCart();
                    startActivity(new Intent(ShoppingCart.this, CustomerDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(ShoppingCart.this, "Cart is empty! Cannot checkout", Toast.LENGTH_LONG).show();
                }
            }
        });

//        back=(Button)findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ShoppingCart.this, CustomerStoreView2.class));
//            }
//        });
        // Get current user ID
        String currentUserID = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
        if (currentUserID == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize cart for current user
        Cart cart = UserCart.getCart(currentUserID);
        if (cart == null) {
            cart = new Cart();
            UserCart.getCart(currentUserID); // This will create and store the cart
        }
        
        UserCart.setOrderList();
        
        // Check if cart has items
        if (cart.getOrderList() == null || cart.getOrderList().isEmpty()) {
            // Show empty cart message
            findViewById(R.id.emptyCartCard).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyCartCard).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adaptor = new ShoppingCartAdaptor(cart.getOrderList());
            recyclerView.setAdapter(adaptor);
        }

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
