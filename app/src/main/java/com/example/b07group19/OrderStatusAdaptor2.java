package com.example.b07group19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.b07group19.models.Order;
import com.example.b07group19.models.OrderItem;
import com.example.b07group19.models.OrderStatus;

import java.util.Collections;
import java.util.List;

public class OrderStatusAdaptor2 extends ArrayAdapter<List<OrderItem> > {

    private Context context;
    private int resource;
    Order order;


    public OrderStatusAdaptor2(@NonNull Context context, int resource, @NonNull Order order) {
        super(context, resource, Collections.singletonList(order.getItems()));
        this.context = context;
        this.resource = resource;
        this.order = order;
    }

    private class ViewHolder {
        TextView prod;
        TextView pri;
        TextView quan;
    }

    @Override
    public int getCount() {
        return order.items.size();
    }

    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        OrderItem orderItem = this.order.items.get(position);

        String product = String.valueOf(orderItem.getProductName());
        String price = String.valueOf(orderItem.getPrice());
        String quantity = String.valueOf(orderItem.getCount());

        ViewHolder holder;
        if(converView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.prod = (TextView) converView.findViewById(R.id.prod);
            holder.pri = (TextView) converView.findViewById(R.id.Pri);
            holder.quan = (TextView) converView.findViewById(R.id.Quan);

            converView.setTag(holder);

        }else{
            holder = (ViewHolder) converView.getTag();
        }

        holder.prod.setText(product);
        holder.pri.setText(price);
        holder.quan.setText(quantity);

        return converView;
    }
}
