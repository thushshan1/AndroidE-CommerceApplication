package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerStoreViewWithCart extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomerStoreVIewAdaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_view_with_cart);
        recyclerView = (RecyclerView)findViewById(R.id.rv_with_card);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(CustomerDashboardActivity.storeName).child("Products"), Products.class)
                        .build();

        adaptor = new CustomerStoreVIewAdaptor(options);
        recyclerView.setAdapter(adaptor);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptor.stopListening();
    }
}