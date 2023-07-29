package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.b07group19.models.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopperDashboard extends AppCompatActivity{
    private String username;
    private List<String> storeNames;
    private ListView listViewStores;
    private Button ShoppingCart;
    private Button PreviousOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_dashboard);
        ShoppingCart = (Button) findViewById(R.id.Cart);
        PreviousOrder = (Button) findViewById(R.id.Order);
        username = getIntent().getStringExtra("username");
        ShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                intent.setClass(ShopperDashboard.this, ShoppingCart.class);
                startActivity(intent);
            }
        });
        PreviousOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                intent.setClass(ShopperDashboard.this, PreviousOrder.class);
                startActivity(intent);
            }
        });

        listViewStores = findViewById(R.id.listViewStores);
        listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String storeName = storeNames.get(i);
                Intent intent = new Intent(ShopperDashboard.this, StoreDashbord.class);
                intent.putExtra("storeName", storeName);
                intent.putExtra("username", username);
                startActivity(intent);
            }

        });

        getStores();

    }

    public void getStores() {
        FirebaseDatabase.getInstance().getReference("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeNames = new ArrayList<String>();
                // List<Store> stores = new ArrayList<Store>();
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {
                    Store store = userSnapShot.getValue(Store.class);
                    storeNames.add(store.storeName);
                }
                // callback.accept(storeNames);

                // TODO: update the UI (listView: set Adapter)
                ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(ShopperDashboard.this, android.R.layout.simple_list_item_1, storeNames);
                listViewStores.setAdapter(storeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    }
//}