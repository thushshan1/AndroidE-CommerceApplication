package com.example.b07group19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07group19.models.Cart;
import com.example.b07group19.models.UserCart;

public class ShoppingCart extends AppCompatActivity {
    RecyclerView recyclerView;
    ShoppingCartAdaptor adaptor;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        recyclerView = (RecyclerView)findViewById(R.id.rv_store);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button checkout=(Button)findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: generate order from cart
                if (!UserCart.getCart().getOrderList().isEmpty()) {
                    Model.getInstance().saveOrders(UserCart.getCart());
                    Toast.makeText(ShoppingCart.this, "Order created!", Toast.LENGTH_LONG).show();

                    //clear cart of current user;
                    UserCart.clearCart();
                    startActivity(new Intent(ShoppingCart.this, CustomerDashboardActivity.class));

                } else {
                    Toast.makeText(ShoppingCart.this, "Cart is empty!Fail to check out", Toast.LENGTH_LONG).show();
                }

            }
        });

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShoppingCart.this, CustomerStoreView2.class));
            }
        });
        Cart cart=UserCart.getCart();
        UserCart.setOrderList();
//        List<Cart.OrderDataStore> orderdata = new ArrayList<Cart.OrderDataStore>();
//        Cart.OrderDataStore order = new Cart.OrderDataStore();
//        order.setStoreName("test");
//
//        Cart.OrderDataStore.orderDetail detail = new Cart.OrderDataStore.orderDetail();
//        detail.setBrand("abc");
//        detail.setProductName("name");
//        detail.setCount("100");
//        detail.setPurl("https://www.google.com/imgres?imgurl=https%3A%2F%2Fimages.pexels.com%2Fphotos%2F90946%2Fpexels-photo-90946.jpeg%3Fcs%3Dsrgb%26dl%3Dpexels-math-90946.jpg%26fm%3Djpg&tbnid=Wmnu3T05GveSeM&vet=12ahUKEwi18-Ti88aAAxXch-4BHb6EDlUQMygAegUIARDxAQ..i&imgrefurl=https%3A%2F%2Fwww.pexels.com%2Fsearch%2Fproduct%2F&docid=nMjIODhQZ_AKUM&w=4314&h=2857&q=product%20pic&ved=2ahUKEwi18-Ti88aAAxXch-4BHb6EDlUQMygAegUIARDxAQ");
//        detail.setPrice("12");
//        order.getCartlist().add(detail);

//        orderdata.add(order);
        adaptor = new ShoppingCartAdaptor(cart.getOrderList());
        recyclerView.setAdapter(adaptor);

    }

}