package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ecommerceapp.models.Cart;
import com.example.ecommerceapp.models.Store;
import com.example.ecommerceapp.models.UserCart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import androidx.annotation.NonNull;

public class CustomerStoreView2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter2 mainAdapter2;
    String storeName;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_view2);
        storeName = getIntent().getStringExtra("storeName");
        UserCart.setupStoreCart(storeName);
        recyclerView = (RecyclerView)findViewById(R.id.Rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton checkout= (FloatingActionButton) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CustomerStoreView2.this, ShoppingCart.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        
        // Set up action bar with back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(storeName != null ? storeName : "Store");
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        // Find store by storeName (stores are keyed by owner ID, so we need to query by storeName field)
        loadStoreAndProducts();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mainAdapter2 != null) {
            mainAdapter2.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter2 != null) {
            mainAdapter2.stopListening();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        MenuItem item = menu.findItem(R.id.search);
        
        // Use androidx SearchView instead of android.widget.SearchView
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        
        if (searchView != null) {
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    text(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    text(newText);
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private String storeOwnerID; // Store the owner ID once we find the store
    
    private void loadStoreAndProducts() {
        // Find store by storeName field (stores are keyed by owner ID)
        FirebaseDatabase.getInstance().getReference("stores")
                .orderByChild("storeName")
                .equalTo(storeName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Get the first matching store (should be unique)
                            for (DataSnapshot storeSnapshot : snapshot.getChildren()) {
                                storeOwnerID = storeSnapshot.getKey(); // This is the owner ID (the key)
                                Store store = storeSnapshot.getValue(Store.class);
                                
                                if (store != null) {
                                    // Update toolbar title with actual store name
                                    if (getSupportActionBar() != null) {
                                        getSupportActionBar().setTitle(store.storeName != null ? store.storeName : "Store");
                                    }
                                    
                                    // Load products using the owner ID as the key
                                    loadProducts(storeOwnerID);
                                    return;
                                }
                            }
                        }
                        // Store not found
                        Log.e("CustomerStoreView2", "Store not found: " + storeName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CustomerStoreView2", "Database error: " + error.getMessage());
                    }
                });
    }
    
    private void loadProducts(String ownerID) {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(ownerID).child("products"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options, storeName);
        recyclerView.setAdapter(mainAdapter2);
        mainAdapter2.startListening();
    }
    
    private void text(String str){
        if (storeOwnerID == null) {
            // Store not loaded yet, can't search
            return;
        }
        
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(storeOwnerID).child("products").orderByChild("product").startAt(str).endAt(str+"~"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options, storeName);
        mainAdapter2.startListening();
        recyclerView.setAdapter(mainAdapter2);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
