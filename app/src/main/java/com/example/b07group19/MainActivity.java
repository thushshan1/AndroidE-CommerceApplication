package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private EditText edTxtEmail, edTxtPassword;
    private Button btnLogIn;
    private CheckBox ckBxRemember;
    private TextView tvRegister;
    private Model model;
    private Presenter presenter;
    private CheckBox ckOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        edTxtEmail = (EditText) findViewById(R.id.editTextEmail);
        edTxtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);

        ckBxRemember = (CheckBox) findViewById(R.id.checkboxRemember);

        tvRegister = (TextView) findViewById(R.id.register);
        tvRegister.setOnClickListener(this);


        preferences = getSharedPreferences("b07Project", Context.MODE_PRIVATE);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();
        checkSharedPreferences();

        presenter = new Presenter(Model.getInstance(), this);
    }

    private void checkSharedPreferences() {
        boolean remember = preferences.getBoolean("remember", false);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        edTxtEmail.setText(email);
        edTxtPassword.setText(password);
        ckBxRemember.setChecked(remember);
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btnLogIn) {
            logIn();
        } else if (viewId == R.id.register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    private void logIn() {
        String email = edTxtEmail.getText().toString().trim();
        String password = edTxtPassword.getText().toString().trim();

        editor.putBoolean("remember", ckBxRemember.isChecked());
        editor.putString("email", ckBxRemember.isChecked() ? email : "");
        editor.putString("password", ckBxRemember.isChecked() ? password : "");
        editor.apply();

        if (email.isEmpty()) {
            edTxtEmail.setError("Email is required!");
            edTxtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edTxtEmail.setError("Please provide valid email!");
            edTxtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edTxtPassword.setError("password is required!");
            edTxtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edTxtPassword.setError("Min password length should be 6 characters!");
            edTxtPassword.requestFocus();
            return;
        }

//        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    final String userID = mAuth.getCurrentUser().getUid();
//                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                    DatabaseReference userRef = rootRef.child("Users").child(userID);
//                    DatabaseReference ownerRef = rootRef.child("Owners").child(userID);
//
//                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                // User is a User, navigate to customerDashboard
//                                startActivity(new Intent(MainActivity.this, CustomerDashboardActivity.class));
//                            } else {
//                                // If not found in Users, check in Owners
//                                ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            // User is an Owner, navigate to StoreDashboard
//                                            Intent intent = new Intent(MainActivity.this, StoreDashboardActivity.class);
//                                            intent.putExtra("currentUserID", userID);
//                                            startActivity(intent);
//
//                                        } else {
//                                            // User not found in either Users or Owners
//                                            Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        presenter.login(email, password);

    }

    public void redirectToStoreDashboard() {
        Intent intent = new Intent(this,StoreDashboardActivity.class);
        final String userID = mAuth.getCurrentUser().getUid();
        intent.putExtra("currentUserID", userID);

        startActivity(intent);
    }

    public void redirectToCustomerDashboard() {
        Intent intent = new Intent(this, CustomerDashboardActivity.class);
        final String userID = mAuth.getCurrentUser().getUid();

        intent.putExtra("currentUserID", userID);

        startActivity(intent);
    }

    public void failedToLogin() {
        Toast.makeText(this, "failed to login.", Toast.LENGTH_LONG).show();
    }
}
