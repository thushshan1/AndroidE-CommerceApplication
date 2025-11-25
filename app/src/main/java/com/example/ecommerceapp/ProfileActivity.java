package com.example.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerceapp.models.Store;
import com.example.ecommerceapp.models.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

public class ProfileActivity extends AppCompatActivity {

    private String currentUserID;
    private Model model;
    private Store store;
    private String currentStoreKey; // The actual Firebase key (store name) for the current store
    private UserModel ownerInfo;
    
    private TextInputEditText editTextOwnerName;
    private TextInputEditText editTextOwnerEmail;
    private TextInputEditText editTextStoreName;
    private Button buttonSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get current user
        currentUserID = FirebaseAuth.getInstance().getUid();
        if (currentUserID == null) {
            Log.e("ProfileActivity", "User ID is null, redirecting to login");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        model = Model.getInstance();

        // Initialize views
        editTextOwnerName = findViewById(R.id.editTextOwnerName);
        editTextOwnerEmail = findViewById(R.id.editTextOwnerEmail);
        editTextStoreName = findViewById(R.id.editTextStoreName);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);

        // Set up toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load profile information
        loadProfileInformation();

        // Set click listener
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInformation();
            }
        });
    }

    private void loadProfileInformation() {
        // Load owner information
        model.getOwner(currentUserID, (UserModel owner) -> {
            if (owner != null) {
                ownerInfo = owner;
                if (editTextOwnerName != null && owner.name != null) {
                    editTextOwnerName.setText(owner.name);
                }
                if (editTextOwnerEmail != null) {
                    String email = owner.email != null ? owner.email : 
                        (FirebaseAuth.getInstance().getCurrentUser() != null ? 
                         FirebaseAuth.getInstance().getCurrentUser().getEmail() : "");
                    editTextOwnerEmail.setText(email);
                }
            } else {
                // Fallback to Firebase Auth email
                if (editTextOwnerEmail != null) {
                    String email = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                        FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
                    editTextOwnerEmail.setText(email);
                }
            }
        });

        // Load store information - we need to get the actual Firebase key
        loadStoreWithKey();
    }

    private void loadStoreWithKey() {
        // Get store by owner ID (stores are now keyed by owner ID)
        model.getStoreByOwner(currentUserID, (Store store) -> {
            if (store != null) {
                ProfileActivity.this.store = store;
                currentStoreKey = currentUserID; // Store key is now the owner ID
                
                if (editTextStoreName != null) {
                    if (store.storeName != null && !store.storeName.equals(currentUserID)) {
                        editTextStoreName.setText(store.storeName);
                    } else {
                        editTextStoreName.setText("");
                    }
                }
            }
        });
    }

    private void saveProfileInformation() {
        String newOwnerName = editTextOwnerName.getText().toString().trim();
        String newStoreName = editTextStoreName.getText().toString().trim();

        // Validate store name if provided
        if (!newStoreName.isEmpty()) {
            if (newStoreName.length() < 3) {
                editTextStoreName.setError("Store name must be at least 3 characters!");
                editTextStoreName.requestFocus();
                return;
            }

            if (newStoreName.length() > 50) {
                editTextStoreName.setError("Store name must be less than 50 characters!");
                editTextStoreName.requestFocus();
                return;
            }
        }

        // Update owner name
        if (newOwnerName != null && !newOwnerName.isEmpty()) {
            if (ownerInfo == null) {
                ownerInfo = new UserModel();
                ownerInfo.userID = currentUserID;
                String email = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                    FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
                ownerInfo.email = email;
            }
            ownerInfo.name = newOwnerName;
            ownerInfo.userID = currentUserID;

            model.postOwner(ownerInfo, (Boolean updated) -> {
                if (!updated) {
                    Log.e("ProfileActivity", "Failed to update owner name");
                }
            });
        }

        // Update store name if provided
        if (!newStoreName.isEmpty()) {
            // Check if store name already exists (and it's not the current store)
            model.getStoreByName(newStoreName, (Store existingStore) -> {
                if (existingStore != null && existingStore.owner != null && !existingStore.owner.equals(currentUserID)) {
                    editTextStoreName.setError("Store name already exists!");
                    editTextStoreName.requestFocus();
                    return;
                }

                if (store != null) {
                    // Store exists - just update the store name field
                    store.storeName = newStoreName;
                    store.owner = currentUserID; // Ensure owner is set
                    model.postStore(store, (Boolean updated) -> {
                        if (updated) {
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to update store name.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // No store yet, create one
                    Store newStore = new Store(newStoreName);
                    newStore.owner = currentUserID;
                    model.postStore(newStore, (Boolean created) -> {
                        if (created) {
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to create store.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else {
            // Only owner name was updated
            Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}

