package com.example.easydelivery.views.activities.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easydelivery.R;
import com.example.easydelivery.UserType;
import com.example.easydelivery.views.activities.auth.LoginScreenActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void goToLogin(View view) {
        startActivity(new Intent( this, LoginScreenActivity.class));
    }

    public void goToRegister(View view) {
        startActivity(new Intent( this, UserType.class));
    }
}