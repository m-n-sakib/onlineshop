package com.example.cuetplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cuetplex.ui.gallery.GalleryFragment;
import com.example.cuetplex.ui.home.HomeFragment;
import com.example.cuetplex.ui.slideshow.SlideshowFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rclview;
    ActionBarDrawerToggle toggle;
    DatabaseReference productref;
    Adapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navview=findViewById(R.id.nav_view);


        rclview=(RecyclerView)findViewById(R.id.product_show_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclview.setLayoutManager(layoutManager);
        productref= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<product_show_data_model> options
                = new FirebaseRecyclerOptions.Builder<product_show_data_model>()
                .setQuery(productref.orderByKey(), product_show_data_model.class)
                .build();
        adapter = new Adapter(options);
        rclview.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClick() {
            @Override
            public void OnitemClick(product_show_data_model model) {
                Intent intent=new Intent(home.this,showproduct.class);
                intent.putExtra("name",model.getId());
                startActivity(intent);
            }
        });

        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                switch (id){
                    case R.id.nav_logout:
                        pref = getApplicationContext().getSharedPreferences("currentuser", MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putString("usermail", "");
                        edit.putString("userpass", "");
                        edit.commit();
                        Intent intent=new Intent(home.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_cart:
                        Intent intent3=new Intent(home.this, mycart.class);
                        intent3.putExtra("from","cart");
                        startActivity(intent3);
                        break;
                    case R.id.nav_order:
                        Intent intent6=new Intent(home.this, mycart.class);
                        intent6.putExtra("from","cart");
                        startActivity(intent6);
                        break;
                    case R.id.nav_sellproduct:
                       Intent intent2=new Intent(home.this,seller.class);
                       startActivity(intent2);
                       break;
                    case R.id.nav_myproduct:
                        Intent intent5=new Intent(home.this,mycart.class);
                        intent5.putExtra("from","my product");
                        startActivity(intent5);
                        break;
                    case R.id.nav_profile:
                        Intent intent4=new Intent(home.this,registrationactivity.class);
                        intent4.putExtra("from","home");
                        startActivity(intent4);
                        break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    });


}
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override protected void onStop()
    {
        super.onStop();
        if (adapter!= null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}