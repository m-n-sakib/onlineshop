package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class loginactivity extends AppCompatActivity {
    SharedPreferences pref;
    public SharedPreferences.Editor edit;
    private Button login;
    private EditText email, pass;
    private String mail,password;
    private ProgressDialog loadingbar;
    private CheckBox remember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button) findViewById(R.id.login2);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.password);
        remember=(CheckBox)findViewById(R.id.rememberme);
        loadingbar=new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = (email.getText().toString()).replace(".", ",");
                password = pass.getText().toString();
                if (mail.isEmpty()) {
                    Toast.makeText(loginactivity.this, "Enter mail", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(loginactivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else{
                    loadingbar.setTitle("Logging Account");
                loadingbar.setMessage("Checking Account, Please wait");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                final DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference();


                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((snapshot.child("Customer").child(mail).exists())) {
                            if ((snapshot.child("Customer").child(mail).child("pass").getValue()).equals(password)) {
                                if (remember.isChecked()) {
                                    pref = getApplicationContext().getSharedPreferences("currentuser", MODE_PRIVATE);
                                    edit = pref.edit();
                                    edit.putString("usermail", mail);
                                    edit.putString("userpass", password);
                                    edit.commit();
                                }
                                loadingbar.dismiss();
                                Toast.makeText(loginactivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(loginactivity.this, home.class);
                                startActivity(intent);
                            } else {
                                loadingbar.dismiss();
                                Toast.makeText(loginactivity.this, "Password Didn't match, try again", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(loginactivity.this, loginactivity.class);
                                startActivity(intent);
                            }
                        } else {
                            loadingbar.dismiss();
                            Toast.makeText(loginactivity.this, "No account found, Create one", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginactivity.this, loginactivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingbar.dismiss();
                        Toast.makeText(loginactivity.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(loginactivity.this, loginactivity.class);
                        startActivity(intent);

                    }
                });
            }
            }
        });
    }
}