package com.example.cuetplex;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class mycart extends AppCompatActivity {
    RecyclerView rclview;
    ActionBarDrawerToggle toggle;
    DatabaseReference productref;
    Adapter2 adapter2;
    Adapter adapter;
    String mail,from;
    Query q;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);
        pref=getApplicationContext().getSharedPreferences("currentuser",MODE_PRIVATE);
        mail= pref.getString("usermail","");
        from=getIntent().getStringExtra("from");
        rclview=(RecyclerView)findViewById(R.id.my_cart_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclview.setLayoutManager(layoutManager);
        if(from.equals("cart")) {
            q = FirebaseDatabase.getInstance().getReference().child("order").orderByChild("buyer").equalTo(mail);
            FirebaseRecyclerOptions<product_show_data_model_2> options
                    = new FirebaseRecyclerOptions.Builder<product_show_data_model_2>()
                    .setQuery(q, product_show_data_model_2.class)
                    .build();
            adapter2 = new Adapter2(options);
            rclview.setAdapter(adapter2);

            adapter2.setOnItemClickListener(new OnItemClick2() {
                @Override
                public void OnitemClick2(product_show_data_model_2 model) {

                }
            });
        }
        else if(from.equals("my product")) {
            q = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("seller").equalTo(mail);
            FirebaseRecyclerOptions<product_show_data_model> options
                    = new FirebaseRecyclerOptions.Builder<product_show_data_model>()
                    .setQuery(q, product_show_data_model.class)
                    .build();
            adapter = new Adapter(options);
            rclview.setAdapter(adapter);
        }
        else if(from.equals("order")) {
            q = FirebaseDatabase.getInstance().getReference().child("order").orderByChild("seller").equalTo(mail);
            FirebaseRecyclerOptions<product_show_data_model_2> options
                    = new FirebaseRecyclerOptions.Builder<product_show_data_model_2>()
                    .setQuery(q, product_show_data_model_2.class)
                    .build();
            adapter2 = new Adapter2(options);
            rclview.setAdapter(adapter2);

            adapter2.setOnItemClickListener(new OnItemClick2() {
                @Override
                public void OnitemClick2(product_show_data_model_2 model) {

                }
            });
        }

    }
    @Override protected void onStart()
    {
        super.onStart();
        if(from.equals("cart") || from.equals("order")){
            adapter2.startListening();
        }
        else {
            adapter.startListening();
        }

    }
    @Override protected void onStop()
    {
        super.onStop();
        if (adapter2!= null &&from.equals("cart")) {
            adapter2.stopListening();
        }
        if (adapter!= null) {
            adapter.stopListening();
        }
    }
}