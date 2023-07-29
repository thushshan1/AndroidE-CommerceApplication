package com.example.b07group19;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07group19.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView banner;
    private Button btnRegister;
    private EditText edtTxtName, edtTxtAge, edtTxtEmail,edtTxtPassword, edtTxtPassword2;
    private CheckBox ckOwner;
    private ProgressBar progressBar;
    private Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        edtTxtName = (EditText)findViewById(R.id.name);
        edtTxtAge = (EditText) findViewById(R.id.age);
        edtTxtEmail = (EditText) findViewById(R.id.email);
        edtTxtPassword = (EditText) findViewById(R.id.password);
        edtTxtPassword2 = (EditText) findViewById(R.id.password2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ckOwner = (CheckBox) findViewById(R.id.ckOwner);
        mAuth = FirebaseAuth.getInstance();
        model = Model.getInstance();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.banner) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (viewId == R.id.btnRegister) {
            register();
        }
    }
    private void register() {
        String email = edtTxtEmail.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();
        String name = edtTxtName.getText().toString().trim();
        String age = edtTxtAge.getText().toString().trim();
        String password2 = edtTxtPassword2.getText().toString().trim();

        if (name.isEmpty()) {
            edtTxtName.setError("Name is required!");
            edtTxtName.requestFocus();
            return;
        }

        if (!password.equals(password2)) {
            edtTxtPassword2.setError("Password does not match");
            edtTxtPassword2.requestFocus();
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
            edtTxtPassword.setError("password is required!");
            edtTxtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtTxtPassword.setError("Min password length should be 6 characters!");
            edtTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserModel user = new UserModel(email, name, age);

                            // check if owner or user
                            String userType = ckOwner.isChecked() ? "Owners" : "Users";

                            FirebaseDatabase.getInstance().getReference(userType).child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "You have been registered successfully!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                // redirect to login
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
//        model.register(email, password, (String userID) -> {
//            if (userID == null) {
//                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//                return;
//            }
//            UserModel user = new UserModel(userID, email, age, ckOwner.isChecked());
//            model.postUser(user, (Boolean created) -> {
//                if (!created) {
//                    Toast.makeText(RegisterActivity.this, "Failed to create a user!", Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                Toast.makeText(RegisterActivity.this, "You have been registered successfully", Toast.LENGTH_LONG).show();
//                progressBar.setVisibility(View.GONE);
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//            });
//        });
}

