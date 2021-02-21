package com.example.easydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.easydelivery.menu.Category;
import com.example.easydelivery.menu.Search;
import com.example.easydelivery.menu.SettingUser;
import com.example.easydelivery.menu.StoreForBusinnes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


  //  FragmentUser fragmentUser = new FragmentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = findViewById(R.id.BottonNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenProductsBusiness:
                     intent = new Intent(MainActivity.this, StoreForBusinnes.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenCategory:
                     intent = new Intent(MainActivity.this, Category.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenSearch:
                     intent = new Intent(MainActivity.this, Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenSettingUser:
                     intent = new Intent(MainActivity.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };


}