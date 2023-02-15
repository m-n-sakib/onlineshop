package com.example.cuetplex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class seller extends AppCompatActivity {
    private ImageView laptop,mobile,hoodie;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        laptop=(ImageView)findViewById(R.id.laptop);
        mobile=(ImageView)findViewById(R.id.mobile);
        hoodie=(ImageView)findViewById(R.id.hoodie);
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller.this,addproduct.class);
                intent.putExtra("category","laptop");
                startActivity(intent);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller.this,addproduct.class);
                intent.putExtra("category","mobile");
                startActivity(intent);
            }
        });
        hoodie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seller.this,addproduct.class);
                intent.putExtra("category","hoodie");
                startActivity(intent);
            }
        });
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void onBackPressed() {
        Intent intent=new Intent(seller.this, home.class);
        startActivity(intent);
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}