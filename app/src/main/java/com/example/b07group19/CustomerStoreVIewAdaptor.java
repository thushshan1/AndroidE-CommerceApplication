package com.example.b07group19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerStoreVIewAdaptor extends FirebaseRecyclerAdapter<Products, CustomerStoreVIewAdaptor.myViewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CustomerStoreVIewAdaptor(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Products model) {
        holder.pro.setText(model.getProduct());
        holder.price.setText(model.getPrice());
        holder.brand.setText(model.getBrand());
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_with_cart,parent,false);

        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView pro;

        TextView brand,price;
        TextView count;
        TextView inc,desc;

        Button btnAdd;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            pro = (TextView)itemView.findViewById(R.id.nametext);
            brand = (TextView)itemView.findViewById(R.id.Brand);
            price = (TextView)itemView.findViewById(R.id.Price);
            count = (TextView)itemView.findViewById(R.id.Count);
            inc = (TextView)itemView.findViewById(R.id.tv_increase_goods_num);
            desc = (TextView)itemView.findViewById(R.id.tv_reduce_goods_num);
            btnAdd = (Button) itemView.findViewById(R.id.CountAdd);
        }
    }

}
