package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.b07group19.models.OrderDescription;
import com.example.b07group19.models.Store;

public class StoreOrdersActivity extends AppCompatActivity {
    private String currentUserID;
    private Store store;
    private ListView lv;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);

        currentUserID = getIntent().getStringExtra("currentUserID");
        model = Model.getInstance();
        //getStore();
        lv = findViewById(R.id.lvOrders);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String orderID = store.pendingOrders.get(i).orderID;
                Intent intent = new Intent(StoreOrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);



            }
        });
    }

    private  void getStore() {
        Toast.makeText(this, currentUserID, Toast.LENGTH_LONG).show();
        model.getStoreByOwner(currentUserID, (Store store) -> {
            this.store = store;

            OrderDescriptionAdapter adapter = new OrderDescriptionAdapter(
                    StoreOrdersActivity.this, R.layout.activity_order_description_adapter, store.pendingOrders);
            lv.setAdapter(adapter);


        });
    }
}