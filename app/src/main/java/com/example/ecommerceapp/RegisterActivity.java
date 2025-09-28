package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.ecommerceapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView banner;
    private Button btnRegister;
    private EditText edtTxtName, edtTxtEmail, edtTxtPassword;
    private RadioButton ckOwner, radioCustomer;
    private Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(this);
        edtTxtName = (EditText)findViewById(R.id.name);
        edtTxtEmail = (EditText) findViewById(R.id.email);
        edtTxtPassword = (EditText) findViewById(R.id.password);
        ckOwner = (RadioButton) findViewById(R.id.radio_owner);
        radioCustomer = (RadioButton) findViewById(R.id.radio_customer);
        mAuth = FirebaseAuth.getInstance();
        model = Model.getInstance();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.banner) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (viewId == R.id.register) {
            register();
        }
    }
    private void register() {
        String email = edtTxtEmail.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();
        String name = edtTxtName.getText().toString().trim();
        boolean isOwner = ckOwner.isChecked();

        if (name.isEmpty()) {
            edtTxtName.setError("Name is required!");
            edtTxtName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtTxtEmail.setError("Email is required!");
            edtTxtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtTxtEmail.setError("Please provide valid email!");
            edtTxtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtTxtPassword.setError("Password is required!");
            edtTxtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtTxtPassword.setError("Minimum password length should be 6 characters!");
            edtTxtPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserModel user = new UserModel(email, name, "25"); // Default age

                            // check if owner or user
                            String userType = isOwner ? "Owners" : "Users";

                            String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
                            if (uid != null) {
                                FirebaseDatabase.getInstance().getReference(userType).child(uid)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "You have been registered successfully!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                // redirect to login
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            } else {
                                Log.e("RegisterActivity", "User ID is null after registration");
                                Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

