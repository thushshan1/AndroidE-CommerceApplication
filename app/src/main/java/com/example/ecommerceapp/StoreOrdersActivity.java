package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.models.Order;
import com.example.ecommerceapp.models.OrderDescription;
import com.example.ecommerceapp.models.Store;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.util.Log;
import java.util.List;
import java.util.Map;

public class StoreOrdersActivity extends AppCompatActivity {
    private String currentUserID;
    private Store store;
    private String storeName;
    private ListView lv;
    private Model model;
    private FirebaseListAdapter<OrderDescription> adapter;
    private ListView pending;
    private ValueEventListener ordersListener; // For memory leak prevention
    private List<Order> pendingOrders;
    Map<String,String> userNames;

    public static String theID;

   // private List<Order> pendingOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);

        currentUserID = getIntent().getStringExtra("currentUserID");
        model = Model.getInstance();
        lv = findViewById(R.id.lvOrders);
        storeName = getIntent().getStringExtra("storeName");
//        getStore();
        getOrders();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String orderID = pendingOrders.get(i).getOrderID();
                theID = String.valueOf(orderID);
                Intent intent = new Intent(StoreOrdersActivity.this, OrderDetailsActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

    }
    private void getOrders() {
        ordersListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pendingOrders = new ArrayList<Order>();

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = (Order) orderSnapshot.getValue(Order.class);
                            if (order != null && order.status != null && order.status.equals("pending")) {
                                pendingOrders.add(order);
                            }
                        }
                        Model.getInstance().getUserNames(userNames ->{

                            OwnerOrderAdapter pendingAdapter =
                                    new OwnerOrderAdapter(StoreOrdersActivity.this, R.layout.activity_owner_order_adapter, pendingOrders,userNames);
                            lv.setAdapter(pendingAdapter);
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("StoreOrdersActivity", "Database error loading orders: " + error.getMessage());
                    }
                };
                
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("storeName").equalTo(storeName)
                .addValueEventListener(ordersListener);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MEMORY LEAK FIX: Remove Firebase listeners
        if (ordersListener != null) {
            FirebaseDatabase.getInstance().getReference("orders")
                    .orderByChild("storeName").equalTo(storeName)
                    .removeEventListener(ordersListener);
        }
    }

}

