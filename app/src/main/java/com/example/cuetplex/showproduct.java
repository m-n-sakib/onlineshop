package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class showproduct extends AppCompatActivity {
    private TextView name,price,details;
    private Button cart;
    private ImageView image;
    private Toolbar toolbar;
    String state,requirement;
    SharedPreferences pref;
    private String mail,seller;
    DatabaseReference dataref;
    LinearLayout.LayoutParams params;
    LinearLayout myLayout,r_layout;
    ArrayList<String>images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showproduct);
        toolbar=(Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        dataref=FirebaseDatabase.getInstance().getReference().child("Products").child(getIntent().getExtras().get("name").toString());
        price=(TextView)findViewById(R.id.productprice);
        name=(TextView)findViewById(R.id.productname);
        details=(TextView)findViewById(R.id.productdetails);
        image=(ImageView)findViewById(R.id.productimage);
        cart=findViewById(R.id.add_to_cart);
        images=new ArrayList<>();
        state="show";
        pref=getApplicationContext().getSharedPreferences("currentuser",MODE_PRIVATE);
        mail= pref.getString("usermail","");
        r_layout=findViewById(R.id.requirement_layout);
        r_layout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        params =  new LinearLayout
                .LayoutParams(Integer.parseInt("250"), Integer.parseInt("250"));
        myLayout = (LinearLayout)findViewById(R.id.show_product_pic_layout);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r_layout.setVisibility(View.VISIBLE);
                cart.setText("Confirm Buying");

                if(state.equals("confirm")){
                    requirement=((EditText)findViewById(R.id.requirement)).getText().toString();
                    if(requirement.isEmpty()){
                        Toast.makeText(showproduct.this,"Requirement Field must be filled up",Toast.LENGTH_LONG).show();
                    }
                    else{
                        new AlertDialog.Builder(showproduct.this).setTitle("Confirm?").setMessage("Are you sure to buy this product?You will not be able to cancel if you confirm buying.").setCancelable(false)
                                .setPositiveButton("YES,I'm sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Calendar time= Calendar.getInstance();
                                        SimpleDateFormat date= new SimpleDateFormat("dd:MM:yyyy");
                                        String currentdate = date.format(time.getTime());
                                        SimpleDateFormat Time= new SimpleDateFormat("hh:mm:ss");
                                        String currenttime = Time.format(time.getTime());
                                        HashMap<String,Object> data=new HashMap<>();
                                        data.put("p_id",getIntent().getExtras().get("name").toString());
                                        data.put("p_requirement",requirement);
                                        data.put("buyer",mail);
                                        data.put("seller",seller);
                                        FirebaseDatabase.getInstance().getReference().child("order").push().updateChildren(data);
                                        Toast.makeText(showproduct.this,"Product is add to your cart.",Toast.LENGTH_LONG).show();
                                        state="show";
                                        r_layout.setVisibility(View.GONE);
                                        cart.setText("Buy this Product");
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
                state="confirm";
            }
        });

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                name.setText("Product Name: "+snapshot.child("Name").getValue().toString());
                price.setText("Price: "+snapshot.child("Price").getValue().toString()+"Tk");
                details.setText(snapshot.child("Details").getValue().toString());
                seller=snapshot.child("seller").getValue().toString();
                Picasso.get().load(snapshot.child("Image").getValue().toString()).into(image);
                int i=0;
                for (i=0;i<snapshot.child("Images").getChildrenCount();i++)
                {
                    final String p=snapshot.child("Images").child(String.valueOf(i)).getValue().toString();
                    images.add(p);
                    ImageView imagebyCode = new ImageView(showproduct.this);
                    Picasso.get().load(p).into(imagebyCode);
                    imagebyCode.setLayoutParams(params);
                    myLayout.addView(imagebyCode);
                    imagebyCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Picasso.get().load(p).into(image);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}