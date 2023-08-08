package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.b07group19.models.Cart;
import com.example.b07group19.models.Store;
import com.example.b07group19.models.UserCart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {
    private String currentUserID;
    private String username;
    private List<String> storeNames;
    private ListView listViewStores;
    //private Button ShoppingCart;
    private Button PreviousOrder;
    private Button Logout;
    private Model model;
    private Button ViewOrder;
    FloatingActionButton fabCart;

    static public String storeName;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        //ShoppingCart = (Button) findViewById(R.id.btnViewCart);
        PreviousOrder = (Button) findViewById(R.id.btnViewOrders);
        fabCart = findViewById(R.id.cart_fab);
        Logout = (Button) findViewById(R.id.btnLogout);
        username = getIntent().getStringExtra("username");
        ViewOrder = (Button) findViewById(R.id.buttonViewOrders);
        username = FirebaseAuth.getInstance().getUid();
        currentUserID = username;
        Cart cart =UserCart.getCart(username);
//        currentUserID = getIntent().getStringExtra("currentUserID");


        model = Model.getInstance();




        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(CustomerDashboardActivity.this, ShoppingCart.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(CustomerDashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        PreviousOrder = (Button) findViewById(R.id.btnViewOrders);
        PreviousOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("currentUserID", currentUserID);
                intent.setClass(CustomerDashboardActivity.this, OrderStatusActivity.class);
                startActivity(intent);
            }
        });

        listViewStores = findViewById(R.id.listViewStores);
        listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                storeName = storeNames.get(i);
                Intent intent = new Intent(CustomerDashboardActivity.this, CustomerStoreView2.class);
                intent.putExtra("storeName", storeName);
                intent.putExtra("username", username);
                startActivity(intent);
            }

        });

        getStoreNames();

    }
    private void getStoreNames() {
        model.getStoreNames((List<String> storeNames) -> {
            this.storeNames = storeNames;

            ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeNames);
            listViewStores.setAdapter(storeAdapter);
        });
    }
    public void getStores() {
        FirebaseDatabase.getInstance().getReference("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeNames = new ArrayList<String>();
                // List<Store> stores = new ArrayList<Store>();
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {
                    Store store = userSnapShot.getValue(Store.class);
//                    storeNames.add(store.storeName);
                }
                // callback.accept(storeNames);

                // TODO: update the UI (listView: set Adapter)
                ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(CustomerDashboardActivity.this, android.R.layout.simple_list_item_1, storeNames);
                listViewStores.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


}