package com.example.easydelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easydelivery.module.CreateAcount;
import com.example.easydelivery.module.CreateAucontforBussines;
import com.example.easydelivery.module.CrateAcountDelivery;
public class UserType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

    }
    public void userBuisnes(View view)
    {
        startActivity(new Intent(this, CreateAucontforBussines.class));
        finish();
    }
    public void userClient(View view)
    {
        startActivity(new Intent(this, CreateAcount.class));
        finish();
    }
    public void userDelivery(View view)
    {
        startActivity(new Intent(this, CrateAcountDelivery.class));
        finish();
    }
}