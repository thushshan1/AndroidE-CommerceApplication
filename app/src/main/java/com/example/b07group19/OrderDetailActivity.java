package com.example.b07group19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private String orderID;
    private Order order;
    private String storeName;
    private String orderStatus;
    private DatabaseReference orderRef;
    private ListView listView;
    private DatabaseReference reference;

    private List<OrderStatus> thelist;
    private ArrayAdapter<String> adapter;


    OrderStatus model;
    Button compeleteOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        orderID = getIntent().getStringExtra("orderID");
        storeName = getIntent().getStringExtra("storeName");
        orderStatus = getIntent().getStringExtra("orderStatus");

        orderRef = FirebaseDatabase.getInstance().getReference("orders");
        if (MainActivity.check == 1 && orderStatus.equals("pending")) {
            setContentView(R.layout.activity_owner_order_detail);

            compeleteOrder = (Button) findViewById(R.id.btnSendOrder);
            compeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderRef.child(orderID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Order order = snapshot.getValue(Order.class);
                            order.setStatus("completed");
                            orderRef.child(orderID).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(OrderDetailActivity.this, OwnerOrderStatusActivity.class);
                                    intent.putExtra("orderID", orderID);
                                    intent.putExtra("storeName", storeName);

                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    Model.getInstance().completeOrder(orderID);
                    Toast.makeText(OrderDetailActivity.this, "Order completed!", Toast.LENGTH_LONG).show();


                }
            });
        } else {
            setContentView(R.layout.activity_shopper_order_detail);


        }

        listView = (ListView) findViewById(R.id.lvItems);


        getOrders();
    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference("orders").child(orderID)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        order = snapshot.getValue(Order.class);
                        order.setOrderID(snapshot.getKey());
                        OrderStatusAdaptor2 pendingAdapter =
                                new OrderStatusAdaptor2(OrderDetailActivity.this, R.layout.activity_order_status_adapter2, order);
                        listView.setAdapter(pendingAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}