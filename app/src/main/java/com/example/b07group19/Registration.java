package com.example.b07group19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db = FirebaseDatabase.getInstance("https://b07project-c6f23-default-rtdb.firebaseio.com/");
    }

    public void onClickSignUp(View view){
        DatabaseReference ref= db.getReference();
        EditText shopperUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText shopperPassword = (EditText) findViewById(R.id.editTextUserPassword);
        String username = shopperUsername.getText().toString();
        String userpassword = shopperPassword.getText().toString();
        shopperUsername.setText("");
        shopperPassword.setText("");
        DatabaseReference query = ref.child("shoppers").child(username);

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    ref.child("shoppers").child(username).child("username").setValue(username);
                    ref.child("shoppers").child(username).child("password").setValue(userpassword);
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