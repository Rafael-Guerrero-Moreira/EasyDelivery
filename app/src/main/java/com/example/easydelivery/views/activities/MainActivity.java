package com.example.easydelivery.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.easydelivery.R;
import com.example.easydelivery.databinding.ActivityMainBinding;
import com.example.easydelivery.menu.Category;
import com.example.easydelivery.menu.Search;
import com.example.easydelivery.menu.SettingUser;
import com.example.easydelivery.menu.StoreForBusinnes;
import com.example.easydelivery.views.activities.fragments.Account;
import com.example.easydelivery.views.activities.fragments.Dashboard;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
  //  FragmentUser fragmentUser = new FragmentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        configViewPager(binding.amViewPager);
        binding.amTabLayout.setupWithViewPager(binding.amViewPager);
        binding.amTabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_dashboard_24);
        binding.amTabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_manage_accounts_24);
        binding.amViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        /*setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = findViewById(R.id.BottonNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

    private void configViewPager(ViewPager viewPager) {
        MainActivity.ViewSectionAdapter adapter = new ViewSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new Dashboard(), "Inicio");
        adapter.addFragment(new Account(), "Cuenta");
        viewPager.setAdapter(adapter);
    }

    private static class ViewSectionAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentsList = new ArrayList<>();
        private final List<String> fragmentsTitleList = new ArrayList<>();

        public ViewSectionAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fm, String title) {
            fragmentsList.add(fm);
            fragmentsTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitleList.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }


    }

    /*private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
    };*/


}