package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.ecommerceapp.models.Order;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import androidx.annotation.NonNull;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerOrders;
    private LinearLayout emptyOrdersState;
    private Chip chipAll, chipPending, chipCompleted, chipCancelled;
    private OrderAdapter orderAdapter;
    private List<Order> allOrders;
    private List<Order> filteredOrders;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initializeViews();
        setupChips();
        setupRecyclerView();
        setupBackButton();
        loadOrders();
    }
    
    private void setupBackButton() {
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

    private void initializeViews() {
        recyclerOrders = findViewById(R.id.recycler_orders);
        emptyOrdersState = findViewById(R.id.empty_orders_state);
        chipAll = findViewById(R.id.chip_all);
        chipPending = findViewById(R.id.chip_pending);
        chipCompleted = findViewById(R.id.chip_completed);
        chipCancelled = findViewById(R.id.chip_cancelled);

        // Set click listeners
        chipAll.setOnClickListener(this);
        chipPending.setOnClickListener(this);
        chipCompleted.setOnClickListener(this);
        chipCancelled.setOnClickListener(this);
    }

    private void setupChips() {
        // Set initial state
        chipAll.setChecked(true);
    }

    private void setupRecyclerView() {
        allOrders = new ArrayList<>();
        filteredOrders = new ArrayList<>();
        orderAdapter = new OrderAdapter(filteredOrders, this);
        
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        // Load orders from Firebase
        String currentUserID = getIntent().getStringExtra("currentUserID");
        if (currentUserID != null && !currentUserID.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("orders")
                    .orderByChild("userID")
                    .equalTo(currentUserID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allOrders.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                    Order order = orderSnapshot.getValue(Order.class);
                                    if (order != null) {
                                        order.setOrderID(orderSnapshot.getKey());
                                        allOrders.add(order);
                                    }
                                }
                            }
                            // No orders found - show empty state
                            filterOrders(currentFilter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("OrderHistoryActivity", "Failed to load orders: " + error.getMessage());
                            filterOrders(currentFilter);
                        }
                    });
        } else {
            // No user ID from intent - try to get from Firebase Auth
            String uid = FirebaseAuth.getInstance().getUid();
            if (uid != null) {
                // Retry with current user ID
                FirebaseDatabase.getInstance().getReference("orders")
                        .orderByChild("userID")
                        .equalTo(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                allOrders.clear();
                                if (snapshot.exists()) {
                                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                        Order order = orderSnapshot.getValue(Order.class);
                                        if (order != null) {
                                            order.setOrderID(orderSnapshot.getKey());
                                            allOrders.add(order);
                                        }
                                    }
                                }
                                filterOrders(currentFilter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("OrderHistoryActivity", "Failed to load orders: " + error.getMessage());
                                filterOrders(currentFilter);
                            }
                        });
            } else {
                filterOrders(currentFilter);
            }
        }
    }

    private void filterOrders(String filter) {
        filteredOrders.clear();
        
        if (filter.equals("all")) {
            filteredOrders.addAll(allOrders);
        } else {
            for (Order order : allOrders) {
                if (order.getStatus().equals(filter)) {
                    filteredOrders.add(order);
                }
            }
        }
        
        orderAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredOrders.isEmpty()) {
            recyclerOrders.setVisibility(View.GONE);
            emptyOrdersState.setVisibility(View.VISIBLE);
        } else {
            recyclerOrders.setVisibility(View.VISIBLE);
            emptyOrdersState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        
        // Reset all chips
        chipAll.setChecked(false);
        chipPending.setChecked(false);
        chipCompleted.setChecked(false);
        chipCancelled.setChecked(false);
        
        if (viewId == R.id.chip_all) {
            chipAll.setChecked(true);
            currentFilter = "all";
        } else if (viewId == R.id.chip_pending) {
            chipPending.setChecked(true);
            currentFilter = "pending";
        } else if (viewId == R.id.chip_completed) {
            chipCompleted.setChecked(true);
            currentFilter = "completed";
        } else if (viewId == R.id.chip_cancelled) {
            chipCancelled.setChecked(true);
            currentFilter = "cancelled";
        }
        
        filterOrders(currentFilter);
    }

    public void onOrderClick(Order order) {
        // Navigate to order details
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order_id", order.getOrderId());
        startActivity(intent);
    }

    public void onTrackOrderClick(Order order) {
        // Implement order tracking
        if (order != null && order.getOrderId() != null) {
            // In a real app, this would open a tracking page or external tracking service
            String status = order.status != null ? order.status : "Unknown";
            String trackingInfo = "Order #" + order.getOrderId() + 
                                "\nStatus: " + status + 
                                "\nExpected delivery: 2-3 business days" +
                                "\nTrack at: track.ecommerceapp.com";
            Toast.makeText(this, trackingInfo, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Order information not available for tracking", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Smooth fade transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
