package com.example.cuetplex;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import static androidx.core.content.ContextCompat.startActivity;

public class Adapter extends FirebaseRecyclerAdapter<product_show_data_model,Adapter.ViewHolder> {


    private OnItemClick itemClick;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_product_item,parent,false);
        return new Adapter.ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final product_show_data_model model) {
        holder.pname.setText(model.getName());
        holder.pprice.setText("Price:"+model.getPrice());
        Picasso.get().load(model.getImage()).into(holder.productimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.OnitemClick(model);
            }
        });

    }

    public Adapter(FirebaseRecyclerOptions<product_show_data_model> product_list) {
        super(product_list);
    }
    public void setOnItemClickListener(OnItemClick itemClick)
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
