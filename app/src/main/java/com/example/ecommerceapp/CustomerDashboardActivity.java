package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.util.Log;

import com.example.ecommerceapp.models.Cart;
import com.example.ecommerceapp.models.Store;
import com.example.ecommerceapp.models.UserCart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {
    private String currentUserID;
    private String username;
    private List<String> storeNames;
    private List<String> filteredStoreNames;
    private ListView listViewStores;
    private EditText searchStores;
    private ProgressBar loadingProgress;
    private LinearLayout emptyStoresState;
    private Button PreviousOrder;
    private Button Logout;
    private Model model;
    private Button ViewOrder;
    FloatingActionButton fabCart;
    private ValueEventListener storesListener; // For memory leak prevention

    private String storeName;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        
        // Initialize views
        listViewStores = findViewById(R.id.listViewStores);
        searchStores = findViewById(R.id.search_stores);
        loadingProgress = findViewById(R.id.loading_progress);
        emptyStoresState = findViewById(R.id.empty_stores_state);
        PreviousOrder = findViewById(R.id.btnViewOrders);
        fabCart = findViewById(R.id.cart_fab);
        Logout = findViewById(R.id.btnLogout);
        ViewOrder = findViewById(R.id.buttonViewOrders);
        
        // Initialize data
        username = FirebaseAuth.getInstance().getUid();
        if (username == null) {
            Log.e("CustomerDashboardActivity", "User ID is null, redirecting to login");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        currentUserID = username;
        storeNames = new ArrayList<>();
        filteredStoreNames = new ArrayList<>();
        Cart cart = UserCart.getCart(username);
        
        // Setup search functionality
        setupSearch();
        
        // Add entrance animations
        startCustomerDashboardAnimations();
        
        model = Model.getInstance();




        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(CustomerDashboardActivity.this, ShoppingCart.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(CustomerDashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        PreviousOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("currentUserID", currentUserID);
                intent.setClass(CustomerDashboardActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                storeName = storeNames.get(i);
                Intent intent = new Intent(CustomerDashboardActivity.this, CustomerStoreView2.class);
                intent.putExtra("storeName", storeName);
                intent.putExtra("username", username);
                startActivity(intent);
            }

        });

        getStoreNames();

    }
    private void getStoreNames() {
        model.getStoreNames((List<String> storeNames) -> {
            this.storeNames = storeNames;
            this.filteredStoreNames = new ArrayList<>(storeNames);
            
            // Hide loading, show results
            loadingProgress.setVisibility(View.GONE);
            updateStoreList();
        });
    }
    
    private void setupSearch() {
        searchStores.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStores(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void filterStores(String query) {
        filteredStoreNames.clear();
        if (query.isEmpty()) {
            filteredStoreNames.addAll(storeNames);
        } else {
            for (String storeName : storeNames) {
                if (storeName.toLowerCase().contains(query.toLowerCase())) {
                    filteredStoreNames.add(storeName);
                }
            }
        }
        updateStoreList();
    }
    
    private void updateStoreList() {
        if (filteredStoreNames.isEmpty()) {
            listViewStores.setVisibility(View.GONE);
            emptyStoresState.setVisibility(View.VISIBLE);
        } else {
            listViewStores.setVisibility(View.VISIBLE);
            emptyStoresState.setVisibility(View.GONE);
            ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredStoreNames);
            listViewStores.setAdapter(storeAdapter);
        }
    }
    
    public void getStores() {
        storesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeNames = new ArrayList<String>();
                // List<Store> stores = new ArrayList<Store>();
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {
                    Store store = userSnapShot.getValue(Store.class);
                    if (store != null && store.storeName != null) {
                        storeNames.add(store.storeName);
                    }
                }
                // callback.accept(storeNames);

                // Update filtered list and UI
                filteredStoreNames.clear();
                filteredStoreNames.addAll(storeNames);
                ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(CustomerDashboardActivity.this, android.R.layout.simple_list_item_1, filteredStoreNames);
                listViewStores.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CustomerDashboardActivity", "Database error loading stores: " + error.getMessage());
            }
        };
        
        FirebaseDatabase.getInstance().getReference("stores").addValueEventListener(storesListener);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MEMORY LEAK FIX: Remove Firebase listeners
        if (storesListener != null) {
            FirebaseDatabase.getInstance().getReference("stores").removeEventListener(storesListener);
        }
    }
    
    private void startCustomerDashboardAnimations() {
        // Get views
        View welcomeCard = findViewById(R.id.welcome_card);
        View viewOrdersBtn = findViewById(R.id.btnViewOrders);
        View logoutBtn = findViewById(R.id.btnLogout);
        View storesCard = findViewById(R.id.stores_card);
        View fabCart = findViewById(R.id.cart_fab);
        
        // Set initial states
        welcomeCard.setAlpha(0f);
        welcomeCard.setTranslationY(-50f);
        
        viewOrdersBtn.setAlpha(0f);
        viewOrdersBtn.setScaleX(0.8f);
        viewOrdersBtn.setScaleY(0.8f);
        
        logoutBtn.setAlpha(0f);
        logoutBtn.setScaleX(0.8f);
        logoutBtn.setScaleY(0.8f);
        
        storesCard.setAlpha(0f);
        storesCard.setTranslationY(50f);
        
        fabCart.setAlpha(0f);
        fabCart.setScaleX(0f);
        fabCart.setScaleY(0f);
        
        // Animate welcome card
        welcomeCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(200)
            .start();
        
        // Animate action buttons
        viewOrdersBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(400)
            .start();
        
        logoutBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(500)
            .start();
        
        // Animate stores card
        storesCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(600)
            .start();
        
        // Animate FAB with bounce
        fabCart.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setStartDelay(700)
            .start();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
