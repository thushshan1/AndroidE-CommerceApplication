package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerStoreView2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter2 mainAdapter2;

    MainAdapter3 mainAdapter3;





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
        mainAdapter3 = new MainAdapter3(options);
        recyclerView.setAdapter(mainAdapter2);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter2.startListening();
        mainAdapter3.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter2.stopListening();
        mainAdapter3.stopListening();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(CustomerDashboardActivity.storeName).child("Products").orderByChild("product").startAt(str).endAt(str+"~"), Products.class)
                        .build();

        mainAdapter2 = new MainAdapter2(options);
        mainAdapter2.startListening();
        recyclerView.setAdapter(mainAdapter2);
    }
}