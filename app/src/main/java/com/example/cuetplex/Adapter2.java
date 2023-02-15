package com.example.cuetplex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Adapter2 extends FirebaseRecyclerAdapter<product_show_data_model_2, Adapter2.ViewHolder> {


    private OnItemClick2 itemClick;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_product_item,parent,false);
        return new Adapter2.ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final product_show_data_model_2 model) {
        FirebaseDatabase.getInstance().getReference().child("Products").child(model.getP_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.pname.setText(snapshot.child("Name").getValue().toString());
                holder.pprice.setText("Price:"+snapshot.child("Price").getValue().toString());
                Picasso.get().load(snapshot.child("Image").getValue().toString()).into(holder.productimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.OnitemClick2(model);
            }
        });

    }

    public Adapter2(FirebaseRecyclerOptions<product_show_data_model_2> product_list) {
        super(product_list);
    }
    public void setOnItemClickListener(OnItemClick2 itemClick)
    {
        this.itemClick=itemClick;
    }


     class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productimage;
        TextView pname,pprice;
        public ViewHolder(@NonNull final View itemView) {

            super(itemView);

            productimage=itemView.findViewById(R.id.product_image);
            pname=itemView.findViewById(R.id.product_name);
            pprice=itemView.findViewById(R.id.product_price);

        }
    }
}
