package com.example.ecommerceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.models.Store;
import com.google.firebase.auth.FirebaseAuth;


public class StoreDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonManageProducts;
    private Button buttonViewOrder;
    private TextView storeText;
    private String currentUserID;
    private Model model;
    private Store store;

    private String thename;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_dashboard);

        // Initialize views
        buttonManageProducts = findViewById(R.id.buttonManageProducts);
        buttonViewOrder = findViewById(R.id.buttonViewOrders);
        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        storeText = findViewById(R.id.textViewWelcome);

        // Set click listeners
        buttonManageProducts.setOnClickListener(this);
        buttonViewOrder.setOnClickListener(this);
        buttonAddProduct.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        // Get current user
        currentUserID = FirebaseAuth.getInstance().getUid();
        if (currentUserID == null) {
            Log.e("StoreDashboardActivity", "User ID is null, redirecting to login");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        model = Model.getInstance();

        getStore();
        
        // Add entrance animations
        startDashboardAnimations();
    }

    @Override
    public void onClick(View view) {
        // Add button click animation
        animateButtonClick(view);
        
        int viewId = view.getId();

        if (viewId == R.id.buttonManageProducts) {
            Intent intent = new Intent(this, ManageProducts.class);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        } else if (viewId == R.id.buttonViewOrders) {
            Intent intent = new Intent(this, StoreOrdersActivity.class);
            intent.putExtra("currentUserID", currentUserID);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        } else if (viewId == R.id.buttonAddProduct) {
            Intent intent = new Intent(this, AddProduct.class);
            intent.putExtra("currentUserID", currentUserID);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        } else if (viewId == R.id.buttonLogout) {
            // Logout functionality
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }




    }
    private void getStore() {
        model.getStoreByOwner(currentUserID, (Store store) -> {
            if (store == null) {
                Intent intent = new Intent(this, CreateStoreActivity.class);
                intent.putExtra("currentUserID", currentUserID);
                startActivity(intent);
                return;
            }
            this.store = store;
            storeName = store.storeName;
            
            // Update welcome text with store name
            if (storeText != null) {
                storeText.setText("Welcome to " + storeName);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    
    private void startDashboardAnimations() {
        // Get views
        View welcomeCard = findViewById(R.id.welcome_card);
        View manageProductsBtn = findViewById(R.id.buttonManageProducts);
        View viewOrdersBtn = findViewById(R.id.buttonViewOrders);
        View addProductBtn = findViewById(R.id.buttonAddProduct);
        View logoutBtn = findViewById(R.id.buttonLogout);
        View statsCard = findViewById(R.id.stats_card);
        
        // Set initial states
        welcomeCard.setAlpha(0f);
        welcomeCard.setTranslationY(-50f);
        
        manageProductsBtn.setAlpha(0f);
        manageProductsBtn.setScaleX(0.8f);
        manageProductsBtn.setScaleY(0.8f);
        
        viewOrdersBtn.setAlpha(0f);
        viewOrdersBtn.setScaleX(0.8f);
        viewOrdersBtn.setScaleY(0.8f);
        
        addProductBtn.setAlpha(0f);
        addProductBtn.setScaleX(0.8f);
        addProductBtn.setScaleY(0.8f);
        
        logoutBtn.setAlpha(0f);
        logoutBtn.setScaleX(0.8f);
        logoutBtn.setScaleY(0.8f);
        
        statsCard.setAlpha(0f);
        statsCard.setTranslationY(50f);
        
        // Animate welcome card
        welcomeCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(200)
            .start();
        
        // Animate buttons with staggered timing
        manageProductsBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(400)
            .start();
        
        viewOrdersBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(500)
            .start();
        
        addProductBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(600)
            .start();
        
        logoutBtn.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(700)
            .start();
        
        // Animate stats card
        statsCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(800)
            .start();
    }
    
    private void animateButtonClick(View view) {
        // Scale down animation
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction(() -> {
                // Scale back up
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start();
            })
            .start();
    }

}
