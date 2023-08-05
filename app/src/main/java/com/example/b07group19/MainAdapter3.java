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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter3 extends FirebaseRecyclerAdapter<Products,MainAdapter3.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter3(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapter3.myViewholder holder, int position, @NonNull Products model) {

        holder.pro.setText(model.getProduct());
        holder.pri.setText(model.getPrice());
        holder.bran.setText(model.getBrand());
        holder.hei.setText(model.getHeight());
        holder.len.setText(model.getLength());
        holder.qual.setText(model.getQuantity());
        holder.wid.setText(model.getWidth());


        Glide.with(holder.img3.getContext())
                .load(model.getTurl())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img3);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img3.getContext())
                        .setContentHolder(new ViewHolder(R.layout.viewitem))
                        .setExpanded(true, 2400)
                        .create();

                dialogPlus.show();
            }
        });

    }

    @NonNull
    @Override
    public MainAdapter3.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem,parent,false);

        return new myViewholder(view);

    }

    class myViewholder extends RecyclerView.ViewHolder{

        CircleImageView img3;
        TextView pro,pri,bran,hei, len, qual, wid;

        Button view;


        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img3 = (CircleImageView)itemView.findViewById(R.id.img3);
            pro = (TextView)itemView.findViewById(R.id.nametext1);
            pri = (TextView)itemView.findViewById(R.id.Price1);
            bran = (TextView)itemView.findViewById(R.id.Brand1);
            hei = (TextView)itemView.findViewById(R.id.Height1);
            len = (TextView)itemView.findViewById(R.id.Length1);
            qual = (TextView)itemView.findViewById(R.id.Quantity1);
            wid = (TextView)itemView.findViewById(R.id.Width1);

            view = (Button)itemView.findViewById(R.id.view);


        }
    }
}
