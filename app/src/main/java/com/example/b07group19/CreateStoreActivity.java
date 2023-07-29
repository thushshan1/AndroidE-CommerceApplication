package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07group19.models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class CreateStoreActivity extends AppCompatActivity {
    private Button btnCreateStore;
    private EditText edTxtStoreName;
    FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_create);
        edTxtStoreName = (EditText) findViewById(R.id.storeName);
        btnCreateStore = (Button) findViewById(R.id.btnCreateStore);
        db = FirebaseDatabase.getInstance("https://b07project-c6f23-default-rtdb.firebaseio.com/");
//        btnCreateStore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //监听按钮，如果点击，就跳转
//                Intent intent = new Intent();
//                intent.setClass(CreateStoreActivity.this, ShopperDashboard.class);
//                startActivity(intent);
//            }
//        });
    }
    public void onClickCreate(View view){
        DatabaseReference ref= db.getReference();
        edTxtStoreName = (EditText) findViewById(R.id.storeName);
        String StoreName = edTxtStoreName.getText().toString();
        edTxtStoreName.setText("");
        DatabaseReference query = ref.child("stores").child(StoreName);

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    Toast.makeText(CreateStoreActivity.this, "Store Created.", Toast.LENGTH_LONG).show();
                    ref.child("stores").child(StoreName).child("storeName").setValue(StoreName);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Name already exists", Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


}