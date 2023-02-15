package com.example.cuetplex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class verification extends AppCompatActivity {
    String mail,code,input;
    EditText e_input;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mail=getIntent().getStringExtra("mail");
        code=codegen();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SendEmailTLS().send(mail,"Your CUETPlex email verification code is :"+code);
            }
        }).start();
        System.out.println("verification");
        e_input=findViewById(R.id.verification_code);
        ok=findViewById(R.id.verification_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input=e_input.getText().toString();
                if(input.isEmpty())
                {
                    Toast.makeText(verification.this,"Please enter the verification code.",Toast.LENGTH_SHORT).show();
                }
                else if(input.equals(code)){
                    Intent data=new Intent();
                    data.putExtra("result","matched");
                    setResult(2888,data);
                    finish();
                }
                else{
                    new AlertDialog.Builder(verification.this).setTitle("Error").setMessage("Verification code doesn't matched. Try again").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }
    private String codegen(){
        String AlphaNumericString = "ABCDEFGHJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        int n;
        n=8;
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}