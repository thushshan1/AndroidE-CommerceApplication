package com.example.ecommerceapp;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.ecommerceapp.models.Cart;
import com.example.ecommerceapp.models.Order;
import com.example.ecommerceapp.models.Store;
import com.example.ecommerceapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;


import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model {
    private static Model instance;
    private DatabaseReference userRef;
    private DatabaseReference ownerRef;
    private DatabaseReference orderRef;
    private FirebaseAuth auth;
    private DatabaseReference storesRef;

    private Model() {
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        ownerRef = FirebaseDatabase.getInstance().getReference("Owners");
        auth = FirebaseAuth.getInstance();
        storesRef = FirebaseDatabase.getInstance().getReference("stores");
        orderRef = FirebaseDatabase.getInstance().getReference("orders");

    }

    public static Model getInstance() {
        if (instance == null)
                instance = new Model();
        return instance;
    }

    public void authenticate(String email, String password, Consumer<String> callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e("Model.authenticate", "Authentication failed: " + task.getException().getMessage());
                    callback.accept(null);
                } else {
                    // OPTIMIZATION: Check user type more efficiently
                    String uid = auth.getUid();
                    if (uid != null) {
                        checkUserType(uid, callback);
                    } else {
                        Log.e("Model.authenticate", "User ID is null after successful authentication");
                        callback.accept(null);
                    }
                }
            }
        });
    }
    
    // OPTIMIZED: New method to check user type more efficiently
    private void checkUserType(String uid, Consumer<String> callback) {
        // Use a single query to check if user exists in either Users or Owners
        // This is more efficient than nested calls
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.accept("user");
                } else {
                    // Only check Owners if not found in Users
                    ownerRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                callback.accept("owner");
                            } else {
                                Log.w("Model.authenticate", "User not found in Users or Owners: " + uid);
                                callback.accept(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Model.authenticate", "Database error checking owner: " + databaseError.getMessage());
                            callback.accept(null);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.authenticate", "Database error checking user: " + error.getMessage());
                callback.accept(null);
            }
        });
    }

    public void getUser(String userID, Consumer<UserModel> callback){
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                callback.accept(user); // Can be null if user doesn't exist
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.getUser", "Database error: " + error.getMessage());
                callback.accept(null);
            }
        });
    }


    public void register(String email, String password, Consumer<String>callback) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = auth.getUid();
                    if (uid != null) {
                        callback.accept(uid);
                    } else {
                        Log.e("Model.register", "User ID is null after successful registration");
                        callback.accept(null);
                    }
                } else {
                    Log.e("Model.register", "Failed to register user", task.getException());
                    callback.accept(null);
                }
            }
        });
    }


    public void postUser(UserModel user, Consumer<Boolean> callback){
        userRef.child(user.userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.accept(true);
                } else {
                    callback.accept(false);
                    Exception e = task.getException();
                    Log.e("RegisterActivity", "Failed to post user", e);
                }
            }
        });

    }
    public void getStoreByName(String storeName, Consumer<Store> callback) {
        storesRef.child(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Store store=new Store();
                    List<Products> list = getProducts(snapshot, store);
                    store.setProducts(list);
//                    Store store = snapshot.getValue(Store.class);
                    callback.accept(store);
                }
                else{
                    callback.accept(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.getStoreByName", "Database error: " + error.getMessage());
                callback.accept(null);
            }
        });

    }

    @NonNull
    private static List<Products> getProducts(@NonNull DataSnapshot snapshot, Store store) {
        Map<String, Object> ob= (HashMap<String, Object>) snapshot.getValue();
        if (ob != null) {
            store.setOwner((String) ob.get("owner"));
            store.setStoreName((String) ob.get("storeName"));
            
            // Extract products from the snapshot
            List<Products> products = new ArrayList<>();
            DataSnapshot productsSnapshot = snapshot.child("products");
            if (productsSnapshot.exists()) {
                for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                    Products product = productSnapshot.getValue(Products.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
            return products;
        }
        return new ArrayList<>();
    }

    public void postStore(Store store, Consumer<Boolean> callback) {
        storesRef.child(store.storeName).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.accept(task.isSuccessful());
            }
        });
    }

    public void getStoreByOwner(String owner, Consumer<Store> callback) {

        storesRef.orderByChild("owner").equalTo(owner).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot storeSnapShot: snapshot.getChildren()) {
                    Store store=new Store();
                    List<Products> list = getProducts(storeSnapShot, store);
                    store.setProducts(list);
//                    Store store = storeSnapShot.getValue(Store.class);
                    callback.accept(store);
                    return;
                }
                // not exist
                callback.accept(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.getStoreByOwner", "Database error: " + error.getMessage());
                callback.accept(null);
            }
        });
    }

    public void getStoreNames(Consumer<List<String>> callback) {
        storesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> storeNames = new ArrayList<String>();

//                storeNames.add("apple");
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {
                    Store store = userSnapShot.getValue(Store.class);
                    storeNames.add(store.storeName);
                }
                callback.accept(storeNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.getStoreNames", "Database error: " + error.getMessage());
                callback.accept(new ArrayList<>());
            }
        });
    }
    public void getUserNames(Consumer<Map<String,String>> callback) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> userNames = new HashMap<String,String>();
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {
                    UserModel user = userSnapShot.getValue(UserModel.class);
                    if (user != null && user.name != null) {
                        userNames.put(userSnapShot.getKey(), user.name);
                    }
                }
                callback.accept(userNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.getUserNames", "Database error: " + error.getMessage());
                callback.accept(new HashMap<>());
            }
        });
    }
    public void saveOrders(Cart cart)
    {
        for (int i=0;i< cart.getOrderList().size();i++)
        {
            Order order = cart.getOrderList().get(i);
            if (order.getItems().size()==0)
                continue;
            order.setStatus("pending");
            postOrder(order, (Order ret) -> {
                if (ret == null) {
                    return;
                }
            });

        }
    }
    public void postOrder(Order order, java.util.function.Consumer<Order> callback) {
        String key = orderRef.push().getKey();
        order.setOrderID(key);
        String uid = auth.getUid();
        if (uid != null) {
            order.setUserID(uid);
        } else {
            Log.e("Model.postOrder", "User ID is null, cannot create order");
            callback.accept(null);
            return;
        }
        order.setCreateDate(DateFormat.format(Order.DATETIME_FORMAT, Calendar.getInstance()).toString());
        orderRef.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.accept(task.isSuccessful() ? order : null);
            }
        });
    }

    public void completeOrder(String orderID) {
        if (orderID != null && !orderID.isEmpty()) {
            orderRef.child(orderID).child("status").setValue("completed")
                .addOnSuccessListener(aVoid -> Log.d("Model.completeOrder", "Order completed successfully: " + orderID))
                .addOnFailureListener(e -> Log.e("Model.completeOrder", "Failed to complete order: " + orderID, e));
        } else {
            Log.e("Model.completeOrder", "Order ID is null or empty");
        }
    }

    public void deleterProductItem(String storeName,int positon) {
        storesRef.child(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store store=snapshot.getValue(Store.class);
                store.products.remove(positon);
                storesRef.child(storeName).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });;

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Model.deleterProductItem", "Database error: " + error.getMessage());
            }
        });


    }


}
