package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.data.SingleRefDataBufferIterator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registrationactivity extends AppCompatActivity {
    private Button registers;
    private EditText Name,remail,phone,hallname,roomno,setpass;
    private ProgressDialog loadingbar;
    private String from;
    SharedPreferences pref;
    private String mail;
    private String id,name,phn,hall,room,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        from=getIntent().getStringExtra("from");
        registers=(Button) findViewById(R.id.register);
        Name=(EditText)findViewById(R.id.name);
        remail=(EditText)findViewById(R.id.remail);
        phone=(EditText)findViewById(R.id.rphonenumber);
        hallname=(EditText)findViewById(R.id.rhall);
        roomno=(EditText)findViewById(R.id.rroom);
        setpass=(EditText)findViewById(R.id.rpass);
        loadingbar=new ProgressDialog(this);
        if(from.equals("home")){
            update_profile();
        }
        registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatacctount();
            }
        });
    }

    private void update_profile(){
        pref=getApplicationContext().getSharedPreferences("currentuser",MODE_PRIVATE);
        mail= pref.getString("usermail","");
        remail.setVisibility(View.GONE);
        registers.setText("Update Profile");
        FirebaseDatabase.getInstance().getReference().child("Customer").child(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name.setText(snapshot.child("name").getValue().toString());
                phone.setText(snapshot.child("phone").getValue().toString());
                setpass.setText(snapshot.child("pass").getValue().toString());
                hallname.setText(snapshot.child("hallname").getValue().toString());
                roomno.setText(snapshot.child("roomno").getValue().toString());
                remail.setText(mail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void creatacctount()
    {
        name = Name.getText().toString();
        id= remail.getText().toString();
        phn= phone.getText().toString();
        hall= hallname.getText().toString();
        room=roomno.getText().toString();
        pass= setpass.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Enter Your name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phn))
        {
            Toast.makeText(this,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(id))
        {
            Toast.makeText(this,"Please Enter Varsity Id",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(hall))
        {
            Toast.makeText(this,"Please Enter Your Hall Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(room))
        {
            Toast.makeText(this,"Please Enter Your Room No",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Checking All credentials , Please Wait");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            resister(id);

        }

    }
    private void resister(String iid)
    {
        final DatabaseReference ref;
        final String mail= iid;
        ref= FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Customer").child(mail).exists()))
                {
                        validatemail("u"+mail+"@student.cuet.ac.bd");
                }
                else if(from.equals("home")){
                    validatemail("u"+mail+"@student.cuet.ac.bd");
                }
                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(registrationactivity.this,"You have already created an account.Log into your account",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(registrationactivity.this, registrationactivity.class);
                    startActivity(intent);
                }            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingbar.dismiss();
                Toast.makeText(registrationactivity.this,"Account has not been registered",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void validatemail(String mail){

        Intent a=new Intent(registrationactivity.this,verification.class);
        a.putExtra("mail",mail);
        startActivityForResult(a,2855);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2855)
        {
            if(resultCode==2888)
            {
                if(data.getStringExtra("result").equals("matched")) {
                    HashMap<String, Object> datas = new HashMap<>();
                    datas.put("name", name);
                    datas.put("phone", phn);
                    datas.put("pass", pass);
                    datas.put("roomno", room);
                    datas.put("hallname", hall);
                    datas.put("id", id);
                    FirebaseDatabase.getInstance().getReference().child("Customer").child(id).updateChildren(datas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingbar.dismiss();
                                if(from.equals("home")){
                                    new AlertDialog.Builder(registrationactivity.this).setTitle("Successful").setMessage("Your Account has been updated successfully").setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();
                                }
                                else {
                                    new AlertDialog.Builder(registrationactivity.this).setTitle("Successful").setMessage("Your Account has been created successfully").setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(registrationactivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).show();
                                }

                            } else {
                                loadingbar.dismiss();
                                Toast.makeText(registrationactivity.this, "Network Error: Plase try agian", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(registrationactivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{

                }

            }
        }
    }
}