package com.example.b07group19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter2 extends FirebaseRecyclerAdapter<Products,MainAdapter2.myViewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter2(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Products model) {
        holder.pro.setText(model.getProduct());

        Glide.with(holder.img2.getContext())
                .load(model.getTurl())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img2);
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productview,parent,false);

        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{

        CircleImageView img2;
        TextView pro;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img2 = (CircleImageView)itemView.findViewById(R.id.img2);
            pro = (TextView)itemView.findViewById(R.id.pro);
        }
    }

}
