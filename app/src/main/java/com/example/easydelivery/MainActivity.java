package com.example.easydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentStore firstFragment = new FragmentStore();
    FragmentCategory secondFragment = new FragmentCategory();
    FragmentSearch thirdFragment = new FragmentSearch();
    FragmentUser fragmentUser = new FragmentUser();

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
            switch (item.getItemId()) {
                case R.id.fragmenStore:
                    loadFragment(firstFragment);
                    return true;

                case R.id.fragmenCategory:
                    loadFragment(secondFragment);
                    return true;

                case R.id.fragmenSearch:
                    loadFragment(thirdFragment);
                    return true;
                case R.id.fragmenUser:
                    loadFragment(fragmentUser);
                    return true;
            }


            return false;
        }
    };
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_conten,fragment);
        transaction.commit();
    }

}