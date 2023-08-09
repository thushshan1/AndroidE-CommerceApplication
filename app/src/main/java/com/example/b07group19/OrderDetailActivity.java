package com.example.b07group19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderStatus;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private String orderID;
    private Order order;

    private ListView listView;
    private DatabaseReference reference;

    private List<OrderStatus> thelist;



    private ArrayAdapter<String> adapter;

    OrderStatus model;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.check == 1)
        {
            setContentView(R.layout.activity_owner_order_detail);
            orderID = StoreOrdersActivity.theID;
        } else {
            setContentView(R.layout.activity_shopper_order_detail);
            orderID = OrderStatusActivity.theID2;
        }



        listView = (ListView) findViewById(R.id.lvItems);


        //orderID = getIntent().getStringExtra("orderID");

        getOrders();
    }

    private void getOrders()
    {
        FirebaseDatabase.getInstance().getReference("orders")
                .child(orderID).child("items")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        thelist = new ArrayList<OrderStatus>();

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            OrderStatus order = (OrderStatus) orderSnapshot.getValue(OrderStatus.class);
                            thelist.add(order);

                        }
                        OrderStatusAdaptor2 pendingAdapter =
                                new OrderStatusAdaptor2(OrderDetailActivity.this, R.layout.activity_order_status_adapter2, thelist);
                        listView.setAdapter(pendingAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}