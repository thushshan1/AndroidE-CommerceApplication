package com.example.ecommerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ecommerceapp.models.Order;

import java.util.List;
import java.util.Map;

public class OwnerOrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private int resource;
    Map<String,String> userNames;

    public OwnerOrderAdapter(Context context, int resource, List<Order> objects,Map<String,String>userNames){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.userNames=userNames;


        //Model.getInstance().getUserNames();
    }
    private class ViewHolder {
        TextView tvUserName;
        TextView tvCreateDate;
        TextView tvNumberOfItems;
        Map<String,String> userNames;

        public ViewHolder() {


        }
    }
    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        Order order = getItem(position);

        String userName = userNames.get(order.userID);
        String createdData = order.createDate;
        String numberOfItems = ((Integer) order.items.size()).toString();

        ViewHolder holder;
        if(converView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvUserName = (TextView) converView.findViewById(R.id.tvUserName);
            holder.tvCreateDate = (TextView) converView.findViewById(R.id.tvCreatedDate);
            holder.tvNumberOfItems = (TextView) converView.findViewById(R.id.tvOwnerNumberOfItems);

            converView.setTag(holder);

        }else{
            holder = (ViewHolder) converView.getTag();
        }

        holder.tvUserName.setText(userName);
        holder.tvCreateDate.setText(createdData);
        holder.tvNumberOfItems.setText(numberOfItems);

        return converView;
    }
}
