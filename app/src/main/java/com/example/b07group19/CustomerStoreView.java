package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.b07group19.models.Order;
import com.example.b07group19.Products;
import com.example.b07group19.models.Store;
import com.example.b07group19.models.UserModel;

public class CustomerStoreView extends AppCompatActivity {
    private Store store;

    private String storeName;

    private UserModel currentUser;

    private Button btnSendOrder;
    private ListView lvItems;

    private Order order;

    private Model model;

    private Products selectedItem;
    private TextView tvSelectedItem, tvNumberOfItems;
    private EditText edTxtSelectedQuantity;
    private Button btnCart;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_view);
        storeName = getIntent().getStringExtra("storeName");
        currentUser = (UserModel) getIntent().getSerializableExtra("currentUser");
        order = (Order) getIntent().getSerializableExtra("order");

        model = Model.getInstance();
        tvSelectedItem = (TextView) findViewById(R.id.tvSelectedItem);
        tvNumberOfItems = (TextView) findViewById(R.id.tvNumberOfItems);
        edTxtSelectedQuantity = (EditText) findViewById(R.id.edTxtSelectedQuantity);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnSendOrder = (Button) findViewById(R.id.btnSendOrder);


    }
    }