package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

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
    private TextView tvForgotPassword;
    public static int check;


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

        tvForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        tvForgotPassword.setOnClickListener(this);


        preferences = getSharedPreferences("b07Project", Context.MODE_PRIVATE);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();
        checkSharedPreferences();

        presenter = new Presenter(Model.getInstance(), this);
        
        // Add entrance animations
        startEntranceAnimations();
    }

    private void checkSharedPreferences() {
        boolean remember = preferences.getBoolean("remember", false);
        String email = preferences.getString("email", "");
        // SECURITY FIX: Don't load password from storage

        edTxtEmail.setText(email);
        // Password field remains empty for security
        ckBxRemember.setChecked(remember);
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btnLogIn) {
            logIn();
        } else if (viewId == R.id.register) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (viewId == R.id.forgotPassword) {
            forgotPassword();
        }
    }


    private void logIn() {
        String email = edTxtEmail.getText().toString().trim();
        String password = edTxtPassword.getText().toString().trim();

        // SECURITY FIX: Only store email, never store password
        editor.putBoolean("remember", ckBxRemember.isChecked());
        editor.putString("email", ckBxRemember.isChecked() ? email : "");
        // REMOVED: Password storage for security - passwords should never be stored locally
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
            edTxtPassword.setError("Password is required!");
            edTxtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edTxtPassword.setError("Minimum password length should be 6 characters!");
            edTxtPassword.requestFocus();
            return;
        }

        presenter.login(email, password);

    }

    public void redirectToStoreDashboard() {
        Intent intent = new Intent(this,StoreDashboardActivity.class);
        final String userID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userID != null) {
            intent.putExtra("currentUserID", userID);
            check = 1;
            startActivity(intent);
        } else {
            Log.e("MainActivity", "User ID is null, cannot redirect to store dashboard");
            Toast.makeText(this, "Authentication error. Please login again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void redirectToCustomerDashboard() {
        Intent intent = new Intent(this, CustomerDashboardActivity.class);
        final String userID = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userID != null) {
            intent.putExtra("currentUserID", userID);
            check = 0;
            startActivity(intent);
        } else {
            Log.e("MainActivity", "User ID is null, cannot redirect to customer dashboard");
            Toast.makeText(this, "Authentication error. Please login again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void failedToLogin() {
        Toast.makeText(this, "Failed to login. Please check your credentials.", Toast.LENGTH_LONG).show();
    }

    private void forgotPassword() {
        String email = edTxtEmail.getText().toString().trim();
        
        if (email.isEmpty()) {
            edTxtEmail.setError("Please enter your email address");
            edTxtEmail.requestFocus();
            return;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edTxtEmail.setError("Please provide valid email!");
            edTxtEmail.requestFocus();
            return;
        }
        
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Password reset email sent to " + email, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to send password reset email. Please check your email address.", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
    
    private void startEntranceAnimations() {
        // Get views
        View appLogo = findViewById(R.id.app_logo);
        View banner = findViewById(R.id.banner);
        View emailLayout = findViewById(R.id.email_input_layout);
        View passwordLayout = findViewById(R.id.password_input_layout);
        View loginButton = findViewById(R.id.btnLogIn);
        View registerText = findViewById(R.id.register);
        View forgotPasswordText = findViewById(R.id.forgotPassword);
        View themeToggle = findViewById(R.id.theme_toggle);
        
        // Set initial states
        appLogo.setAlpha(0f);
        appLogo.setScaleX(0.5f);
        appLogo.setScaleY(0.5f);
        
        banner.setAlpha(0f);
        banner.setTranslationY(-50f);
        
        emailLayout.setAlpha(0f);
        emailLayout.setTranslationX(-100f);
        
        passwordLayout.setAlpha(0f);
        passwordLayout.setTranslationX(-100f);
        
        loginButton.setAlpha(0f);
        loginButton.setTranslationY(50f);
        
        registerText.setAlpha(0f);
        forgotPasswordText.setAlpha(0f);
        themeToggle.setAlpha(0f);
        
        // Animate logo
        appLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setStartDelay(200)
            .start();
        
        // Animate banner
        banner.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setStartDelay(400)
            .start();
        
        // Animate email input
        emailLayout.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(600)
            .start();
        
        // Animate password input
        passwordLayout.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(700)
            .start();
        
        // Animate login button
        loginButton.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(800)
            .start();
        
        // Animate text links
        registerText.animate()
            .alpha(1f)
            .setDuration(400)
            .setStartDelay(900)
            .start();
        
        forgotPasswordText.animate()
            .alpha(1f)
            .setDuration(400)
            .setStartDelay(1000)
            .start();
        
        // Animate theme toggle
        themeToggle.animate()
            .alpha(1f)
            .rotation(360f)
            .setDuration(600)
            .setStartDelay(1100)
            .start();
    }
}
