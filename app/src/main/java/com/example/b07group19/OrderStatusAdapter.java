package com.example.b07group19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.os.Bundle;

import com.example.b07group19.models.Order;

import java.util.List;

public class OrderStatusAdapter extends ArrayAdapter<Order> {

    private Context context;
    private int resource;

    public OrderStatusAdapter(Context context, int resource, List<Order> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

    }
    private class ViewHolder {
        TextView tvStorename;
        TextView tvCreateDate;
        TextView tvNumberOfItems;
    }
    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        Order order = getItem(position);

        String storeName = order.storeName;
        String createdData = order.createDate;
        String numberOfItems = ((Integer) order.items.size()).toString();

        ViewHolder holder;
        if(converView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvStorename = (TextView) converView.findViewById(R.id.tvStoreName);
            holder.tvCreateDate = (TextView) converView.findViewById(R.id.tvCreatedDate);
            holder.tvNumberOfItems = (TextView) converView.findViewById(R.id.tvNumberOfItems);

            converView.setTag(holder);

        }else{
            holder = (ViewHolder) converView.getTag();
        }

        holder.tvStorename.setText(storeName);
        holder.tvCreateDate.setText(createdData);
        holder.tvNumberOfItems.setText(numberOfItems);

        return converView;
    }
}