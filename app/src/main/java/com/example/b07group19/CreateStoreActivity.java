package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07group19.models.Store;

public class CreateStoreActivity extends AppCompatActivity {
    private String currentUserID;

    private EditText edTxtStoreName;
    private Button btnCreateStore;

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        edTxtStoreName = (EditText) findViewById(R.id.storeName);
        btnCreateStore = (Button) findViewById(R.id.createStore);
        //btnCreateStore.setOnClickListener(this);

        currentUserID = getIntent().getStringExtra("currentUserID");

        model = Model.getInstance();
        btnCreateStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeName = edTxtStoreName.getText().toString().trim().toLowerCase();
                model.getStoreByName(storeName, (Store store) -> {
                    if(store==null){
                        createStore();
                    }
                    else{
                        Toast.makeText(CreateStoreActivity.this, "Store name already exist!", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
            }
        });
    }
    private void createStore() {
        String storeName = edTxtStoreName.getText().toString().trim().toLowerCase();
        Store store = new Store(storeName);
        store.owner = currentUserID;


        model.postStore(store, (Boolean created) -> {
            if (!created) {
                Toast.makeText(CreateStoreActivity.this, "Failed to create a store!", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(CreateStoreActivity.this, "Store Created.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, EditProduct.class);
            intent.putExtra("currentUserID", currentUserID);
            startActivity(intent);
        });

    }
}