package com.example.b07group19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                        Model.getInstance().getUserNames(userNames ->{

                            OwnerOrderAdapter pendingAdapter =
                                    new OwnerOrderAdapter(StoreOrdersActivity.this, R.layout.activity_owner_order_adapter, pendingOrders,userNames);
                            lv.setAdapter(pendingAdapter);
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}

