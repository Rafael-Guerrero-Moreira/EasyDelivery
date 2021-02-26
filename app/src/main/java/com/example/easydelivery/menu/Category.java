package com.example.easydelivery.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.easydelivery.R;
import com.example.easydelivery.generallist.ListProducts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenCategory);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenProductsBusiness:
                    intent = new Intent(Category.this, ListProducts.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenCategory:
                    intent = new Intent(Category.this, Category.class);
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.fragmenSearch:
                    intent = new Intent(Category.this, Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenSettingUser:
                    intent = new Intent(Category.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };

}