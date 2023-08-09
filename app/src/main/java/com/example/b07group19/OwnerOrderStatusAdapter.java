package com.example.b07group19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.b07group19.models.Order;

import java.util.List;
import java.util.Map;

public class OwnerOrderStatusAdapter extends ArrayAdapter<Order> {

    private Context context;
    private int resource;
    Map<String,String> userNames;

    public OwnerOrderStatusAdapter(Context context, int resource, List<Order> objects,Map<String,String> userNames){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.userNames = userNames;

    }
    private class ViewHolder {
        TextView tvUserName;
        TextView tvCreateDate;
        TextView tvNumberOfItems;
    }
    @NonNull
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        Order order = getItem(position);

        String userID = order.userID;
        String createdData = order.createDate;
        String numberOfItems = ((Integer) order.items.size()).toString();

        ViewHolder holder;
        if(converView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            converView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tvUserName = (TextView) converView.findViewById(R.id.tvStoreName);
            holder.tvCreateDate = (TextView) converView.findViewById(R.id.tvCreatedDate);
            holder.tvNumberOfItems = (TextView) converView.findViewById(R.id.tvNumberOfItems);

            converView.setTag(holder);

        }else{
            holder = (ViewHolder) converView.getTag();
        }

        holder.tvUserName.setText(userNames.get(userID));
        holder.tvCreateDate.setText(createdData);
        holder.tvNumberOfItems.setText(numberOfItems);

        return converView;
    }
}