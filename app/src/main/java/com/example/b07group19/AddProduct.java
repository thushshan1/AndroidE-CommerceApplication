package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.b07group19.models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    private EditText ProductName, Price, Brand, Quantity, Length, Width, Height, url;

    private Button button;

    private Model model;

    private Products product;


    private FirebaseAuth mAuth;

    DatabaseReference ref;

    private String currentUserID;

    private Store store;

    private String the;

    private String storeName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(this);
        ProductName = (EditText) findViewById(R.id.ProductName);
        Price = (EditText) findViewById(R.id.Price);
        Brand = (EditText) findViewById(R.id.Brand);
        Quantity = (EditText) findViewById(R.id.Quantity);
        Length = (EditText) findViewById(R.id.Length);
        Width = (EditText) findViewById(R.id.Width);
        Height = (EditText) findViewById(R.id.Height);
        url = (EditText) findViewById(R.id.ExtraInfo);
        currentUserID = getIntent().getStringExtra("currentUserID");

        storeName = getIntent().getStringExtra("storeName");
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button4) {
            insert();
            clearAll();

        }
    }

    private void insert(){
        Products product = new Products();
        product.product = ProductName.getText().toString();
        product.price = Price.getText().toString();
        product.brand = Brand.getText().toString();
        product.height = Height.getText().toString();
        product.length = Length.getText().toString();
        product.width = Width.getText().toString();
        product.quantity = Quantity.getText().toString();
        product.turl = url.getText().toString();

        FirebaseDatabase.getInstance().getReference("stores").child(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map<String, Object> ob= (HashMap<String, Object>) snapshot.getValue();
//                store = new Store();
//                store.setOwner((String) ob.get("owner"));
//                store.setStoreName((String) ob.get("storeName"));
                Store store = snapshot.getValue(Store.class);
                store.products.add(product);

                // save the updated store back to firebase
                FirebaseDatabase.getInstance().getReference("stores").child(storeName).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddProduct.this, task.isSuccessful() ? "added" : "failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearAll()
    {
        ProductName.setText("");
        Price.setText("");
        Brand.setText("");
        Length.setText("");
        Width.setText("");
        Quantity.setText("");
        url.setText("");
        Height.setText("");
    }

}