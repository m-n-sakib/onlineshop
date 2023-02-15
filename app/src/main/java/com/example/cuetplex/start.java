package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class start extends AppCompatActivity {
    SharedPreferences pref;
    private String mail;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        pref=getApplicationContext().getSharedPreferences("currentuser",MODE_PRIVATE);
        mail= pref.getString("usermail","");
        password=pref.getString("userpass","");
        if(!(mail.equals("")))
        {
            final DatabaseReference ref;
            ref= FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if((snapshot.child("Customer").child(mail).exists()))
                    {
                        if((snapshot.child("Customer").child(mail).child("pass").getValue()).equals(password))
                        {
                            Intent intent=new Intent(start.this, home.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(start.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent=new Intent(start.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Intent intent=new Intent(start.this, MainActivity.class);
                    startActivity(intent);

                }
            });
        }
        else
        {
            Intent intent=new Intent(start.this, MainActivity.class);
            startActivity(intent);
        }
    }
}