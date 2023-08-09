package com.example.b07group19;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.b07group19.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerOrderStatusActivity extends AppCompatActivity {

    private String currentUserID;
    private String storeName;
    private ListView Pending, Completed;
    private List<Order> pendingOrders, completedOrders;
    @Override
    protected void onCreate(Bundle InstanceState){
        super.onCreate(InstanceState);
        setContentView(R.layout.activity_owner_order_status);
        currentUserID = getIntent().getStringExtra("currentUserID");
        storeName = getIntent().getStringExtra("storeName");
        Pending = (ListView) findViewById(R.id.lvPending);
        Completed = (ListView) findViewById(R.id.lvCompleted);

        getOrders();
    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("storeName").equalTo(storeName)
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
                        Model.getInstance().getUserNames(userNames ->{

                            OwnerOrderStatusAdapter pendingAdapter =
                                    new OwnerOrderStatusAdapter(OwnerOrderStatusActivity.this, R.layout.activity_owner_order_status_adapter, pendingOrders,userNames);
                            Pending.setAdapter(pendingAdapter);

                            OwnerOrderStatusAdapter completedAdapter =
                                    new OwnerOrderStatusAdapter(OwnerOrderStatusActivity.this, R.layout.activity_owner_order_status_adapter, completedOrders,userNames);
                            Completed.setAdapter(completedAdapter);
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
