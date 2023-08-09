package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderDescription;
import com.example.b07group19.models.Store;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
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
                Intent intent = new Intent(StoreOrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

    }
    private void getOrders() {
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("storeName").equalTo(storeName)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pendingOrders = new ArrayList<Order>();

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = (Order) orderSnapshot.getValue(Order.class);
                            if (order.status.equals("pending")) pendingOrders.add(order);
                        }
                        OwnerOrderAdapter pendingAdapter =
                                new OwnerOrderAdapter(StoreOrdersActivity.this, R.layout.activity_owner_order_adapter, pendingOrders);
                        lv.setAdapter(pendingAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
//        FirebaseListOptions<OrderDescription> options = new FirebaseListOptions.Builder<OrderDescription>()
//                .setLayout(R.layout.activity_order_description_adapter)
//                .setQuery(databaseReference, OrderDescription.class)
//                .build();
//
//        adapter = new FirebaseListAdapter<OrderDescription>(options) {
//            @Override
//            protected void populateView(@NonNull View v, @NonNull OrderDescription model, int position) {
//                TextView tvCustomerName = v.findViewById(R.id.tvCustomerName);
//                TextView tvCreatedDate = v.findViewById(R.id.tvCreatedDate);
//                TextView tvOrderID = v.findViewById(R.id.tvOrderID);
//
//                tvCustomerName.setText(model.getCustomerName());
//                tvCreatedDate.setText(model.getCreatedDate());
//                tvOrderID.setText(model.getOrderID());
//            }
//        };
//
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                OrderDescription order = adapter.getItem(i);
//                String orderID = order.getOrderID();
//
//                Intent intent = new Intent(StoreOrdersActivity.this, OrderDetailActivity.class);
//                intent.putExtra("orderID", orderID);
//                startActivity(intent);
//            }
//        });
//
//        adapter.startListening();
