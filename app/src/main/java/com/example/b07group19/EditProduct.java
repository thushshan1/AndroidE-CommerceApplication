package com.example.b07group19;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07group19.models.Store;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProduct extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;


    private Button buttonManageProducts;
    private Button buttonViewOrder;
    private TextView storeText;
    private String currentUserID;
    private Model model;
    private Store store;

    public static String thename;
    public static String storeName;

    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        buttonManageProducts = (Button) findViewById(R.id.buttonManageProducts);
        buttonManageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProduct.this, AddProduct.class);
                intent.putExtra("storeName", storeName);
                startActivity(intent);

            }
        });
        // currentUserID = getIntent().getStringExtra("currentUserID");
        currentUserID = FirebaseAuth.getInstance().getUid();
        buttonViewOrder = (Button) findViewById(R.id.buttonViewOrders);

        model = Model.getInstance();
        storeText = (TextView) findViewById(R.id.textViewWelcome);
        reference = FirebaseDatabase.getInstance().getReference("Owners");
        reference.child(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = String.valueOf(dataSnapshot.child("name").getValue());

                        storeText.setText("Welcome back, " + name + "!");
                    }
                }
            }
        });



        buttonViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(EditProduct.this,OwnerOrderStatusActivity.class);
                intent.putExtra("storeName", storeName);
                startActivity(intent);

            }

        }
        );

        storeName = getIntent().getStringExtra("storeName");
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(storeName).child("products"), Products.class)
                        .build();

        mainAdapter = new MainAdapter(options,storeName);
        recyclerView.setAdapter(mainAdapter);



    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.buttonManageProducts) {
            Intent intent = new Intent(this, AddProduct.class);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        }
        if (viewId == R.id.btnViewOrders) {
            Intent intent = new Intent(this, StoreOrdersActivity.class);
            intent.putExtra("currentUserID", currentUserID);
            intent.putExtra("storeName", storeName);
            startActivity(intent);
        }




    }



    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("stores").child(storeName).child("products").orderByChild("product").startAt(str).endAt(str+"~"), Products.class)
                        .build();

        mainAdapter = new MainAdapter(options,storeName);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}