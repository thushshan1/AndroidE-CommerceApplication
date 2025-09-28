package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.models.Order;
import com.example.ecommerceapp.models.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import androidx.annotation.NonNull;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView orderId, orderDate, storeName, orderTotal;
    private RecyclerView recyclerOrderItems;
    private OrderItemAdapter orderItemAdapter;
    private String currentOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        currentOrderId = getIntent().getStringExtra("order_id");
        
        initializeViews();
        setupRecyclerView();
        loadOrderDetails();
    }

    private void initializeViews() {
        orderId = findViewById(R.id.order_id);
        orderDate = findViewById(R.id.order_date);
        storeName = findViewById(R.id.store_name);
        orderTotal = findViewById(R.id.order_total);
        recyclerOrderItems = findViewById(R.id.recycler_order_items);

        // Set click listeners
        findViewById(R.id.btn_track_order).setOnClickListener(this);
        findViewById(R.id.btn_contact_support).setOnClickListener(this);
    }

    private void setupRecyclerView() {
        orderItemAdapter = new OrderItemAdapter(new ArrayList<>());
        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrderItems.setAdapter(orderItemAdapter);
    }

    private void loadOrderDetails() {
        // Load order details from Firebase
        if (currentOrderId != null && !currentOrderId.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("orders")
                    .child(currentOrderId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Order order = snapshot.getValue(Order.class);
                                if (order != null) {
                                    displayOrderDetails(order);
                                } else {
                                    createSampleOrderDetails(); // Fallback to sample data
                                }
                            } else {
                                createSampleOrderDetails(); // Fallback to sample data
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("OrderDetailsActivity", "Failed to load order: " + error.getMessage());
                            createSampleOrderDetails(); // Fallback to sample data
                        }
                    });
        } else {
            createSampleOrderDetails(); // Fallback to sample data
        }
    }
    
    private void displayOrderDetails(Order order) {
        orderId.setText("#" + currentOrderId);
        orderDate.setText(order.createDate != null ? order.createDate : "Unknown Date");
        storeName.setText(order.storeName != null ? order.storeName : "Unknown Store");
        
        // Calculate total from order items
        double total = 0.0;
        if (order.items != null) {
            for (OrderItem item : order.items) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        orderTotal.setText("$" + String.format("%.2f", total));
        
        // Display order items
        if (order.items != null) {
            orderItemAdapter.updateItems(order.items);
        } else {
            orderItemAdapter.updateItems(new ArrayList<>());
        }
    }

    private void createSampleOrderDetails() {
        // Sample order details
        orderId.setText("#" + currentOrderId);
        orderDate.setText("Dec 15, 2024");
        storeName.setText("Tech Store");
        orderTotal.setText("$1,299.99");

        // Sample order items
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("1", "iPhone 15 Pro", "Apple", 999.99, 1));
        items.add(new OrderItem("2", "AirPods Pro", "Apple", 249.99, 1));
        items.add(new OrderItem("3", "Lightning Cable", "Apple", 29.99, 1));
        items.add(new OrderItem("4", "Screen Protector", "Apple", 19.99, 1));

        orderItemAdapter.updateItems(items);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btn_track_order) {
            // Implement order tracking
            if (currentOrderId != null && !currentOrderId.isEmpty()) {
                // In a real app, this would open a tracking page or external tracking service
                Toast.makeText(this, "Tracking order #" + currentOrderId + "\nStatus: In Transit\nExpected delivery: 2-3 business days", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Order ID not available for tracking", Toast.LENGTH_SHORT).show();
            }
        } else if (viewId == R.id.btn_contact_support) {
            // Implement contact support
            if (currentOrderId != null && !currentOrderId.isEmpty()) {
                // In a real app, this would open email client or support chat
                Toast.makeText(this, "Contact support for order #" + currentOrderId + "\nEmail: support@ecommerceapp.com\nPhone: 1-800-SUPPORT", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Order ID not available for support", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
