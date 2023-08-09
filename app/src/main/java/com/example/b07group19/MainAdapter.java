package com.example.b07group19;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Products,MainAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String storeName;
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Products> options, String storeName) {
        super(options);
        this.storeName = storeName;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Products model) {
        holder.text1.setText(model.getProduct());
        holder.text2.setText(model.getPrice());
        holder.text3.setText(model.getBrand());

        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder((R.layout.editor)))
                        .setExpanded(true, 2400)
                        .create();

                dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText name = view1.findViewById(R.id.txt);
                EditText price = view1.findViewById(R.id.txt1);
                EditText brand = view1.findViewById(R.id.txt2);
                EditText length = view1.findViewById(R.id.txt3);
                EditText width = view1.findViewById(R.id.txt4);
                EditText height = view1.findViewById(R.id.txt5);
                EditText quantity = view1.findViewById(R.id.txt6);
                EditText url = view1.findViewById(R.id.txt7);

                Button btnUpdate = view1.findViewById(R.id.btnUpdate);

                name.setText(model.getProduct());
                price.setText(model.getPrice());
                brand.setText(model.getBrand());
                length.setText(model.getLength());
                width.setText(model.getWidth());
                height.setText(model.getHeight());
                quantity.setText(model.getQuantity());
                url.setText(model.getTurl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("product", name.getText().toString());
                        map.put("price", price.getText().toString());
                        map.put("brand", brand.getText().toString());
                        map.put("length", length.getText().toString());
                        map.put("width", width.getText().toString());
                        map.put("height", height.getText().toString());
                        map.put("quantity", quantity.getText().toString());
                        map.put("turl", url.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("stores")
                                .child(StoreDashboardActivity.storeName)
                                .child("products")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>(){
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.text1.getContext(),"Updated", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.text1.getContext(),"Error", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.text1.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can not be retrieved");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Model.getInstance().deleterProductItem(storeName,position);

//                        FirebaseDatabase.getInstance().getReference().child("stores")
//                                .child(StoreDashboardActivity.storeName)
//                                .child("products")
//                                .child(getRef(position).getKey()).removeValue();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(holder.text1.getContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView text1;
        TextView text2;
        TextView text3;

        Button btnEdit;
        Button btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById((R.id.img1));
            text1 = (TextView) itemView.findViewById((R.id.nametext));
            text2 = (TextView) itemView.findViewById((R.id.Price));
            text3 = (TextView) itemView.findViewById((R.id.Brand));

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);
        }
    }
}
