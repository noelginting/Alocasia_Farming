package com.example.ndg.alocasiafarming.Admin;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ndg.alocasiafarming.MainActivity;
import com.example.ndg.alocasiafarming.R;

public class Admin extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbaradmin,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
        AdminAdapter adminAdapter= new AdminAdapter(this,this.getSupportFragmentManager());
        viewPager.setAdapter(adminAdapter);
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
