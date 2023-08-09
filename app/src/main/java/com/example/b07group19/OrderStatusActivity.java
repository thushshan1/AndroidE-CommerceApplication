package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.b07group19.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {

    private String currentUserID;
    private ListView Pending, Completed;
    private List<Order> pendingOrders, completedOrders;

    public static String theID2;
    @Override
    protected void onCreate(Bundle InstanceState){
        super.onCreate(InstanceState);
        setContentView(R.layout.activity_order_status);
        currentUserID = getIntent().getStringExtra("currentUserID");

        Pending = (ListView) findViewById(R.id.lvPending);
        Completed = (ListView) findViewById(R.id.lvCompleted);

        Pending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String orderID = pendingOrders.get(i).getOrderID();
                theID2 = String.valueOf(pendingOrders.get(i).getOrderID());
                Intent intent = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

        getOrders();
    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("userID").equalTo(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pendingOrders = new ArrayList<Order>();
                        completedOrders = new ArrayList<Order>();

                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = (Order) orderSnapshot.getValue(Order.class);
                            if (order.status.equals("pending")) pendingOrders.add(order);
                            else completedOrders.add(order);
                        }

                        OrderStatusAdapter pendingAdapter =
                                new OrderStatusAdapter(OrderStatusActivity.this, R.layout.activity_order_status_adapter, pendingOrders);
                        Pending.setAdapter(pendingAdapter);

                        OrderStatusAdapter completedAdapter =
                                new OrderStatusAdapter(OrderStatusActivity.this, R.layout.activity_order_status_adapter, completedOrders);
                        Completed.setAdapter(completedAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
