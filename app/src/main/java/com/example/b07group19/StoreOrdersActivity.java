package com.example.b07group19;

import static com.example.b07group19.models.Order.orderID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderDescription;
import com.example.b07group19.models.Store;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoreOrdersActivity extends AppCompatActivity {
    private String currentUserID;
    private Store store;
    private ListView lv;
    private Model model;
    private FirebaseListAdapter<OrderDescription> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);

        currentUserID = getIntent().getStringExtra("currentUserID");
        model = Model.getInstance();
        lv = findViewById(R.id.lvOrders);

        getStore();
    }

    private void getStore() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("orders");



        FirebaseListOptions<OrderDescription> options = new FirebaseListOptions.Builder<OrderDescription>()
                .setLayout(R.layout.activity_order_description_adapter)
                .setQuery(databaseReference, OrderDescription.class)
                .build();

        adapter = new FirebaseListAdapter<OrderDescription>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull OrderDescription model, int position) {
                TextView tvCustomerName = v.findViewById(R.id.tvCustomerName);
                TextView tvCreatedDate = v.findViewById(R.id.tvCreatedDate);
                TextView tvOrderID = v.findViewById(R.id.tvOrderID);

                tvCustomerName.setText(model.getCustomerName());
                tvCreatedDate.setText(model.getCreatedDate());
                tvOrderID.setText(model.getOrderID());
            }
        };

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderDescription order = adapter.getItem(i);
                String orderID = order.getOrderID();

                Intent intent = new Intent(StoreOrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

        adapter.startListening();
    }
}
