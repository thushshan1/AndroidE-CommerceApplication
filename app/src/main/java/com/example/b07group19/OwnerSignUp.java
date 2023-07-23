package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OwnerSignUp extends AppCompatActivity {

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_sign_up);
        db = FirebaseDatabase.getInstance("https://b07project-c6f23-default-rtdb.firebaseio.com/");
    }

    public void onClickSignUp(View view){
        DatabaseReference ref= db.getReference();
        EditText ownerUsername = (EditText) findViewById(R.id.editTextOwnername);
        EditText ownerPassword = (EditText) findViewById(R.id.editTextOwnerPassword);
        String username = ownerUsername.getText().toString();
        String userpassword = ownerPassword.getText().toString();
        ownerUsername.setText("");
        ownerPassword.setText("");
        DatabaseReference query = ref.child("owners").child(username);

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    ref.child("owners").child(username).child("username").setValue(username);
                    ref.child("owners").child(username).child("password").setValue(userpassword);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Shopper already exists", Toast.LENGTH_LONG ).show();
                    ownerUsername.setHint("Enter new username");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void onClickGoBack(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}