package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class addproduct extends AppCompatActivity {
    private String categoryname;
    private EditText productname,productprice,productdetails;
    private ImageView productimage;
    private Button add,addpic;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri,displaypic;
    private String name,sellermail,price,details,productid,currentdate,currenttime;
    StorageReference productimageref;
    SharedPreferences pref;
    LinearLayout myLayout;
    ArrayList<Uri> images;
    DatabaseReference productref;
    HashMap<String,Object> image;
    ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        categoryname=getIntent().getExtras().get("category").toString();
        images=new ArrayList<>();
        image=new HashMap<>();
        productdetails=(EditText)findViewById(R.id.product_details);
        productname=(EditText)findViewById(R.id.product_name);
        productprice=(EditText)findViewById(R.id.product_price);
        add=(Button)findViewById(R.id.add_product_button);
        addpic=findViewById(R.id.add_pic_button);
        productimage=(ImageView)findViewById(R.id.select_photo);
        pref=getApplicationContext().getSharedPreferences("currentuser",MODE_PRIVATE);
        sellermail= pref.getString("usermail","");
        productimageref= FirebaseStorage.getInstance().getReference().child("Product Image");
        productref= FirebaseDatabase.getInstance().getReference().child("Products");
        loadingbar=new ProgressDialog(this);
        myLayout = (LinearLayout)findViewById(R.id.add_product_pic_layout);
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateproduct();
            }
        });
        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery2();
            }
        });
    }

    private void validateproduct() {
        name=productname.getText().toString();
        price=productprice.getText().toString();
        details=productdetails.getText().toString();
        if(imageUri==null)
        {
            Toast.makeText(addproduct.this,"Product Image Required",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(addproduct.this,"Product Name Required",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(addproduct.this,"Product price Required",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(details))
        {
            Toast.makeText(addproduct.this,"Product Details Required",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Product Uploading");
            loadingbar.setMessage("Wait for a few moment when product is uploading");
            loadingbar.show();
            addthisproduct();
        }

    }

    private void addthisproduct() {
        Calendar time= Calendar.getInstance();
        SimpleDateFormat date= new SimpleDateFormat("dd:MM:yyyy");
        currentdate=date.format(time.getTime());
        SimpleDateFormat Time= new SimpleDateFormat("hh:mm:ss");
        currenttime=Time.format(time.getTime());
        productid=currenttime+"_"+currentdate+"_"+sellermail;
        for(int i=0;i<images.size();i++) {
            final String j=String.valueOf(i);
            final StorageReference filename = productimageref.child(productid+"_"+String.valueOf(i)+ ".jpg");
            final UploadTask uploadTask = filename.putFile(images.get(i));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    loadingbar.dismiss();
                    Toast.makeText(addproduct.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image.put(j,uri.toString());
                            if(j.equals(String.valueOf(image.size()-1))){
                                saveproductinformation();
                            }
                        }
                    });

                }
            });
        }
    }

    private void saveproductinformation() {
        System.out.println("upload");
        HashMap<String,Object> productinformation=new HashMap<>();
        productinformation.put("id",productid);
        productinformation.put("Date",currentdate);
        productinformation.put("Time",currenttime);
        productinformation.put("Name",name);
        productinformation.put("Image",image.get("0"));
        productinformation.put("Price",price);
        productinformation.put("Details",details);
        productinformation.put("Category",categoryname);
        productinformation.put("state","available");
        productinformation.put("seller",sellermail);

        productref.child(productid).updateChildren(productinformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    productref.child(productid).child("Images").updateChildren(image);
                    FirebaseDatabase.getInstance().getReference().child("Customer").child(sellermail).child("myproduct").child(currenttime+"_"+currentdate).child("p_id").setValue(productid);
                    //FirebaseDatabase.getInstance().getReference().child("Customer").child(sellermail).child("myproduct").child(currenttime+"_"+currentdate).child("p_id").setValue();
                    Toast.makeText(addproduct.this,"Product Added Successfully",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Intent intent=new Intent(addproduct.this, seller.class);
                    startActivity(intent);
                }
                else
                {
                    loadingbar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(addproduct.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 2885);
    }
    private void openGallery2() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 2855);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2885){
            imageUri = data.getData();
            productimage.setImageURI(imageUri);
            images.add(0,imageUri);
        }
        if (resultCode == RESULT_OK && requestCode == 2855){
            displaypic = data.getData();
            images.add(displaypic);
            addimage(displaypic);

        }
    }
    private void addimage(Uri uri){
        ImageView imagebyCode = new ImageView(this);
        imagebyCode.setImageURI(uri);
        LinearLayout.LayoutParams params =  new LinearLayout
                .LayoutParams(Integer.parseInt("250"), Integer.parseInt("250"));
        imagebyCode.setLayoutParams(params);
        myLayout.addView(imagebyCode);
    }

}