package com.example.easydelivery.views.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easydelivery.R;

public class UserTypeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

    }

    public void goToPreviousActivity(View view) {
        finish();
    }

    public void goToLoginActivity(View view)
    {
        startActivity(new Intent(this, LoginScreenActivity.class));
        finish();
    }

    public void userBusiness(View view)
    {
        startActivity(new Intent(this, RegisterBusinessScreenActivity.class));
        finish();
    }
    public void userClient(View view)
    {
        startActivity(new Intent(this, RegisterClientScreenActivity.class));
        finish();
    }
    public void userDelivery(View view)
    {
        startActivity(new Intent(this, RegisterDeliveryScreenActivity.class));
        finish();
    }
}