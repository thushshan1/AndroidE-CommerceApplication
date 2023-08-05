package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerStoreView2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter2 mainAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_view2);

        recyclerView = (RecyclerView)findViewById(R.id.Rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(CustomerDashboardActivity.storeName).child("Products"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options);
        recyclerView.setAdapter(mainAdapter2);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter2.stopListening();
    }
}