package com.example.b07group19;

import android.text.format.DateFormat;
import android.util.Log;

import com.example.b07group19.models.Cart;
import com.example.b07group19.models.Order;
import com.example.b07group19.models.Store;
import com.example.b07group19.models.UserModel;
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
                    callback.accept(null);
                } else {

                    userRef.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // UserModel user = snapshot.getValue(UserModel.class);
                            if (snapshot.exists())
                                callback.accept( "user");
                            else
                            {
                                // If not found in Users, check in Owners
                                ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // User is an Owner, navigate to StoreDashboard
                                            callback.accept( "owner");
                                        } else {
                                            // User not found in either Users or Owners
                                            callback.accept( null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.accept( null);                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            callback.accept( null);
                        }
                    });
                }
            }
        });
    }

    public void getUser(String userID, Consumer<UserModel> callback){
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                callback.accept(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void register(String email, String password, Consumer<String>callback) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    callback.accept(auth.getUid());
                } else {
                    Log.d("Model.register", "Failed to register user", task.getException());
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @NonNull
    private static List<Products> getProducts(@NonNull DataSnapshot snapshot, Store store) {
//        return null;
        Map<String, Object> ob= (HashMap<String, Object>) snapshot.getValue();
        store.setOwner((String) ob.get("owner"));
        store.setStoreName((String) ob.get("storeName"));
        return null;
//        Map<String, String> products=(HashMap<String, String>)ob.get("products");
//        if (products==null)
//            return null;
//        Set entrySet=products.entrySet();
//        Iterator it=entrySet.iterator();
//        List<Products> list=new ArrayList<>();
//        while(it.hasNext()){
//            Map.Entry entry=(Map.Entry)(it.next());
//            Gson gson = new Gson();
//            String pstring= (String)gson.toJson( entry.getValue());
//            Products value = gson.fromJson(pstring, Products.class);
//            list.add(value);
//        }
//        return list;
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

        storesRef.orderByChild("owner").equalTo(owner).addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getStoreNames(Consumer<List<String>> callback) {
        storesRef.addValueEventListener(new ValueEventListener() {
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void getUserNames(Consumer<Map<String,String>> callback) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> userNames = new HashMap<String,String>();
                for (DataSnapshot userSnapShot: snapshot.getChildren()) {

                    UserModel user = userSnapShot.getValue(UserModel.class);
                    userNames.put(userSnapShot.getKey(),user.name);
                }
                callback.accept(userNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
        order.setUserID(auth.getUid());
        order.setCreateDate(DateFormat.format(Order.DATETIME_FORMAT, Calendar.getInstance()).toString());
        orderRef.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.accept(task.isSuccessful() ? order : null);
            }
        });
    }




}
