package com.example.easydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenStore);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenStore:
                    intent = new Intent(Store.this, Store.class);
                    startActivity(intent);
                    return true;
                case R.id.fragmenCategory:
                    intent = new Intent(Store.this, Category.class);
                    startActivity(intent);
                    return true;
                case R.id.fragmenSearch:
                    intent = new Intent(Store.this, Search.class);
                    startActivity(intent);
                    return true;
                case R.id.fragmenUser:
                    intent = new Intent(Store.this, SettingUser.class);
                    startActivity(intent);
                    return true;
            }


            return false;
        }
    };

}