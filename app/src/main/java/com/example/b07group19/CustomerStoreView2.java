package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.b07group19.models.Cart;
import com.example.b07group19.models.UserCart;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerStoreView2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter2 mainAdapter2;
    String storeName;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_view2);
        storeName = getIntent().getStringExtra("storeName");
        UserCart.setupStoreCart(storeName);
        recyclerView = (RecyclerView)findViewById(R.id.Rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton checkout= (FloatingActionButton) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CustomerStoreView2.this, ShoppingCart.class);
                startActivity(intent);
            }
        });

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(CustomerDashboardActivity.storeName).child("products"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options,storeName);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchbar, menu);
        MenuItem item =  menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                text(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                text(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void text(String str){
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(CustomerDashboardActivity.storeName).child("products").orderByChild("product").startAt(str).endAt(str+"~"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options,storeName);
        mainAdapter2.startListening();
        recyclerView.setAdapter(mainAdapter2);
    }
}