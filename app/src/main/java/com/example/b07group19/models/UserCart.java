package com.example.b07group19.models;

import android.widget.Toast;

import com.example.b07group19.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserCart {
    private static Map<String, Cart> carts = new HashMap<String, Cart>();
    private static String currentUser;

    private String count;
    public static void clearCart()
    {
        carts.get(currentUser).clear();
    }
    public static Cart getCart(String userid)
    {
        currentUser=userid;
        if (carts.get(userid)==null)
        {
            Cart cart= new Cart();
            carts.put(userid,cart);
        }
        return carts.get(userid);
    }
    public static Cart getCart()
    {
        return carts.get(currentUser);
    }
    public static void setupStoreCart(String storeName)
    {
        Cart cart= UserCart.getCart();
        if (cart.getOrderMap().get(storeName)==null)
        {
            Order store = new Order();
            cart.getOrderList().add(store);
            cart.getOrderMap().put(storeName,store);
        }
    }
    public static void addItem(String storeName, OrderItem orderItem)
    {
        Cart cart= UserCart.getCart();
        if (cart.getOrderMap().get(storeName)==null)
        {
            Order order = new Order();
            order.setStoreName(storeName);
            cart.getOrderList().add(order);
            cart.getOrderMap().put(storeName,order);
        }
        Order order=cart.getOrderMap().get(storeName);
        order.setStoreName(storeName);
        order.getItems().add(orderItem);

    }
    public static int getCount( String count) {
        int num = Integer.parseInt(count);
        return num;
    }

    public static void setOrderList()
    {
        Cart cart= getCart();
        Set entrySet=cart.getOrderMap().entrySet();
        Iterator it=entrySet.iterator();
        List<Order> list=new ArrayList<Order>();
        while(it.hasNext()){
            Map.Entry entry=(Map.Entry)(it.next());
            list.add((Order) entry.getValue());
        }
        cart.setOrderList(list);
    }
}
