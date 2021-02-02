package com.example.easydelivery.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.easydelivery.R;
import com.example.easydelivery.module.CreateProduct;
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

    public void createProdcut(View view)
    {
        startActivity(new Intent(Store.this, CreateProduct.class));

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
                    finish();
                    return true;
                case R.id.fragmenCategory:
                    intent = new Intent(Store.this, Category.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenSearch:
                    intent = new Intent(Store.this, Search.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.fragmenUser:
                    intent = new Intent(Store.this, SettingUser.class);
                    startActivity(intent);
                    finish();
                    return true;
            }


            return false;
        }
    };

}