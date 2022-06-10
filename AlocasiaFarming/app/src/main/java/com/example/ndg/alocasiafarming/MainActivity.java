package com.example.ndg.alocasiafarming;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ndg.alocasiafarming.Admin.AdminLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin:
                Intent intent = new Intent(this, AdminLogin.class);
                startActivity(intent);
                return true;
        }return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //default fragment ketika di buka
        loadFragment(new HomeFragment());
        //inisiasi bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //listener pada saat item/menu bottomnavigation terpilij
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment fragment= null;

                switch (menuItem.getItemId()){
                    case R.id.home_menu :
                        fragment= new HomeFragment();
                        break;
                    case R.id.histori_menu :
                        fragment= new History();
                        break;
                    case R.id.log:
                        fragment=new LogFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }
    private boolean loadFragment(Fragment fragment){
        if (fragment!=null){
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
